package org.example.devices;

import SmartHome.Errors.SensitivityLevelOutOfRange;
import SmartHome.Interfaces.DailySchedule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.zeroc.Ice.Current;
import java.io.FileInputStream;
import java.util.Properties;
import java.io.IOException;

import SmartHome.Detectors.MoveDetector;
import SmartHome.Errors.ActionNotPermitted;
import SmartHome.Enums.Levels;

public class MoveSensor extends Device implements MoveDetector {
    private static final Logger LOGGER = LogManager.getLogger(MoveSensor.class);
    private static final String CONFIG_FILE = "config.properties";

    private static Levels SENSITIVITY;
    private static String LOCATION;
    private static int BATTERY;

    @Override
    public String toString() {
        return "MoveSensor{" +
                "mode=" + mode +
                '}';
    }

    private static boolean IS_CONNECTED;
    private static boolean ALARM;
    private static DailySchedule SCHEDULE;

    public MoveSensor(){
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(CONFIG_FILE));
            SENSITIVITY = Levels.MEDIUM;
            LOCATION = properties.getProperty("move.location");
        } catch (IOException e) {
            LOGGER.error("Cannot load settings from config.properties");
            SENSITIVITY = Levels.MEDIUM;
            LOCATION = "";
        }

        DailySchedule a = new DailySchedule();
        a.startHour = 15;
        a.endHour = 20;

        SCHEDULE = a;
    }

    @Override
    public boolean motionDetected(Current current) throws  ActionNotPermitted{
        if(super.mode == SmartHome.Enums.Mode.RESTRICTED) {
            LOGGER.error("GET SMOKE DENSITY - Cannot use device [{}] - RESTRICTED mode!", current.id);
            throw new ActionNotPermitted();
        }

        boolean move = Math.random()%2 == 1;
        LOGGER.info("Motion detector - current value [{}]", move);
        return move;
    }

    @Override
    public void activateAlarm(Current current) throws ActionNotPermitted {
        if(super.mode == SmartHome.Enums.Mode.RESTRICTED) {
            LOGGER.error("GET SMOKE DENSITY - Cannot use device [{}] - RESTRICTED mode!", current.id);
            throw new ActionNotPermitted();
        }
        LOGGER.info("activateAlarm - current value [{}]", ALARM);
        ALARM = true;
    }

    @Override
    public void deactivateAlarm(Current current) throws ActionNotPermitted {
        if(super.mode == SmartHome.Enums.Mode.RESTRICTED) {
            LOGGER.error("GET SMOKE DENSITY - Cannot use device [{}] - RESTRICTED mode!", current.id);
            throw new ActionNotPermitted();
        }
        LOGGER.info("deactivateAlarm - current value [{}]", ALARM);
        ALARM = false;
    }

    @Override
    public void setAlarm(DailySchedule schedule, Current current) throws ActionNotPermitted {
        if(super.mode == SmartHome.Enums.Mode.RESTRICTED) {
            LOGGER.error("setAlarm - Cannot use device [{}] - RESTRICTED mode!", current.id);
            throw new ActionNotPermitted();
        }

        SCHEDULE = schedule;
        LOGGER.info("Set alarm of [{}] - new value [{}]", current.id, SCHEDULE);
    }

    @Override
    public DailySchedule getAlarm(Current current) throws ActionNotPermitted {
        if(super.mode == SmartHome.Enums.Mode.RESTRICTED) {
            LOGGER.error("getAlarm - Cannot use device [{}] - RESTRICTED mode!", current.id);
            throw new ActionNotPermitted();
        }
        LOGGER.info("Get alarm of [{}] - current value [{}]", current.id, SCHEDULE);
        return SCHEDULE;
    }

    @Override
    public int getSensitivityLevel(Current current) {
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
        if(super.mode == SmartHome.Enums.Mode.RESTRICTED) {
            LOGGER.error("Set Smoke Sensor Sensitivity - Cannot use device [{}] - RESTRICTED mode!", current.id);
            throw new ActionNotPermitted();
        }

        LOGGER.info("Read device location - current value [{}]", LOCATION);
        return LOCATION;
    }

    @Override
    public void setLocation(String location, Current current) throws ActionNotPermitted {
        if(super.mode == SmartHome.Enums.Mode.RESTRICTED) {
            LOGGER.error("Set Smoke Sensor Sensitivity - Cannot use device [{}] - RESTRICTED mode!", current.id);
            throw new ActionNotPermitted();
        }

        LOCATION = location;
        LOGGER.info("Set device location - new value [{}]", LOCATION);}


    @Override
    public void setSensitivityLevel(int level, Current current) throws ActionNotPermitted, SmartHome.Errors.SensitivityLevelOutOfRange {
        if(super.mode == SmartHome.Enums.Mode.RESTRICTED) {
            LOGGER.error("Set Smoke Sensor Sensitivity - Cannot use device [{}] - RESTRICTED mode!", current.id);
            throw new ActionNotPermitted();
        }

        switch (level){
            case 1:
                SENSITIVITY = SmartHome.Enums.Levels.LOW;
                break;
            case 2:
                SENSITIVITY = SmartHome.Enums.Levels.MEDIUM;
                break;
            case 3:
                SENSITIVITY = SmartHome.Enums.Levels.HIGH;
                break;
            default:
                LOGGER.error("Set Smoke Sensor Sensitivity - Device [{}] - Sensitivity Out of Range!", current.id);
                throw new SensitivityLevelOutOfRange();
        }

        LOGGER.info("Update Sensitivity of [{}] - current value [{}]", current.id, SENSITIVITY);
    }




}
