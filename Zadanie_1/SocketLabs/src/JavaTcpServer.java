import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

/*
    Ports for Server-Client communication are defined in CommonConfig Class
    Server is listening on port and when new connection occur, new thread is created
    to handle message exchange.

    Multicast communication is created with MulticastSocket and MulticastSocket.
    Server does not take part in Multicast Communication (Clients only).

    Server has one UDP Channel that is gathering all Datagram Packets
    Number of Client's TCP thread's is not limited!

    Server spreads information to all TCP Client's via Array of PrintWriters
    (ObjectOutputStream)

    Messages are defined as follows:
        * Type : MessageType
        * Username: String
        * Message: String
*/

public class JavaTcpServer {
    private static ServerSocket tcpSocket;
    private static ArrayList<Integer> udpSockets;
    private static ArrayList<ObjectOutputStream> tcpPrintWriters;
    private static DatagramSocket udpSocket;

    private static void sendUdpMessage(Message message, InetAddress address, Integer port) {
        try {
            byte [] msg = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, address, port);
            udpSocket.send(sendPacket);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private static void sendTcpMessage(ObjectOutputStream OOStream, Message message){
        try {
            OOStream.writeObject(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {

        System.out.println("Initializing Server...");
        tcpPrintWriters = new ArrayList<>();
        udpSockets = new ArrayList<>();

        // Starting up sockets
        tcpSocket = new ServerSocket(CommonConfig.port);
        udpSocket = new DatagramSocket(CommonConfig.port);
        System.out.println("Server starts listening on port: " + CommonConfig.port);

        // Starting UDP Connection
        Thread udpConnectionHandler = new Thread(() -> {
            try {
                byte[] receiveBuffer = new byte[4096];

                while (!Thread.currentThread().isInterrupted()) {
                    Arrays.fill(receiveBuffer, (byte) 0);
                    DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                    udpSocket.receive(receivePacket);

                    // New ObjectInputStream due to loop receiving old message
                    ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData()));
                    Message message = (Message) iStream.readObject();
                    iStream.close();

                    // Do not spread Connect Message - Add to UDP Sockets if not there
                    if(message.type == MessageType.CONNECT || ! udpSockets.contains(receivePacket.getPort())){
                        udpSockets.add(receivePacket.getPort());
                        continue;
                    }

                    // Remove port if not necessary
                    if(message.type == MessageType.DISCONNECT && udpSockets.contains(receivePacket.getPort())){
                        udpSockets.remove((Object)receivePacket.getPort());
                        continue;
                    }

                    System.out.println("UDP channel received message: type=" + message.type + " content: \"" + "[ASCII ART MESSAGE]" +"\" from " + message.username);

                    udpSockets.stream()
                            .filter(x -> x != receivePacket.getPort())
                            .forEach(entry -> {sendUdpMessage(message, receivePacket.getAddress(), entry);});

                }

            } catch (IOException e) {
                System.out.println("Stopping UDP channel due to exception: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("Stopping UDP channel due to ClassNotFoundException: " + e.getMessage());
            } finally {
                System.out.println("Stopping UDP channel");
                udpSocket.close();
            }
        });

        udpConnectionHandler.start();

        try {
            // Main loop
            while(true){
                Socket clientSocket = tcpSocket.accept();
                System.out.println("New TCP connection from: " + clientSocket.getInetAddress());

                Thread clientThread = new Thread(() -> {
                    try {
                        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                        tcpPrintWriters.add(out);
                        out.writeObject(new Message(MessageType.COMMUNICATION, "No. users currently at chat: [" + tcpPrintWriters.size() + "]", ""));

                        while(!clientSocket.isClosed()){
                            Message message = (Message) in.readObject();
                            System.out.println("TCP channel received message: type=" + message.type + " content: \"" + message.message +"\" from " + message.username);

                            if(message.type == MessageType.DISCONNECT){
                                tcpPrintWriters.remove(out);
                                message.type = MessageType.COMMUNICATION;
                                message.message = "User: " + message.username + " has left the chat!";

                                tcpPrintWriters.stream()
                                        .filter(x -> x != out)
                                        .forEach(entry -> {sendTcpMessage(entry, message);});

                                Thread.currentThread().interrupt();
                                return;
                            }

                            tcpPrintWriters.stream()
                                .filter(x -> x != out)
                                .forEach(entry -> {sendTcpMessage(entry, message);});
                        }

                    } catch (InterruptedIOException | EOFException e) {
                        System.out.println("Disconnect requested! Stopping TCP channel of client: " + Thread.currentThread().getName());
                    } catch (ClassNotFoundException e) {
                        System.out.println("Stopping TCP channel due to ClassNotFoundException!");
                    } catch (IOException e) {
                        System.out.println("Stopping TCP channel due to exception: ");
                    }
                });

                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("IOException!");
        } finally {
            udpSockets.clear();
            tcpSocket.close();
            udpSocket.close();
        }
    }
}