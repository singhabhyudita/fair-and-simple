package util;

import controller.SingleChatCardFXMLController;
import controller.SingleImageChatCardFXMLController;
import entity.Main;
import entity.Message;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import org.controlsfx.control.Notifications;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Objects;

import entity.Message;
import sun.awt.image.ToolkitImage;

import javax.swing.*;

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


            if(Main.chatVBox == null || !message.getCourseID().equals(Main.lastOpenCourseId)) {
                if(!Objects.equals(message.getSenderID(), Main.userRegistrationNumber)) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Notifications.create()
                                    .title("Message from " + message.getSenderName() + " in " + message.getCourseName())
                                    .text(message.getText())
                                    .show();
                        }
                    });
                }
            }

//            if(Main.chatVBox != null)
            if(Main.chatVBox != null && message.getCourseID().equals(Main.lastOpenCourseId)){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if(message.getImage() != null) {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/SingleImageChatCardFXML.fxml"));
                            try {
                                Node node = fxmlLoader.load();
                                SingleImageChatCardFXMLController singleImageChatCardFXMLController = fxmlLoader.getController();
                                BufferedImage bufferedImage=  ((ToolkitImage)message.getImage().getImage()).getBufferedImage();
                                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                                singleImageChatCardFXMLController.imageView.setImage(image);
                                singleImageChatCardFXMLController.vBox.setAlignment(message.getSenderID().equals(Main.userRegistrationNumber) ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
                                singleImageChatCardFXMLController.nameLabel.setText(message.getSenderID().equals(Main.userRegistrationNumber)?"Me":message.getSenderName());
                                singleImageChatCardFXMLController.timestampLabel.setText(message.getSentAt().toString());
                                singleImageChatCardFXMLController.nameHBox.backgroundProperty().set(new Background(new BackgroundFill(Color.web(
                                        (message.getSenderID().equals(Main.userRegistrationNumber)) ? Main.myColor : Main.otherColor),
                                        CornerRadii.EMPTY,
                                        Insets.EMPTY)));
                                Main.chatVBox.getChildren().add(node);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/SingleChatCardFXML.fxml"));
                            try {
                                Node node = fxmlLoader.load();
                                SingleChatCardFXMLController singleChatCardFXMLController = fxmlLoader.getController();
                                singleChatCardFXMLController.messageLabel.setText(message.getText());
                                singleChatCardFXMLController.messageLabel.setAlignment(message.getSenderID().equals(Main.userRegistrationNumber) ? Pos.TOP_RIGHT : Pos.TOP_LEFT);
                                singleChatCardFXMLController.nameLabel.setText(message.getSenderID().equals(Main.userRegistrationNumber)?"Me":message.getSenderName());
                                singleChatCardFXMLController.timestampLabel.setText(message.getSentAt().toString());
                                singleChatCardFXMLController.nameHBox.backgroundProperty().set(new Background(new BackgroundFill(Color.web(
                                        (message.getSenderID().equals(Main.userRegistrationNumber)) ? Main.myColor : Main.otherColor),
                                        CornerRadii.EMPTY,
                                        Insets.EMPTY)));
                                Main.chatVBox.getChildren().add(node);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }
    }
}
