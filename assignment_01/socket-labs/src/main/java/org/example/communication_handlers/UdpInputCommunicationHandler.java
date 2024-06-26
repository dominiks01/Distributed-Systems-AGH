package org.example.communication_handlers;

import org.example.common.MessageExchange;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class UdpInputCommunicationHandler extends Thread{

    final private DatagramSocket socket;
    private final byte[] receiveBuffer = new byte[4096];


    public UdpInputCommunicationHandler(DatagramSocket socket) throws IOException {
        this.socket = socket;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void incomingMessagesHandler() throws IOException, ClassNotFoundException{
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        ObjectInputStream iStream;

        while (true) {
            socket.receive(receivePacket);
            iStream = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData()));
            MessageExchange messageClass = (MessageExchange) iStream.readObject();
            iStream.close();

            System.out.println(messageClass.username + ": sent message");
            System.out.println(messageClass.message);
            System.out.print(">: ");
        }
    }

    @Override
    public void run() throws RuntimeException {
        try {
            this.incomingMessagesHandler();

        } catch (SocketException | ClassNotFoundException e) {
            System.out.println("Socket critical exception");
        } catch (IOException e){
            System.out.println("Unknown exception");
        }
    }
}
