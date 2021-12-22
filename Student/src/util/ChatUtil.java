package util;

import controller.SingleChatCardFXMLController;
import entity.Main;
import entity.Message;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.controlsfx.control.Notifications;

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
        Message message2 = null;
        System.out.println(Thread.currentThread());
        while (true){
            System.out.println("inside socket is connected loop");
            try {
                System.out.println("waiting for message object");
                message2= (Message)ois.readObject();
                System.out.println("response sent on chat thread");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            final Message message = message2;
            System.out.println("Message received from sender id "+ message.getSenderID()+": "+message.getText());

//            if(Main.chatVBox != null)
            if(Main.chatVBox != null){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Notifications.create()
                                .title("Message from " + message.getSenderName() + " in " + message.getCourseName())
                                .text(message.getText())
                                .show();
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/SingleChatCardFXML.fxml"));
                        try {
                            Node node = fxmlLoader.load();
                            SingleChatCardFXMLController singleChatCardFXMLController = fxmlLoader.getController();
                            singleChatCardFXMLController.messageLabel.setText(message.getText());
                            singleChatCardFXMLController.nameLabel.setText(message.getSenderName());
                            singleChatCardFXMLController.timestampLabel.setText(message.getSentAt().toString());
                            singleChatCardFXMLController.nameHBox.backgroundProperty().set(new Background(new BackgroundFill(Color.web("#bee2f7"),
                                    CornerRadii.EMPTY,
                                    Insets.EMPTY)));
                            Main.chatVBox.getChildren().add(node);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }
}
