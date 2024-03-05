import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class JavaTcpServer {
    private static ServerSocket tcpSocket;
    private static ArrayList<Integer> udpSockets;
    private static ArrayList<ObjectOutputStream> tcpPrintWriters;
    private static DatagramSocket socket;

    private static void initializeServer() throws IOException {
        System.out.println("Initializing Server...");
        tcpPrintWriters = new ArrayList<>();
        tcpSocket = new ServerSocket(CommonConfig.port);
        udpSockets = new ArrayList<>();
    }

    private static void sendUdpMessage(Message message, InetAddress address, Integer port) {
        try {
            byte [] msg = message.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(msg, msg.length, address, port);
            socket.send(sendPacket);
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

        initializeServer();
        socket = new DatagramSocket(CommonConfig.port);
        System.out.println("Server starts listening on: " + CommonConfig.port);

        try {
            while(true){
                Socket clientSocket = tcpSocket.accept();
                System.out.println("New TCP connection from: " + clientSocket.getInetAddress());

                Thread udpConnectionHandler = new Thread(() -> {
                    try {
                        System.out.println("Starting UDP chanel");
                        byte[] receiveBuffer = new byte[4096];

                        while (true) {
                            Arrays.fill(receiveBuffer, (byte) 0);
                            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                            socket.receive(receivePacket);

                            ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData()));
                            Message message = (Message) iStream.readObject();
                            iStream.close();

                            if(message.type == MessageType.CONNECT || ! udpSockets.contains(receivePacket.getPort())){
                                udpSockets.add(receivePacket.getPort());
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
                        socket.close();
                    }
                });

                udpConnectionHandler.start();

                Thread clientThread = new Thread(() -> {
                    try {
                        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                        tcpPrintWriters.add(out);
                        out.writeObject(new Message(MessageType.COMMUNICATION, "No. users currently at chat: [" + tcpPrintWriters.size() + "]", ""));

                        while(true){
                            Message message = (Message) in.readObject();
                            System.out.println("TCP channel received message: type=" + message.type + " content: \"" + message.message +"\" from " + message.username);

                            if(message.type == MessageType.DISCONNECT){
                                tcpPrintWriters.remove(out);
                                message.type = MessageType.COMMUNICATION;
                                message.message = "User: " + message.username + " has left the chat!";
                            }

                            tcpPrintWriters.stream()
                                .filter(x -> x != out)
                                .forEach(entry -> {sendTcpMessage(entry, message);});
                        }

                    } catch (IOException e) {
                        System.out.println("Stopping TCP channel due to exception: " + e.getMessage());
                    } catch (ClassNotFoundException e) {
                        System.out.println("Stopping TCP channel due to ClassNotFoundException: " + e.getMessage());
                    }
                });

                clientThread.start();
            }
        } catch (IOException e) {
            System.out.println("IOException!");
        } finally {
            udpSockets.clear();
        }
    }
}