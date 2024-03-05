import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpInputCommunicationHandler extends Thread{

    final private DatagramSocket socket;
    private final byte[] receiveBuffer = new byte[4096];


    UdpInputCommunicationHandler(DatagramSocket socket) throws IOException {
        this.socket = socket;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void incomingMessagesHandler() throws IOException, ClassNotFoundException{
        DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
        ObjectInputStream iStream;

        while (true) {
            socket.receive(receivePacket);
            iStream = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData()));
            Message messageClass = (Message) iStream.readObject();
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
