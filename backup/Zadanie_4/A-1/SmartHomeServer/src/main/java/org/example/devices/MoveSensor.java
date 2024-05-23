package org.example.devices;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.zeroc.Ice.Current;
import java.io.FileInputStream;
import java.util.Properties;
import java.io.IOException;

import SmartHome.Detectors.MoveDetector;
import SmartHome.Errors.ActionNotPermitted;

public class MoveSensor extends Device implements MoveDetector {
    private static final Logger logger = LogManager.getLogger(MoveSensor.class);
    private static final String CONFIG_FILE = "config.properties";

    private static int SENSITIVITY;
    private static String LOCATION;
    private static int BATTERY;
    private static boolean IS_CONNECTED;

    public MoveSensor(){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(CONFIG_FILE));
            SENSITIVITY = Integer.parseInt(properties.getProperty("move.sensitivity"));
            LOCATION = properties.getProperty("move.location");
        } catch (IOException e) {
            logger.error("Cannot load settings from config.properties");
            SENSITIVITY = 10;
            LOCATION = "";
        }
    }

    @Override
    public boolean motionDetected(Current current) {
        return Math.random()%2 == 1;
    }

    @Override
    public int getSensitivityLevel(Current current) {
        return SENSITIVITY;
    }

    @Override
    public String getLocation(Current current) throws ActionNotPermitted {
        return "";
    }

    @Override
    public void setLocation(String location, Current current) throws ActionNotPermitted {

    }

    @Override
    public void setSensitivityLevel(int sensitivity, Current current) throws ActionNotPermitted {
        logger.trace("Update Sensitivity Level of MotionSensor, new value: [{}]", sensitivity);
        SENSITIVITY = sensitivity;
    }



}
