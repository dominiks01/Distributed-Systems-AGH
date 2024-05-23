package org.example.devices;

import com.zeroc.Ice.Current;
import SmartHome.Interfaces.SmartDevice;
import SmartHome.Enums.Mode;
import SmartHome.Errors.ActionNotPermitted;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Device implements SmartDevice {
    @Override
    public String toString() {
        return "Device{" +
                "mode=" + mode +
                '}';
    }

    protected Mode mode = Mode.ACTIVE;
    private static final Logger LOGGER = LogManager.getLogger(Device.class);

    @Override
    public void changeMode(Current current) {
        switch (mode){
            case Mode.RESTRICTED -> {
                mode = Mode.ACTIVE;
                break;
            }
            case Mode.ACTIVE -> {
                mode = Mode.RESTRICTED;
                break;
            }
        }
        LOGGER.info("Device [{}] mode changed: [{}]", current.id, mode);
    }

    @Override
    public Mode getMode(Current current) {
        LOGGER.info("Device [{}] get mode: [{}]", current.id , mode);
        return mode;
    }

    @Override
    public void _notify(Current current) throws ActionNotPermitted{
        LOGGER.info("Device [{}] notify", mode);
        throw new ActionNotPermitted();
    }
}
