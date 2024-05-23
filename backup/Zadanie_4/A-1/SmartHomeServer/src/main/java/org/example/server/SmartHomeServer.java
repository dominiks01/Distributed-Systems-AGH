package org.example.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.zeroc.Ice.*;
import org.example.devices.GeneralLights;
import org.example.devices.MoveSensor;
import org.example.devices.SmokeSensor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Exception;

public class SmartHomeServer {
    private static final Logger LOGGER = LogManager.getLogger(SmartHomeServer.class);
    private static final String ADAPTER_NAME = "adapter";
    private static final String SERVANT_LOCATOR_PREFIX = "";
    private Communicator communicator;
    private final ObjectAdapter adapter;
    private smarthome.server.SmartServantLocator servantLocator;

    public SmartHomeServer(int serverId, String[] args) {
        for(int i = 0; i < args.length; i++)
            System.out.println("Element " + i + ":" + args[i]);

        this.communicator = Util.initialize(args);
        String adapterEndpoints = getAdapterEndpoints(serverId);
        this.adapter = communicator.createObjectAdapterWithEndpoints("SimpleSmartHome", "default -p 10000");
        LOGGER.info("Added adapter with endpoints '{}'", adapterEndpoints);
        this.servantLocator = new smarthome.server.SmartServantLocator(String.valueOf(serverId));
        adapter.addServantLocator(servantLocator, SERVANT_LOCATOR_PREFIX);
        LOGGER.info("Initialized the server");

        for (String deviceName : args) {
            switch (deviceName) {
                case "SmokeDetector":
                    LOGGER.info("Added SmokeDetector");
                    com.zeroc.Ice.Object object = new SmokeSensor();
                    adapter.add(object, this.communicator.stringToIdentity("SD1"));
                    break;
                case "MoveDetector":
                    // Utwórz czujnik ruchu
                    adapter.add(new SmokeSensor(), new Identity("MD1", "MoveDetector"));
                    break;
                // Dodaj kolejne przypadki dla innych typów urządzeń
                default:
                    LOGGER.error("Unknown device type: " + deviceName);
            }
        }

    }

    private String getAdapterEndpoints(int serverId) {
        int offset = 10;
        int offsetServerId = offset + serverId;
        return "tcp -h 127.0.0.!! -p 100!! : udp -h 127.0.0.!! -p 100!!"
                .replace("!!", String.valueOf(offsetServerId));
    }

    public void run() {
        adapter.activate();
        LOGGER.info("Entering event processing loop...");

        int status = 0;
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
                    this.servantLocator.printServants();
                    break;
                case "quit":
                    quit = true;
                    try {
                        adapter.deactivate();
                        communicator.shutdown();
                        communicator.destroy();
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                        status = 1;
                    }
                    break;
                default:
                    LOGGER.info("Invalid command");
            }
        }
        communicator.waitForShutdown();
        System.exit(status);
    }
}
