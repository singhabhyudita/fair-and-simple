package util;

import entity.Message;

import java.io.IOException;
import java.io.ObjectInputStream;

public class ChatUtil implements Runnable {


    ObjectInputStream ois;

    public ChatUtil(ObjectInputStream ois) {
        System.out.println("constructor called");
        this.ois=ois;
        System.out.println("ois assigned");
    }

    @Override
    public void run() {
        Message message = null;
        System.out.println(Thread.currentThread());
        while (true){
            System.out.println("inside socket is connected loop");
            try {
                System.out.println("waiting for message object");
                message= (Message)ois.readObject();
                System.out.println("response sent on chat thread");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            assert message != null;
            System.out.println("Message received from sender id "+ message.getSenderID()+": ");
            System.out.println(message.getText());
            //TODO: display the messages
        }
    }
}
