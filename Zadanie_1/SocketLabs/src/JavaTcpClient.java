import javax.swing.*;
import java.io.*;
import java.net.*;

public class JavaTcpClient {
    private final static BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
    private static String username;
    private static Socket tcpSocket;
    private static DatagramSocket udpSocket;
    private static MulticastSocket multicastSocket;

    public static final String RED = "\033[0;31m";     // RED
    public static final String RESET = "\033[0m";  // Text Reset

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {

        System.out.print("Please, enter your username: ");
        username = inputReader.readLine();

        if(username == null)
            System.exit(2);

        try {
            tcpSocket = new Socket(CommonConfig.host, CommonConfig.port);
            udpSocket = new DatagramSocket();
            multicastSocket = new MulticastSocket(CommonConfig.multicastPort);
        } catch (ConnectException e){
            System.out.println("Client could not connect to server! Check your configuration");
            System.exit(2);
        }

        InetAddress address = InetAddress.getByName(CommonConfig.host);
        InetAddress multicastAddress = InetAddress.getByName(CommonConfig.multicastAddress);
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(multicastAddress);

        multicastSocket.joinGroup(new InetSocketAddress(multicastAddress, CommonConfig.port), networkInterface);

        Thread tcpIncomingMessagesHandler = new TcpInputCommunicationHandler(tcpSocket);
        Thread udpIncomingMessagesHandler = new UdpInputCommunicationHandler(udpSocket);
        Thread multicastMessagesHandler = new MulticastInputCommunicationHandler(multicastSocket);

        ObjectOutputStream out = (new ObjectOutputStream(tcpSocket.getOutputStream()));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Message newMessage = new Message(MessageType.DISCONNECT, "Disconnecting...", username);
            try {
                tcpIncomingMessagesHandler.interrupt();
                udpIncomingMessagesHandler.interrupt();
                multicastMessagesHandler.interrupt();

                out.writeObject(newMessage);
                udpSocket.send(new DatagramPacket(newMessage.getBytes(), newMessage.getBytes().length, address, CommonConfig.port));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));

        try {
            tcpIncomingMessagesHandler.start();
            udpIncomingMessagesHandler.start();
            multicastMessagesHandler.start();

            Message oneTimeMessage = new Message(MessageType.COMMUNICATION, username + " has joined the chat!", username);
            out.writeObject(oneTimeMessage);

            DatagramPacket sendPacket = new DatagramPacket(oneTimeMessage.getBytes(), oneTimeMessage.getBytes().length, address, CommonConfig.port);
            udpSocket.send(sendPacket);

            while (true) {
                Message newMessage = new Message(MessageType.MESSAGE, inputReader.readLine(), username);


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
                               """;

                        sendPacket = new DatagramPacket(newMessage.getBytes(), newMessage.getBytes().length, address, CommonConfig.port);
                        udpSocket.send(sendPacket);
                        System.out.print(">: ");
                        break;
                    }

                    case "M": {
                        newMessage.message = "Multicast Message!";
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
            System.out.print(RED + "Disconnecting client from chat due to interruption!" + RESET);
            System.exit(2);
        } finally {
            if(!tcpSocket.isClosed())
                tcpSocket.close();

            if(!udpSocket.isClosed())
                udpSocket.close();

            if(!multicastSocket.isClosed())
                multicastSocket.close();
        }
    }
}
