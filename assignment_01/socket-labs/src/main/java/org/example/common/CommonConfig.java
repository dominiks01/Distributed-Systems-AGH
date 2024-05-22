package org.example.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommonConfig {
	public static String host;
	public static int port;
	public static int multicastPort;
	public static String multicastAddress;

	private static final Logger LOGGER = LogManager.getLogger(CommonConfig.class);

	static {
		loadProperties();
	}

	private static void loadProperties() {
		Properties properties = new Properties();
		try (InputStream input = CommonConfig.class.getClassLoader().getResourceAsStream("commonconfig.properties")) {
			if (input == null) {
				LOGGER.error("Sorry, unable to find commonconfig.properties");
				return;
			}
			properties.load(input);

			host = properties.getProperty("host");
			port = Integer.parseInt(properties.getProperty("port"));
			multicastPort = Integer.parseInt(properties.getProperty("multicastPort"));
			multicastAddress = properties.getProperty("multicastAddress");
		} catch (IOException ex) {
			LOGGER.error("Unknown exception {}", ex.getMessage());
		}
	}
}