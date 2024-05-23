package smarthome.server;

import com.zeroc.Ice.*;
import com.zeroc.Ice.Object;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.devices.GeneralLights;
import org.example.devices.MoveSensor;
import org.example.devices.OutdoorLights;
import org.example.devices.SmokeSensor;


import java.util.ArrayList;
import java.util.List;

public class SmartServantLocator implements ServantLocator {
	private static final Logger LOGGER = LogManager.getLogger(SmartServantLocator.class);
	private final List<String> servantsNames = new ArrayList<>();
	private final String serverId;

	public SmartServantLocator(String serverId) {
		LOGGER.info("SmartServantLocator '{}'", serverId);
		this.serverId = serverId;
	}

	@Override
	public LocateResult locate(Current current) throws UserException {
		String servantName = current.id.name;
		LOGGER.info("Locate '{}'", servantName);
		ObjectAdapter adapter = current.adapter;

		if (getServantId(servantName).equals(serverId)) {
			String servantBaseName = getServantBaseName(servantName);
			servantsNames.add(servantName);
			switch (servantBaseName) {
				case "GeneralLights":
					GeneralLights generalLights = new GeneralLights();
					adapter.add(generalLights, new Identity(servantName, "GeneralLights"));
					return new ServantLocator.LocateResult(generalLights, null);
				case "OutdoorLights":
					OutdoorLights outdoorLights = new OutdoorLights();
					adapter.add(outdoorLights, new Identity(servantName, "OutdoorLights"));
					return new ServantLocator.LocateResult(outdoorLights, null);
				case "SmokeDetector":
					SmokeSensor smokeDetector = new SmokeSensor();
					adapter.add(smokeDetector, new Identity(servantName, "SmokeSensor"));
					return new ServantLocator.LocateResult(smokeDetector, null);
				case "MoveDetector":
					MoveSensor moveSensor = new MoveSensor();
					adapter.add(moveSensor, new Identity(servantName, "MoveSensor"));
					return new ServantLocator.LocateResult(moveSensor, null);
				default:
					LOGGER.error("UNKNOWN DEVICE");
			}
		}
		throw new RuntimeException("Invalid servant name");
	}

	@Override
	public void deactivate(String s) {

	}

	@Override
	public void finished(Current current, Object object, java.lang.Object o) throws UserException {

	}

	public void printServants() {
		LOGGER.info("Devices connected to the server:");
		for (String name : servantsNames) {
			LOGGER.info("> {}", name);
		}
	}

	private int getFirstServantIdIndex(String servantName) {
		int i = servantName.length();
		while (i > 0 && Character.isDigit(servantName.charAt(i - 1))) {
			i--;
		}
		return i;
	}

	private String getServantBaseName(String servantName) {
		return servantName.substring(0, getFirstServantIdIndex(servantName));
	}

	private String getServantId(String servantName) {
		return servantName.substring(getFirstServantIdIndex(servantName));
	}
}