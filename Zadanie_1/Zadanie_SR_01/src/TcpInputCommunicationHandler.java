import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class TcpInputCommunicationHandler extends Thread{

    final private ObjectInputStream in;

    TcpInputCommunicationHandler(Socket socket) throws IOException {
        this.in = new ObjectInputStream(socket.getInputStream());
    }


    @SuppressWarnings("InfiniteLoopStatement")
    private void handleIncomingMessages() throws ClassNotFoundException, IOException {
       try {
           Message recv;
           while (true){
               recv = (Message) in.readObject();

               switch (recv.type){
                   case MESSAGE: {
                       System.out.println(recv.username + ":" + recv.message);
                       break;
                   }
                   case COMMUNICATION: {
                        System.out.println(recv.message);
                        break;
                   }
                   default: {
                        System.out.println("Unknown command!");
                        break;
                   }
               }

               System.out.print(">: ");
           }
       } catch (IOException ignored){
       }
    }

    @Override
    public void run() throws RuntimeException {
        try {
            this.handleIncomingMessages();
        } catch (IOException | ClassNotFoundException ignored) {
        }
    }
}
