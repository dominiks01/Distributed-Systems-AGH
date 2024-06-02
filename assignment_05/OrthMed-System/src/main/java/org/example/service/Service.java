package org.example.service;

public enum Service {
    HIP, KNEE, ELBOW;

    public static Service fromString(String text) {
        for (Service service : Service.values()) {
            if (service.name().equalsIgnoreCase(text)) {
                return service;
            }
        }
        return null;
    }
}
