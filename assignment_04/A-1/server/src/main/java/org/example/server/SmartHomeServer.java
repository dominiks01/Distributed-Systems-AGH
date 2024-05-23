package org.example.server;

import SmartHome.Detectors.MoveDetector;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.zeroc.Ice.*;
import org.example.devices.GeneralLights;
import org.example.devices.MoveSensor;
import org.example.devices.OutdoorLights;
import org.example.devices.SmokeSensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Exception;
import java.util.ArrayList;

public class SmartHomeServer {
    private static final Logger LOGGER = LogManager.getLogger(SmartHomeServer.class);
    private Communicator communicator;
    private final ObjectAdapter adapter;

    private ArrayList<String> devices = new ArrayList<>();

    public SmartHomeServer(int ID, String[] args) {
        this.communicator = Util.initialize(args);

        String adapterEndpoints = generateAdapterEndpoints(ID);
        this.adapter = communicator.createObjectAdapterWithEndpoints("SmartHomeAdapter", adapterEndpoints);

        LOGGER.info("Added adapter with endpoints '{}'", adapterEndpoints);
        LOGGER.info("Initialized the server");

        com.zeroc.Ice.Object object;
        Identity identity;
        for (String deviceName : args) {
            switch (deviceName) {
                case "SmokeDetector":
                    LOGGER.info("Added SmokeDetector");
                    object = new SmokeSensor();
                    identity = this.communicator.stringToIdentity("SmokeDetector");
                    devices.add(object.toString() + identity);
                    adapter.add(object, identity);
                    break;

                case "MoveDetector":
                    LOGGER.info("Added MoveDetector");
                    object = new MoveSensor();
                    identity = this.communicator.stringToIdentity("MoveDetector");
                    adapter.add(object, identity);
                    devices.add(object.toString() + identity);
                    break;

                case "GeneralLights":
                    LOGGER.info("Added GeneralLights");
                    object = new GeneralLights();
                    identity = this.communicator.stringToIdentity("GeneralLights");
                    adapter.add(object, identity);
                    devices.add(object.toString() + identity);
                    break;

                case "OutdoorLights":
                    LOGGER.info("Added OutdoorLights");
                    object = new OutdoorLights();
                    identity = this.communicator.stringToIdentity("OutdoorLights");
                    adapter.add(object, identity);
                    devices.add(object.toString() + identity);
                    break;
                default:
                    LOGGER.error("Unknown device type: " + deviceName);
            }
        }

    }

    public static String generateAdapterEndpoints(int serverId) {
        serverId = serverId % 255;

        StringBuilder builder = new StringBuilder();
            builder.append("tcp -h 127.0.0.").append(serverId)
                    .append(" -p 1234").append(serverId)
                    .append(" : udp -h 127.0.0.").append(serverId)
                    .append(" -p 1235").append(serverId);

        return builder.toString();
    }

    public void run() {
        adapter.activate();
        LOGGER.info("Main loop");
        boolean quit = false;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while (!quit) {
            String line;
            try {
                line = in.readLine();
            } catch (IOException e) {
                break;
            }

            switch (line) {
                case "devices":
                    System.out.println(this.devices);
                    break;
                case "quit":
                    quit = true;
                    try {
                        adapter.deactivate();
                        communicator.shutdown();
                        communicator.destroy();
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                    }
                    break;
                default:
                    LOGGER.info("Invalid command");
            }
        }
        communicator.waitForShutdown();
    }
}
