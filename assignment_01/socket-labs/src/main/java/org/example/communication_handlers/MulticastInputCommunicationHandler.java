package org.example.communication_handlers;

import org.example.common.MessageExchange;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MulticastInputCommunicationHandler extends Thread {

	final private MulticastSocket socket;
	private final byte[] receiveBuffer = new byte[4096];

	public MulticastInputCommunicationHandler(MulticastSocket socket) {
		this.socket = socket;
	}

	@SuppressWarnings("InfiniteLoopStatement")
	private void incomingMessagesHandler() throws IOException, ClassNotFoundException {
		DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
		ObjectInputStream iStream;

		while (true) {
			socket.receive(receivePacket);
			iStream = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData()));
			MessageExchange messageClass = (MessageExchange) iStream.readObject();
			iStream.close();

			System.out.println(messageClass.message);
			System.out.print(">: ");
		}
	}

	@Override
	public void run() throws RuntimeException {
		try {
			this.incomingMessagesHandler();

		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
