package org.example.common;

import java.io.*;
import java.util.HashMap;

public class MessageExchange implements Serializable {
    public MessageType type;
    public String message;
    public String username;

    public MessageExchange(MessageType type, String message, String username){
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

