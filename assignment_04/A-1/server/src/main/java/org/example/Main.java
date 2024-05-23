package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.Level;
import org.example.server.SmartHomeServer;

import java.util.Arrays;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Configurator.initialize(new DefaultConfiguration());
        Configurator.setRootLevel(Level.INFO);

        if (args.length < 1) {
            LOGGER.error("ID is required");
        } else {
            try {
                int serverId = Integer.parseInt(args[0]);
                String[] iceArgs = Arrays.copyOfRange(args, 1, args.length);
                SmartHomeServer server = new SmartHomeServer(serverId, iceArgs);
                server.run();

            } catch (NumberFormatException e) {
                LOGGER.error("Invalid serverId format");
                e.printStackTrace();
            }
        }
    }
}