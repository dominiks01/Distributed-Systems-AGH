package org.example.devices;

import SmartHome.Enums.Mode;
import SmartHome.Errors.ActionNotPermitted;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.OutputStream;
import com.zeroc.Ice.UserException;
import com.zeroc.IceInternal.Incoming;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.server.SmartHomeServer;

import java.util.concurrent.CompletionStage;
import SmartHome.Errors.ActionNotPermitted;

public class GeneralLights extends Device implements SmartHome.Lights.GeneralLights {
	private static final Logger LOGGER = LogManager.getLogger(GeneralLights.class);
	private String color = "";
	private Boolean ligths = false;

	@Override
	public void changeColor(String color, Current current) {
		LOGGER.info("[{}]: change color - new color [{}]", current.id, color);
	}

	@Override
	public String getColor(Current current) {
		LOGGER.info("[{}]: return color: [{}]", current.id, color);
		return color;
	}

	@Override
	public void turnOn(Current current) throws ActionNotPermitted {
		LOGGER.info("[{}]: turn on lights", current.id);
		ligths = true;
	}

	@Override
	public void turnOff(Current current) throws ActionNotPermitted {
		LOGGER.info("[{}]: turn off lights", current.id);
		ligths = false;
	}

	@Override
	public boolean isOn(Current current) throws ActionNotPermitted {
		LOGGER.info("[{}]: get lights status", current.id);
		return ligths;
	}
}
