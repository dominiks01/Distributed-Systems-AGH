package org.example.devices;

import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import SmartHome.Detectors.SmokeDetector;
import SmartHome.Errors.ActionNotPermitted;


public class SmokeSensor extends Device implements SmokeDetector {
    private static final Logger LOGGER = LogManager.getLogger(MoveSensor.class);
    private static final String CONFIG_FILE = "config.properties";

    private static int MAX_SMOKE_DENSITY;
    private static String LOCATION;

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
    public int getSmokeDensity(Current current) throws ActionNotPermitted {
        int density = (int) (Math.random()%10000);
        LOGGER.info("Read actual density - current value [{}]", density);
        return density;
    }

    @Override
    public boolean isSave(Current current) throws ActionNotPermitted {
        int density = (int) (Math.random()%10000);
        LOGGER.info("Read safe status - current value [{}]", MAX_SMOKE_DENSITY > density);
        return MAX_SMOKE_DENSITY > density;
    }

    @Override
    public void setSensitivityLevel(int level, Current current) throws ActionNotPermitted {

    }

    @Override
    public int getSensitivityLevel(Current current) throws ActionNotPermitted {
        return 0;
    }


    @Override
    public String getLocation(Current current) throws ActionNotPermitted {
        LOGGER.trace("Read device location - current value [{}]", LOCATION);
        return LOCATION;
    }

    @Override
    public void setLocation(String location, Current current) throws ActionNotPermitted {

    }
}
