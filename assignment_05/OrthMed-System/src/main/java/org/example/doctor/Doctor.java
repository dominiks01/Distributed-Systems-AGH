package org.example.doctor;

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
 * Represents a doctor user in the Orthopedic Medical System.
 */
public class Doctor extends OrthMedUser {
    private static final Logger LOGGER = LogManager.getLogger(Doctor.class);

    public Doctor(String name) {
        this.name = name;
    }

    /**
     * Requests a service from the technicians.
     * @param service The service to request.
     * @param clientName The client for whom the service was ordered.
     */
    private void requestService(Service service, String clientName) {
        try {
            String requestId = nextServiceRequestId();
            LOGGER.info("New service requested: '{}'", requestId);
            String message = requestId + clientName + "#" + service.toString();
            channel.basicPublish(RabbitMQConfig.SERVICE_REQUEST_EXCHANGE, service.toString(), null, message.getBytes());
        } catch (IOException e) {
            LOGGER.error("Failed to request a service, details: {}", e.getMessage());
        }
    }

    /**
     * Initialize doctor, connect to RabbitMQ and set up message queues.
     */
    @Override
    protected void init(){
        super.connect();

        try {
            channel.exchangeDeclare(RabbitMQConfig.SERVICE_RESPONSE_EXCHANGE, "direct", true);

            // Declare and bind the response queue
            String responseQueueName = RabbitMQConfig.SERVICE_RESPONSE_QUEUE + name;
            channel.queueDeclare(responseQueueName, true, false, false, null);
            channel.queueBind(responseQueueName, RabbitMQConfig.SERVICE_RESPONSE_EXCHANGE, name);

            // Start consuming messages from the response queue
            channel.basicConsume(responseQueueName, false, createConsumer());

            // Create admin queue
            String adminQueueName = ADMIN_QUEUE + name;
            channel.queueDeclare(adminQueueName, false, false, true, null); // non-durable and auto-delete
            channel.exchangeDeclare(RabbitMQConfig.ADMIN_EXCHANGE, "fanout", true);
            channel.queueBind(adminQueueName, RabbitMQConfig.ADMIN_EXCHANGE, ""); // Bind queue without routing key
            channel.basicConsume(adminQueueName, false, createAdminConsumer());
            channel.basicQos(1);

        } catch (IOException e) {
            LOGGER.error("Failed to connect, details: {}", e.getMessage());
        }

    }

    /**
     * Starts the doctor.
     */
    @Override
    public void start() {
        LOGGER.info("Starting doctor service '{}'", name);
        init();
        enterInputLoop();
    }

    /**
     * Creates a consumer for handling messages.
     * @return The created consumer.
     */
    @Override
    protected Consumer createConsumer() {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                String message = new String(body, CHARSET_NAME);
                LOGGER.info("Service completed: '{}'", message);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
    }

    /**
     * Enters the input loop for handling user commands.
     */
    @Override
    protected void enterInputLoop() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (true) {
                System.out.print("> ");
                String input = br.readLine();
                if (input.equals("quit")) {
                    end();
                    break;
                }
                if (input.equals("help")) {
                    listCommands();
                    continue;
                }

                String[] parts = input.split(" ");
                if (parts.length != 2) {
                    LOGGER.warn("Invalid command format! Expected: <service> <name>");
                    listCommands();
                    continue;
                }

                String serviceName = parts[0];
                String clientName = parts[1];
                Service service = Service.fromString(serviceName);

                if (service != null) {
                    requestService(service, clientName);
                } else {
                    LOGGER.warn("Unknown service: {}", serviceName);
                    listCommands();
                }
            }
        } catch (IOException e) {
            LOGGER.error("IOException occurred: {}", e.getMessage());
        } finally {
            end();
        }
    }

    /**
     * Starting the doctor.
     * @param args Command-line
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            LOGGER.error("Doctor name is required");
            return;
        }

        StringBuilder doctorNameBuilder = new StringBuilder(args[0]);
        for (int i = 1; i < args.length; i++) {
            doctorNameBuilder.append(" ").append(args[i]);
        }

        String doctorName = doctorNameBuilder.toString();
        Doctor doctor = new Doctor(doctorName);
        doctor.start();
    }

    /**
     * Lists available commands for the doctor.
     */
    private void listCommands(){
        LOGGER.info("Available services:");
        for (Service service : Service.values()) {
            LOGGER.info("> {}", service);
        }
    }
}
