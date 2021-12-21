package requestHandler;

import entity.Message;
import main.RequestIdentifier;
import main.Server;
import response.SendMessageResponse;
import table.MessageTable;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class SendMessageRequestHandler extends RequestHandler {
    private Connection connection;
    private ObjectOutputStream oos;
    private Message message;

    public SendMessageRequestHandler(Connection connection, ObjectOutputStream oos, Message message) {
        this.connection = connection;
        this.oos = oos;
        this.message = message;
    }

    @Override
    public void sendResponse() {
        PreparedStatement preparedStatement;
        InputStream is=null;
        BufferedImage bufferedImage;
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        try {
            preparedStatement=connection.prepareStatement(MessageTable.ADD_MESSAGE_QUERY);
            preparedStatement.setString(1, RequestIdentifier.userID);
            preparedStatement.setString(2,message.getCourseID());
            preparedStatement.setString(3,message.getText());

//            bufferedImage=  ((ToolkitImage)message.getImage().getImage()).getBufferedImage();
//            ImageIO.write(bufferedImage,"jpg",byteArrayOutputStream);
//            is=new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

            preparedStatement.setBlob(4,is);
            preparedStatement.setTimestamp(5,message.getSentAt());
            preparedStatement.setBoolean(6,message.getStudent());

            int response=preparedStatement.executeUpdate();

            if(response==0)oos.writeObject(new SendMessageResponse("Failed"));
            else oos.writeObject(new SendMessageResponse("Successful"));

            oos.flush();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    public void sendToAll(){
        ArrayList<ObjectOutputStream>socketArrayList= Server.socketArrayList;
        ObjectOutputStream objectOutputStream;
        System.out.println("inside send to all");
        for (ObjectOutputStream oos:socketArrayList) {
            System.out.println("Caht oos  here:");
            System.out.println(oos.toString());
            try {
                //if(s.getOutputStream().equals(oos))continue;
                oos.writeObject(message);
                System.out.println("message object sent");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
