package org.example.common;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.example.common.RabbitMQConfig.*;

import com.rabbitmq.client.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
* Abstract class representing user of System
*/
public abstract class OrthMedUser {
    private static final Logger LOGGER = LogManager.getLogger(OrthMedUser.class);
    protected Channel channel;
    protected String name;

    protected static final String CHARSET_NAME = "UTF-8";

    private final AtomicInteger serviceRequestIdCounter = new AtomicInteger(0);

    /**
     * Initialize new user
     */
    protected abstract void init();

    /**
    *  Starts user
    *  */
    protected abstract void start();

    /**
    * Create consumer for incoming messages
    * @return created consumer
    *  */
    protected abstract Consumer createConsumer();

    /**
     * Main loop for client-system interaction
     */
    protected abstract void enterInputLoop();

    /**
     * Connect to RabbitMQ server.
     */
    protected void connect() {
        LOGGER.info("Connecting to RabbitMQ server...");
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);

        try {
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            LOGGER.info("Connected to RabbitMQ server successfully.");
        } catch (IOException | TimeoutException e) {
            LOGGER.error("Failed to connect to RabbitMQ server: {}", e.getMessage());
        }
    }

    /**
     * Ends the user session, closing connections and releasing resources.
     */
    protected void end(){
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
                LOGGER.info("Channel closed.");
            }
            LOGGER.info("User session ended.");
        } catch (IOException | TimeoutException e) {
            LOGGER.error("Failed to close RabbitMQ channel: {}", e.getMessage());
        }
    }

    /**
     * Generate unique service request ID
     * @return unique request ID
     */
    protected String nextServiceRequestId() {
        return  "#" + name + "#" + serviceRequestIdCounter.incrementAndGet() + "#";
    }

    /**
     * Creates admin messages consumer
     * @return The created administrator messages consumer
     * */
    protected Consumer createAdminConsumer() {
        return new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, CHARSET_NAME);
                LOGGER.info("Received administrative message: '{}'", message);
            }
        };
    }

}
