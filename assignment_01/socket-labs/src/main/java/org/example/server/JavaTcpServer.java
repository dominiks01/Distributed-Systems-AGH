package org.example.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.common.MessageType;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

import org.example.common.CommonConfig;
import org.example.common.MessageExchange;

/*
    Ports for Server-Client communication are defined in org.example.common.CommonConfig Class
    Server is listening on port and when new connection occurs, new thread is created
    to handle message exchange.

    Multicast communication is created with MulticastSocket and MulticastSocket.
    Server does not take part in Multicast Communication (Clients only).

    Server has one UDP Channel that is gathering all Datagram Packets
    Number of Client's TCP thread's is not limited!

    Server spreads information to all TCP Client's via Array of PrintWriters
    (ObjectOutputStream)

    Messages are defined as follows:
        * Type : org.example.common.MessageType
        * Username: String
        * org.example.common.Message: String
*/

public class JavaTcpServer {
	private static ServerSocket tcpSocket;
	private static ArrayList<Integer> udpSockets;
	private static ArrayList<ObjectOutputStream> tcpPrintWriters;
	private static DatagramSocket udpSocket;

	private static final Logger LOGGER = LogManager.getLogger(JavaTcpServer.class);

	/**
	 * Send message to Client via UDP
	 *
	 * @param message - type of MessageExchange
	 * @param address - destination address
	 * @param port    - destination port
	 */
	private static void sendUdpMessage(MessageExchange message, InetAddress address, Integer port) {
		try {
			byte[] msg = message.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, address, port);
			udpSocket.send(sendPacket);
			LOGGER.info("Message sent via UDP to %s:%d{}{}", address.getHostAddress(), port);
		} catch (IOException e) {
			LOGGER.error("UDP sender - unknown exception: %s:{}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * With given ObjectOutputStream send message via TCP
	 *
	 * @param OOStream - Client Output
	 * @param message  - type of MessageExchange
	 */
	private static void sendTcpMessage(ObjectOutputStream OOStream, MessageExchange message) {
		try {
			OOStream.writeObject(message);
			LOGGER.info("Message sent via TCP to with ObjectOutputStream");
		} catch (IOException e) {
			LOGGER.error("TCP sender - unknown exception: %s:{}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws IOException {
		LOGGER.info("Initializing sever...");

		// TCP and UDP socket handlers
		tcpPrintWriters = new ArrayList<>();
		udpSockets = new ArrayList<>();

		// Starting up sockets
		tcpSocket = new ServerSocket(CommonConfig.port);
		udpSocket = new DatagramSocket(CommonConfig.port);

		LOGGER.info("Server starts listening on port: {}", CommonConfig.port);

		// Starting UDP Connection
		Thread udpConnectionHandler = new Thread(() -> {
			try {
				LOGGER.info("Starting new UDP Connection");
				byte[] receiveBuffer = new byte[4096];

				// Process messages till interruption
				while (!Thread.currentThread().isInterrupted()) {

					// Clear Buffer before receiving new message
					Arrays.fill(receiveBuffer, (byte) 0);

					// Handle receiving new message
					DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
					udpSocket.receive(receivePacket);

					// New ObjectInputStream due to loop receiving old message
					ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData()));
					MessageExchange message = (MessageExchange) iStream.readObject();
					iStream.close();

					// Do not spread Connect org.example.common.Message - Add to UDP Sockets if not there
					if (message.type == MessageType.CONNECT || !udpSockets.contains(receivePacket.getPort())) {
						udpSockets.add(receivePacket.getPort());
						continue;
					}

					// Remove port if not necessary
					if (message.type == MessageType.DISCONNECT && udpSockets.contains(receivePacket.getPort())) {
						udpSockets.remove((Object) receivePacket.getPort());
						continue;
					}

					LOGGER.info("UDP channel received message: type={} content: \"[ASCII ART MESSAGE]\" from {}", message.type, message.username);

					// Spread message to all other users connected to server!
					udpSockets.stream()
							.filter(x -> x != receivePacket.getPort())
							.forEach(entry -> {
								sendUdpMessage(message, receivePacket.getAddress(), entry);
							});

				}
			} catch (ClassNotFoundException e) {
				LOGGER.error("Stopping UDP channel due to ClassNotFoundException: {}", e.getMessage());
			} catch (IOException e) {
				LOGGER.error("Stopping UDP channel due to exception: {}", e.getMessage());
			} finally {
				LOGGER.info("Stopping UDP channel");
				udpSocket.close();
			}
		});

		udpConnectionHandler.start();

		try {
			while (!Thread.currentThread().isInterrupted()) {

				// Accept new connection!
				Socket clientSocket = tcpSocket.accept();
				LOGGER.info("New TCP connection from: {}", clientSocket.getInetAddress());

				// New Thread for connection handling
				Thread clientThread = new Thread(() -> {
					try {

						// Create Object Streams to handle connections
						ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
						ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

						// Add new Output stream to array of handlers
						tcpPrintWriters.add(out);

						// Send Hello Message
						out.writeObject(new MessageExchange(MessageType.COMMUNICATION, "No. users currently at chat: [" + tcpPrintWriters.size() + "]", ""));

						while (!clientSocket.isClosed()) {
							// Read message from client
							MessageExchange message = (MessageExchange) in.readObject();
							LOGGER.info("TCP channel received message: type={} content: \"{}\" from {}", message.type, message.message, message.username);

							// Handle message request
							// Disconnect -> close connection and spread information that user has
							// left the Chat.
							if (message.type == MessageType.DISCONNECT) {
								tcpPrintWriters.remove(out);
								message.type = MessageType.COMMUNICATION;
								message.message = "User: " + message.username + " has left the chat!";

								tcpPrintWriters.stream()
										.filter(x -> x != out)
										.forEach(entry -> {
											sendTcpMessage(entry, message);
										});

								Thread.currentThread().interrupt();
								return;
							}

							tcpPrintWriters.stream()
									.filter(x -> x != out)
									.forEach(entry -> {
										sendTcpMessage(entry, message);
									});
						}

					} catch (InterruptedIOException | EOFException e) {
						LOGGER.error("Disconnect requested! Stopping TCP channel of client: {}", Thread.currentThread().getName());
					} catch (ClassNotFoundException e) {
						LOGGER.error("Stopping TCP channel due to ClassNotFoundException!");
					} catch (IOException e) {
						LOGGER.error("Stopping TCP channel due to exception: ");
					}
				});

				clientThread.start();
			}
		} catch (IOException e) {
			LOGGER.error("Stopping server due to unknown exception: {}", e.getMessage());
		} finally {
			udpSockets.clear();
			tcpSocket.close();
			udpSocket.close();
		}
	}
}