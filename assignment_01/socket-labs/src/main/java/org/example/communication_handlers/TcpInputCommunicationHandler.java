package org.example.communication_handlers;

import org.example.common.MessageExchange;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import static org.example.common.Colors.RESET;
import static org.example.common.Colors.RED;
import static org.example.common.Colors.GREEN;
import static org.example.common.Colors.BLUE;

public class TcpInputCommunicationHandler extends Thread {

	final private ObjectInputStream in;

	public TcpInputCommunicationHandler(Socket socket) throws IOException {
		this.in = new ObjectInputStream(socket.getInputStream());
	}


	@SuppressWarnings("InfiniteLoopStatement")
	private void handleIncomingMessages() throws ClassNotFoundException, IOException {
		try {
			MessageExchange recv;
			while (true) {
				recv = (MessageExchange) in.readObject();

				switch (recv.type) {
					case MESSAGE: {
						System.out.println(RED + recv.username + RESET + ": " + GREEN + recv.message + RESET);
						break;
					}
					case COMMUNICATION: {
						System.out.println(BLUE + recv.message + RESET);
						break;
					}
					default: {
						System.out.println("Unknown command!");
						break;
					}
				}

				System.out.print(">: ");
			}
		} catch (IOException ignored) {
		}
	}

	@Override
	public void run() {
		try {
			this.handleIncomingMessages();
		} catch (IOException | ClassNotFoundException ignored) {
		}
	}
}
