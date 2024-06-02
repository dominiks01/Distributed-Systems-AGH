package org.example.administrator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.rabbitmq.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.common.OrthMedUser;
import org.example.common.RabbitMQConfig;
import org.example.service.Service;

/**
 * Represents an administrator user in the Orthopedic Medical System.
 */
public class Administrator extends OrthMedUser {
    private static final Logger LOGGER = LogManager.getLogger(Administrator.class);

    public Administrator(String name) {
        this.name = name;
    }

    /**
     * Initializes the administrator, including connecting to RabbitMQ and setting up message queues.
     */
    protected void init() {
        try {
            super.connect();

            // Declare new exchange of fanout type.
            channel.exchangeDeclare(RabbitMQConfig.ADMIN_EXCHANGE, "fanout", true);

            // Bind to all services
            for (Service service : Service.values()) {
                String serviceName = service.toString();

                // Bind to service requests.
                String serviceRequestQueueName = RabbitMQConfig.SERVICE_REQUEST_QUEUE + serviceName;
                channel.queueDeclare(serviceRequestQueueName, true, false, false, null);
                channel.queueBind(serviceRequestQueueName, RabbitMQConfig.SERVICE_REQUEST_EXCHANGE, serviceName);
                channel.basicConsume(serviceRequestQueueName, false, createConsumer());


            }

            String serviceResponseQueueName = RabbitMQConfig.SERVICE_RESPONSE_QUEUE + "admin";
            channel.queueDeclare(serviceResponseQueueName, true, false, false, null);
            channel.queueBind(serviceResponseQueueName, RabbitMQConfig.SERVICE_RESPONSE_EXCHANGE, "admin");
            channel.basicConsume(serviceResponseQueueName, false, createConsumer());

            LOGGER.info("Administrator '{}' initialized successfully.", name);
        } catch (IOException e) {
            LOGGER.error("Failed to connect, exception: {}", e.getMessage());
            for (Throwable t = e; t != null; t = t.getCause()) {
                LOGGER.error("Caused by: {}", t.toString());
            }
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred during connection setup: {}", e.getMessage());
        }
    }

    /**
     * Starts the administrator user.
     */
    @Override
    public void start() {
        LOGGER.info("Starting administrator '{}'", name);
        init();
        enterInputLoop();
    }

    /**
     * Creates a consumer for handling incoming messages.
     * @return The created consumer.
     */
    @Override
    protected Consumer createConsumer() {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, CHARSET_NAME);
                LOGGER.info("Received message: '{}'", message);
                channel.basicAck(envelope.getDeliveryTag(), false);
                System.out.print("> ");
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
                try {
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
                    if (input.startsWith("info ")) {
                        String infoMessage = input.substring(5);
                        sendInfoMessage(infoMessage);
                    } else {
                        LOGGER.warn("Unknown command!");
                        listCommands();
                    }
                } catch (IOException e) {
                    LOGGER.error("Error reading input, details: {}", e.getMessage());
                    listCommands();
                }
            }
        } catch (Exception e) {
            LOGGER.error("Unexpected error occurred during input loop: {}", e.getMessage());
        } finally {
            end();
        }
    }

    /**
     * Sends an informational message to all users.
     * @param message The message to send.
     */
    private void sendInfoMessage(String message) {
        try {
            channel.basicPublish(RabbitMQConfig.ADMIN_EXCHANGE, "", null, message.getBytes());
            LOGGER.info("Sent info message: '{}'", message);
        } catch (IOException e) {
            LOGGER.error("Failed to send info message, details: {}", e.getMessage());
        }
    }

    /**
     * Lists available commands for the administrator.
     */
    private void listCommands() {
        LOGGER.info("Available commands:");
        LOGGER.info("> info <message> - Send an informational message to all users");
        LOGGER.info("> quit - Exit the application");
    }

    /**
     * Entry point for starting the administrator.
     * @param args Command-line arguments. The first argument is the administrator name.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            LOGGER.error("Administrator name is required");
            return;
        }

        String doctorName = args[0];
        Administrator doctor = new Administrator(doctorName);
        doctor.start();
    }

}
