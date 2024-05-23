package org.example.devices;

import SmartHome.Enums.Mode;
import SmartHome.Enums.Levels;
import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import SmartHome.Detectors.SmokeDetector;
import SmartHome.Errors.ActionNotPermitted;
import SmartHome.Errors.SensitivityLevelOutOfRange;

public class SmokeSensor extends Device implements SmokeDetector {
    private static final Logger LOGGER = LogManager.getLogger(MoveSensor.class);
    private static final String CONFIG_FILE = "config.properties";

    private static int MAX_SMOKE_DENSITY;
    private static String LOCATION;
    private static Levels SENSITIVITY;

    public SmokeSensor(){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(CONFIG_FILE));
            MAX_SMOKE_DENSITY = Integer.parseInt(properties.getProperty("smoke.max_density"));
            LOCATION = properties.getProperty("smoke.max_density");
        } catch (IOException e) {
            LOGGER.error("Cannot load settings from config.properties");
            MAX_SMOKE_DENSITY = 100;
            LOCATION = "";
        }
    }

    @Override
    public String toString() {
        return "SmokeSensor{" +
                "mode=" + mode +
                '}';
    }

    @Override
    public int getSmokeDensity(Current current) throws ActionNotPermitted {
        if(super.mode == Mode.RESTRICTED) {
            LOGGER.error("GET SMOKE DENSITY - Cannot use device [{}] - RESTRICTED mode!", current.id);
            throw new ActionNotPermitted();
        }

        LOGGER.info("Read actual density - current value [{}]", Math.random()%10000);
        return 1;
    }

    @Override
    public boolean isSave(Current current) throws ActionNotPermitted {
        if(super.mode == Mode.RESTRICTED) {
            LOGGER.error("IS SAFE DENSITY - Cannot use device [{}] - RESTRICTED mode!", current.id);
            throw new ActionNotPermitted();
        }

        int density = (int) (Math.random()%10000);
        LOGGER.info("Read safe status - current value [{}]", MAX_SMOKE_DENSITY > density);
        return MAX_SMOKE_DENSITY > density;
    }

    @Override
    public void setSensitivityLevel(int level, Current current) throws ActionNotPermitted, SensitivityLevelOutOfRange {
        if(super.mode == Mode.RESTRICTED) {
            LOGGER.error("Set Smoke Sensor Sensitivity - Cannot use device [{}] - RESTRICTED mode!", current.id);
            throw new ActionNotPermitted();
        }
        
        switch (level){
            case 1:
                SENSITIVITY = Levels.LOW;
                break;
            case 2:
                SENSITIVITY = Levels.MEDIUM;
                break;
            case 3:
                SENSITIVITY = Levels.HIGH;
                break;
            default:
                LOGGER.error("Set Smoke Sensor Sensitivity - Device [{}] - Sensitivity Out of Range!", current.id);
                throw new SensitivityLevelOutOfRange();
        }

        LOGGER.info("Update Sensitivity of [{}] - current value [{}]", current.id, SENSITIVITY);
    }

    @Override
    public int getSensitivityLevel(Current current) throws ActionNotPermitted {
        LOGGER.info("Get sensitivity of [{}] - current value [{}]", current.id, SENSITIVITY);

        switch (SENSITIVITY) {
            case Levels.LOW:
                return 1;
            case Levels.MEDIUM:
                return 2;
            case Levels.HIGH:
                return 3;
            default:
                return 0;
        }
    }


    @Override
    public String getLocation(Current current) throws ActionNotPermitted {
        if(super.mode == Mode.RESTRICTED) {
            LOGGER.error("Set Smoke Sensor Sensitivity - Cannot use device [{}] - RESTRICTED mode!", current.id);
            throw new ActionNotPermitted();
        }

        LOGGER.info("Read device location - current value [{}]", LOCATION);
        return LOCATION;
    }

    @Override
    public void setLocation(String location, Current current) throws ActionNotPermitted {
        if(super.mode == Mode.RESTRICTED) {
            LOGGER.error("Set Smoke Sensor Sensitivity - Cannot use device [{}] - RESTRICTED mode!", current.id);
            throw new ActionNotPermitted();
        }

        LOCATION = location;
        LOGGER.info("Set device location - new value [{}]", LOCATION);}
}
