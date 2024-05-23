package org.example.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.common.CommonConfig;
import org.example.common.MessageExchange;
import org.example.common.MessageType;
import org.example.communication_handlers.MulticastInputCommunicationHandler;
import org.example.communication_handlers.TcpInputCommunicationHandler;
import org.example.communication_handlers.UdpInputCommunicationHandler;

import java.io.*;
import java.net.*;

import static org.example.common.Colors.RESET;
import static org.example.common.Colors.RED;


public class JavaTcpClient {
	private final static BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
	private static String username;
	private static Socket tcpSocket;
	private static DatagramSocket udpSocket;
	private static MulticastSocket multicastSocket;


	private static final Logger LOGGER = LogManager.getLogger(JavaTcpClient.class);

	private static Thread tcpIncomingMessagesHandler;
	private static Thread udpIncomingMessagesHandler;
	private static Thread multicastMessagesHandler;

	private static ObjectOutputStream out;

	private static InetAddress address;
	private static InetAddress multicastAddress;

	public static void main(String[] args) throws IOException {

		LOGGER.info("Please, enter your username: ");
		username = inputReader.readLine();

		if (username == null)
			System.exit(2);

		initialize();

		try {
			tcpIncomingMessagesHandler.start();
			udpIncomingMessagesHandler.start();
			multicastMessagesHandler.start();

			MessageExchange oneTimeMessage = new MessageExchange(MessageType.COMMUNICATION, username + " has joined the chat!", username);
			out.writeObject(oneTimeMessage);

			DatagramPacket sendPacket = new DatagramPacket(oneTimeMessage.getBytes(), oneTimeMessage.getBytes().length, address, CommonConfig.port);
			udpSocket.send(sendPacket);

			while (!java.lang.Thread.currentThread().isInterrupted()) {
				MessageExchange newMessage = new MessageExchange(MessageType.MESSAGE, inputReader.readLine(), username);

				switch (newMessage.message) {
					case "U": {
						newMessage.message =
								"""
										                   ,.ood888888888888boo.,
										              .od888P^""            ""^Y888bo.
										          .od8P''   ..oood88888888booo.    ``Y8bo.
										       .odP'"  .ood8888888888888888888888boo.  "`Ybo.
										     .d8'   od8'd888888888f`8888't888888888b`8bo   `Yb.
										    d8'  od8^   8888888888[  `'  ]8888888888   ^8bo  `8b
										  .8P  d88'     8888888888P      Y8888888888     `88b  Y8.
										 d8' .d8'       `Y88888888'      `88888888P'       `8b. `8b
										.8P .88P            \"\"\"\"            \"\"\"\"            Y88. Y8.
										88  888                                              888  88
										88  888                                              888  88
										88  888.        ..                        ..        .888  88
										`8b `88b,     d8888b.od8bo.      .od8bo.d8888b     ,d88' d8'
										 Y8. `Y88.    8888888888888b    d8888888888888    .88P' .8P
										  `8b  Y88b.  `88888888888888  88888888888888'  .d88P  d8'
										    Y8.  ^Y88bod8888888888888..8888888888888bod88P^  .8P
										     `Y8.   ^Y888888888888888LS888888888888888P^   .8P'
										       `^Yb.,  `^^Y8888888888888888888888P^^'  ,.dP^'
										          `^Y8b..   ``^^^Y88888888P^^^'    ..d8P^'
										              `^Y888bo.,            ,.od888P^'
										                   "`^^Y888888888888P^^'"
										                  \s""";

						sendPacket = new DatagramPacket(newMessage.getBytes(), newMessage.getBytes().length, address, CommonConfig.port);
						udpSocket.send(sendPacket);
						System.out.print(">: ");
						break;
					}

					case "M": {
						newMessage.message = "Multicast org.example.common.Message!";
						sendPacket = new DatagramPacket(newMessage.getBytes(), newMessage.getBytes().length, multicastAddress, CommonConfig.multicastPort);
						multicastSocket.send(sendPacket);
						System.out.print(">: ");
						break;
					}

					default:
						out.writeObject(newMessage);
						System.out.print(">: ");
						break;
				}
			}
		} catch (Exception e) {
			LOGGER.error(RED + "Disconnecting client from chat due to interruption!" + RESET);
			System.exit(2);
		} finally {
			if (!tcpSocket.isClosed())
				tcpSocket.close();

			if (!udpSocket.isClosed())
				udpSocket.close();

			if (!multicastSocket.isClosed())
				multicastSocket.close();
		}
	}

	private static void initialize() throws IOException {
		try {
			tcpSocket = new Socket(CommonConfig.host, CommonConfig.port);
			udpSocket = new DatagramSocket();
			multicastSocket = new MulticastSocket(CommonConfig.multicastPort);
		} catch (ConnectException e) {
			LOGGER.error("Client could not connect to server! Check your configuration");
			System.exit(2);
		}

		// Read config data
		address = InetAddress.getByName(CommonConfig.host);
		multicastAddress = InetAddress.getByName(CommonConfig.multicastAddress);

		// Join multicast group for multicast messaging
		multicastSocket.joinGroup(new InetSocketAddress(multicastAddress, CommonConfig.port), NetworkInterface.getByInetAddress(multicastAddress));

		// Each handler will work independently
		tcpIncomingMessagesHandler = new TcpInputCommunicationHandler(tcpSocket);
		udpIncomingMessagesHandler = new UdpInputCommunicationHandler(udpSocket);
		multicastMessagesHandler = new MulticastInputCommunicationHandler(multicastSocket);

		out = new ObjectOutputStream(tcpSocket.getOutputStream());

		// ShutdownHook - interrupt all Threads on shutdown to force them to close communication
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			MessageExchange newMessage = new MessageExchange(MessageType.DISCONNECT, "Disconnecting...", username);
			try {
				tcpIncomingMessagesHandler.interrupt();
				udpIncomingMessagesHandler.interrupt();
				multicastMessagesHandler.interrupt();

				out.writeObject(newMessage);
				udpSocket.send(new DatagramPacket(newMessage.getBytes(), newMessage.getBytes().length, address, CommonConfig.port));

			} catch (IOException e) {
				LOGGER.error("Unknown exception{}", e.getMessage());
				throw new RuntimeException(e);
			}
		}));
	}
}
