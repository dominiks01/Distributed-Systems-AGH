package org.example.technician;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.example.common.RabbitMQConfig.*;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.common.OrthMedUser;
import org.example.common.RabbitMQConfig;
import org.example.service.Service;

/**
 * Represents a technician user.
 */
public class Technician extends OrthMedUser {
    private static final Logger LOGGER = LogManager.getLogger(Technician.class);
    private final Service[] services;
    private static final int SERVICE_TIME_MS = 50;

    public Technician(String name, Service[] services) {
        this.name = name;
        this.services = services;
    }

    @Override
    protected void init(){
        super.connect();

        try {
            for (Service service : services) {
                String serviceName = service.toString();
                String serviceRequestQueueName = serviceName.toLowerCase() + "_queue";
                channel.queueDeclare(serviceRequestQueueName, true, false, false, null);
                channel.queueBind(serviceRequestQueueName, SERVICE_REQUEST_EXCHANGE, serviceName);
                channel.basicConsume(serviceRequestQueueName, false, createConsumer());
                LOGGER.info("Technician '{}' is waiting for service requests on '{}'", name, serviceRequestQueueName);
            }

            String adminQueueName = ADMIN_QUEUE + name;
            channel.queueDeclare(adminQueueName, false, false, true, null);
            channel.exchangeDeclare(RabbitMQConfig.ADMIN_EXCHANGE, "fanout", true);
            channel.queueBind(adminQueueName, RabbitMQConfig.ADMIN_EXCHANGE, ""); // Bind queue without routing key
            channel.basicConsume(adminQueueName, false, createAdminConsumer());
            channel.basicQos(1);

        } catch (IOException e) {
            LOGGER.error("Failed to connect, details: {}", e.getMessage());
        }
    }


    @Override
    public void start() {
        LOGGER.info("Starting technician '{}'", name);
        init();
        enterInputLoop();
    }

    /**
     * Creates a consumer for handling incoming service requests.
     *
     * @return The created consumer.
     */
    @Override
    protected Consumer createConsumer() {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, CHARSET_NAME);
                handleServiceRequest(message, envelope);
            }
        };
    }

    /**
     * Handles a service request.
     *
     * @param message  The service request message.
     * @param envelope The message envelope.
     * @throws IOException If an IO error occurs.
     */
    private void handleServiceRequest(String message, Envelope envelope) throws IOException {
        LOGGER.info("Received request '{}', processing time: {}ms", message, SERVICE_TIME_MS);
        String result = "";

        try {
            Thread.sleep(SERVICE_TIME_MS);
            result = message + " done";
        } catch (InterruptedException ignored) {
            LOGGER.info("Could not process request ;/ ");
        }

        // Assuming the message format is 'doctorName#serviceId#serviceType#message'
        String[] messageInfo = message.split("#");
        if (messageInfo.length < 2) {
            LOGGER.error("Invalid message format: '{}'", message);
            channel.basicReject(envelope.getDeliveryTag(), false);
            return;
        }

        String doctorName = messageInfo[1];
        String serviceId = messageInfo[2];
        String serviceName = messageInfo[4];

        LOGGER.info("Processed request '{}', sending results to the doctor {}", message, doctorName);
        channel.basicPublish(RabbitMQConfig.SERVICE_RESPONSE_EXCHANGE, doctorName, null, result.getBytes());
        channel.basicPublish(RabbitMQConfig.SERVICE_RESPONSE_EXCHANGE, "admin", null, result.getBytes());
        LOGGER.info("Completed service request id: '{}'", serviceId);

        // Acknowledge the message
        channel.basicAck(envelope.getDeliveryTag(), false);
    }

    /**
     * Enters the input loop for handling user commands.
     */
    @Override
    protected void enterInputLoop() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean interrupted = false;
        try {
            while (true) {
                try {
                    System.out.print("> ");
                    String input = br.readLine();
                    if (input.equals("quit")) {
                        break;
                    }
                } catch (IOException e) {
                    LOGGER.error("Error reading input, details: {}", e.getMessage());
                    interrupted = true;
                    break;
                }
            }
        } finally {
            end();
            if (interrupted) {
                LOGGER.error("Input loop was interrupted due to an IOException.");
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            LOGGER.error("Technician name and at least one service type are required");
            return;
        }
        String technicianName = args[0];
        Service[] technicianServices = new Service[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            technicianServices[i - 1] = Service.fromString(args[i]);
        }

        Technician technician = new Technician(technicianName, technicianServices);
        technician.start();
    }
}
