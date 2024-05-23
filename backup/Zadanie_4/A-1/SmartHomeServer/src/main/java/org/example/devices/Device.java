package org.example.devices;

import com.zeroc.Ice.Current;
import SmartHome.Interfaces.SmartDevice;
import SmartHome.Enums.Mode;
import SmartHome.Errors.ActionNotPermitted;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Device implements SmartDevice {
    private Mode mode = SmartHome.Enums.Mode.Restricted;
    private static final Logger LOGGER = LogManager.getLogger(Device.class);

    @Override
    public void changeMode(Current current) {
        switch (mode){
            case Mode.Restricted -> {
                mode = Mode.Active;
                break;
            }
            case Mode.Active -> {
                mode = Mode.Restricted;
                break;
            }
        }
        LOGGER.info("Device [{}] mode changed: [{}]", current.id, mode);
    }

    @Override
    public Mode getMode(Current current) {
        LOGGER.info("Device [{}] get mode: [{}]", mode);
        return mode;
    }

    @Override
    public void _notify(Current current) throws ActionNotPermitted{
        LOGGER.info("Device [{}] notify", mode);
        throw new ActionNotPermitted();
    }
}
