import java.io.Console;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class TcpInputCommunicationHandler extends Thread{

    final private ObjectInputStream in;
    public static final String RED = "\033[0;31m";     // RED
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String BLUE = "\033[0;34m";    // BLUE

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
       } catch (IOException ignored){
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
