package response;

import entity.Message;
import response.Response;

import java.util.ArrayList;

public class DisplayMessagesResponse extends Response {
    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    private ArrayList<Message> messages;

    public DisplayMessagesResponse(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
