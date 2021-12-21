package response;

import entity.Message;

import java.util.ArrayList;

public class DisplayMessagesResponse extends Response {
    private ArrayList<Message>senderMessages,otherMessages;

    public DisplayMessagesResponse(ArrayList<Message> senderMessages, ArrayList<Message> otherMessages) {
        this.senderMessages = senderMessages;
        this.otherMessages = otherMessages;
    }

    public ArrayList<Message> getSenderMessages() {
        return senderMessages;
    }

    public ArrayList<Message> getOtherMessages() {
        return otherMessages;
    }
}
