package org.example.common;

public class RabbitMQConfig {
    public static final String SERVICE_REQUEST_QUEUE = "service-request-queue-";
    public static final String SERVICE_REQUEST_EXCHANGE = "service-request-exchange-";
    public static final String SERVICE_RESPONSE_QUEUE = "service-response-queue-";
    public static final String SERVICE_RESPONSE_EXCHANGE = "service-response-exchange-";
    public static final String ADMIN_EXCHANGE = "admin-exchange";
    public static final String ADMIN_QUEUE = "admin-queue-";
    public static final String ADMIN_RESPONSE_QUEUE = "admin-queue-";
    public static final String HOST = "localhost";

    private RabbitMQConfig() {
    }
}
