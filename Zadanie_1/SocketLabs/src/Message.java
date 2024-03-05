import netscape.javascript.JSObject;

import java.io.*;
import java.util.HashMap;

public class Message implements Serializable {
    MessageType type = null;
    String message = null;
    String username = null;

    Message(MessageType type, String message, String username){
        this.type = type;
        this.message = message;
        this.username = username;
    }

    public HashMap<String, String> getData(){
        return new HashMap<>(){{
            put("type", type.name());
            put("message", String.valueOf(message));
            put("username", String.valueOf(username));
        }};
    };

    @Override
    public String toString(){
        return this.getData().toString();
    }

    public byte[] getBytes() throws IOException {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutput oo = new ObjectOutputStream(bStream);
        oo.writeObject(this);
        oo.close();

        return bStream.toByteArray();
    }

}

