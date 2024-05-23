package org.example.devices;

import SmartHome.Errors.ActionNotPermitted;
import SmartHome.Errors.BrightnessLevelOutOfRange;
import com.zeroc.Ice.Current;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class OutdoorLights extends Device implements SmartHome.Lights.OutdoorLights {
	private static final Logger LOGGER = LogManager.getLogger(OutdoorLights.class);
	private int brightnessLevel = 0;
	private boolean ligths = false;

	@Override
	public void adjustBrightness(int level, Current current) throws ActionNotPermitted, BrightnessLevelOutOfRange {
		if(super.mode == SmartHome.Enums.Mode.RESTRICTED) {
			LOGGER.error("adjustBrightness - Cannot use device [{}] - RESTRICTED mode!", current.id);
			throw new ActionNotPermitted();
		}

		if(level > 3 || level < 0)
			throw new BrightnessLevelOutOfRange();

		LOGGER.info("[{}]: adjust brightness level - new level: [{}]", current.id, level);
		brightnessLevel = level;
	}

	@Override
	public String toString() {
		return "OutdoorLights{" +
				"ligths=" + ligths +
				", brightnessLevel=" + brightnessLevel +
				", mode=" + mode +
				'}';
	}

	@Override
	public int getBrightnessLevel(Current current) throws ActionNotPermitted {
		if(super.mode == SmartHome.Enums.Mode.RESTRICTED) {
			LOGGER.error("getBrightnessLevel - Cannot use device [{}] - RESTRICTED mode!", current.id);
			throw new ActionNotPermitted();
		}

		LOGGER.info("[{}]: get brightness level - new level: [{}]", current.id, brightnessLevel);
		return brightnessLevel;
	}

	@Override
	public void turnOn(Current current) throws ActionNotPermitted {
		if(super.mode == SmartHome.Enums.Mode.RESTRICTED) {
			LOGGER.error("turnOn - Cannot use device [{}] - RESTRICTED mode!", current.id);
			throw new ActionNotPermitted();
		}

		LOGGER.info("[{}]: turn off lights", current.id);
		ligths = false;
	}

	@Override
	public void turnOff(Current current) throws ActionNotPermitted {
		if(super.mode == SmartHome.Enums.Mode.RESTRICTED) {
			LOGGER.error("turnOff - Cannot use device [{}] - RESTRICTED mode!", current.id);
			throw new ActionNotPermitted();
		}

		LOGGER.info("[{}]: turn on lights", current.id);
		ligths = true;
	}

	@Override
	public boolean isOn(Current current) throws ActionNotPermitted {
		if(super.mode == SmartHome.Enums.Mode.RESTRICTED) {
			LOGGER.error("isOn - Cannot use device [{}] - RESTRICTED mode!", current.id);
			throw new ActionNotPermitted();
		}

		LOGGER.info("[{}]: get lights status", current.id);
		return ligths;
	}
}
