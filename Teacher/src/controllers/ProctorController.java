package controllers;

import entity.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import main.UdpUtil;

import java.awt.image.BufferedImage;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.*;

public class ProctorController {
    @FXML
    public GridPane studentVideoGridPane;
    @FXML
    public ListView<Student> allStudentsListView;
    @FXML
    public ImageView topLeftImageView;
    @FXML
    public ImageView topRightImageView;
    @FXML
    public ImageView bottomLeftImageView;
    @FXML
    public ImageView bottomRightImageView;

    public List<ImageView> imageViewHolder;
    private DatagramSocket getVideoFeedSocket = null;
    private DatagramPacket receivedSnapshot = null;
    private Queue<Integer> studentsOnDisplay = null;
    private Map<Integer, ImageView> registrationNumberImageWindowMap = null;

    // 3  4  5  1
    // tl tr bl br

    public void callFirst(DatagramSocket videoFeedSocket, List<Student> students) {

        getVideoFeedSocket = videoFeedSocket;

        imageViewHolder = new ArrayList<>(Arrays.asList(topLeftImageView, topRightImageView, bottomLeftImageView, bottomRightImageView));

        allStudentsListView.setCellFactory(param -> new ListCell<Student>() {
            @Override
            protected void updateItem(Student item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) setText(null);
                else setText(item.getName() + " - " + item.getRegistrationNumber());
            }
        });
        studentsOnDisplay = new LinkedList<>();
        registrationNumberImageWindowMap = new HashMap<>();
        allStudentsListView.setItems(FXCollections.observableList(students));
        allStudentsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2) {
                    System.out.println("We want to see the video of student = " + allStudentsListView.getSelectionModel().getSelectedItem().getRegistrationNumber());
                    if(!studentsOnDisplay.contains(allStudentsListView.getSelectionModel().getSelectedItem().getRegistrationNumber())) {
                        if(studentsOnDisplay.size() == 4) {
                            System.out.println("Replace");
                            replaceStudent(allStudentsListView.getSelectionModel().getSelectedItem().getRegistrationNumber(),
                                    studentsOnDisplay.poll());
                        } else {
                            System.out.println("Add");
                            registrationNumberImageWindowMap.put(
                                    allStudentsListView.getSelectionModel().getSelectedItem().getRegistrationNumber(),
                                    imageViewHolder.get(studentsOnDisplay.size()));
                            studentsOnDisplay.add(allStudentsListView.getSelectionModel().getSelectedItem().getRegistrationNumber());
                        }
                    }
                }
            }

            private void replaceStudent(Integer incoming, Integer outgoing) {
                studentsOnDisplay.remove();
                studentsOnDisplay.add(incoming);
                ImageView imageView = registrationNumberImageWindowMap.get(outgoing);
                registrationNumberImageWindowMap.remove(outgoing);
                registrationNumberImageWindowMap.put(incoming, imageView);
            }
        });

        Thread readVideoDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
//                    System.out.println("Waiting for video");
                    Object[] data = (Object[]) UdpUtil.getObjectFromPort(videoFeedSocket);
                    String registrationNumber = (String) UdpUtil.byteArrayToObject((byte[]) data[0]);
                    System.out.println("Got video of registraion no = " + registrationNumber);
                    BufferedImage image = UdpUtil.byteArrayToBufferedImage((byte[]) data[1]);
                    if(image != null && registrationNumber != null) {
                        if(studentsOnDisplay.contains(Integer.valueOf(registrationNumber))) {
                            ImageView view = registrationNumberImageWindowMap.get(Integer.valueOf(registrationNumber));
                            view.setImage(SwingFXUtils.toFXImage(image, null));
                            view.setPreserveRatio(true);
                        }
                    }
                }
            }
        });
        readVideoDataThread.setDaemon(true);
        readVideoDataThread.start();
    }
}
