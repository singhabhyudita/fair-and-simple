package controller;

import com.github.sarxos.webcam.Webcam;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import entity.*;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import request.GetQuestionsRequest;
import request.SubmitExamRequest;
import response.GetQuestionsResponse;
import response.SubmitExamResponse;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class QuestionsScreenController implements Initializable {

    private class QuestionsObservable {
        Property<String> question = new SimpleStringProperty();
        Property<String> optionA = new SimpleStringProperty();
        Property<String> optionB = new SimpleStringProperty();
        Property<String> optionC = new SimpleStringProperty();
        Property<String> optionD = new SimpleStringProperty();
        Property<String> answer = new SimpleStringProperty();
        boolean isObjective = false;

        public void setQuestion(Question question) {
            this.question.setValue(question.getQuestion());
            if(question.isObjective()) {
                this.optionA.setValue(question.getOptionA());
                this.optionB.setValue(question.getOptionB());
                this.optionC.setValue(question.getOptionC());
                this.optionD.setValue(question.getOptionD());
                this.answer.setValue(((Integer)question.getCorrectOption()).toString());
                isObjective = true;
            }
        }

        public boolean isObjective() {
            return isObjective;
        }

    }

    //    FXML FIELDS
    @FXML
    private FlowPane progressPane;
    @FXML
    private Label title;
    @FXML
    private Label time;
    @FXML
    private Label question;
    @FXML
    private JFXRadioButton optionA;
    @FXML
    private JFXRadioButton optionB;
    @FXML
    private JFXRadioButton optionC;
    @FXML
    private JFXRadioButton optionD;
    @FXML
    private ToggleGroup options;
    @FXML
    private JFXButton next;
    @FXML
    private JFXButton submit;
    @FXML
    public VBox subjectiveVBox;
    @FXML
    public JFXTextArea subjectiveAnswerTextArea;
    @FXML
    public VBox objectiveVBox;

    private Webcam webcam;
    private DatagramSocket sendVideoSocket;

    private Exam exam;
    private List<Question> questionList;
    private Question currentQuestion;
    int currentIndex = 0;
    private QuestionsObservable questionsObservable;
    private Map<Question, String> studentAnswers = new HashMap<>();
    private Integer numberOfRightAnswers = 0;
    private Student student;
    private List<Question> objectiveAnswers;
    private File answerFile;
    private FileWriter writer;
    private BufferedWriter bufferedWriter;
    private Thread videoThread;

    //    timer fields
    private long min, sec, hr, totalSec = 0; //250 4 min 10 sec
    private Timer timer;

    //    METHODS AND CONSTRUCTOR
    public void setQuiz(Exam exam) {
        this.exam = exam;
        this.title.setText(this.exam.getTitle());
    }

    private String format(long value) {
        if (value < 10) {
            return 0 + "" + value;
        }
        return value + "";
    }

    public void convertTime() {
        min = TimeUnit.SECONDS.toMinutes(totalSec);
        sec = totalSec - (min * 60);
        hr = TimeUnit.MINUTES.toHours(min);
        min = min - (hr * 60);
        time.setText(format(hr) + ":" + format(min) + ":" + format(sec));
        totalSec--;
    }

    private void setTimer() {
        totalSec = (this.exam.getEndTime().getTime() - Math.max((new Date()).getTime(),this.exam.getDate().getTime()))/1000;
        this.timer = new Timer();

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        convertTime();
                        if (totalSec <= 0) {
                            timer.cancel();
                            time.setText("00:00:00");
                            submit(null);
                            Notifications.create()
                                    .title("Error")
                                    .text("TIme Up")
                                    .position(Pos.BOTTOM_RIGHT)
                                    .showError();
                        }
                    }
                });
            }
        };

        timer.schedule(timerTask, 0, 1000);
    }

    public void setData(int proctorPort, List<Question> questions, String examId) {
        Thread videoThread = setupProctoringStuff(proctorPort);
        while(videoThread == null) {
            GuiUtil.alert(Alert.AlertType.ERROR, "Could not access you camera. Close all other applications and try again!!");
            videoThread = setupProctoringStuff(proctorPort);
        }
        this.videoThread = videoThread;
        this.questionList = questions;
        objectiveAnswers = new ArrayList<>();
        answerFile = new File(Main.userRegistrationNumber + "_" + examId + "_subjective_answer.txt");
        try {
            writer = new FileWriter(answerFile);
            bufferedWriter = new BufferedWriter(writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.shuffle(this.questionList);
        renderProgress();
        setNextQuestion();
        setTimer();
    }

    private Thread setupProctoringStuff(int proctorPort) {
        for(Webcam w : Webcam.getWebcams()) {
            try {
                webcam = Webcam.getWebcamByName(w.getName());
                webcam.open();
                break;
            } catch (Exception ignored) {}
        }
        if(webcam == null)
            return null;
        try {
            this.sendVideoSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Thread videoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramPacket packet;
                while(!Thread.interrupted()) {
                    try {
                        Thread.sleep(500);
                        BufferedImage image = webcam.getImage();
                        String registrationNumber = Main.userRegistrationNumber;
                        byte [] imageByte = UdpUtil.bufferedImageToByteArray(image);
                        byte [] registrationNumberByte = UdpUtil.objectToByteArray(registrationNumber);
                        System.out.println("image being sent by Registraiton number = " + registrationNumber);
                        Object [] wrapped = {registrationNumberByte, imageByte};
                        UdpUtil.sendObjectToPort(wrapped, proctorPort);
                    } catch (InterruptedException ignored) {}
                }
            }
        });
        videoThread.setDaemon(true);
        videoThread.start();
        return videoThread;
    }

    private void renderProgress() {
        for (int i = 0; i < this.questionList.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/ProgressCircleFXML.fxml"));
            try {
                Node node = fxmlLoader.load();
                ProgressCircleFXMLController progressCircleFXMLController = fxmlLoader.getController();
                progressCircleFXMLController.setNumber(i + 1);
                progressCircleFXMLController.setDefaultColor();
                progressPane.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.showNextQuestionButton();
        this.hideSubmitQuizButton();
        this.questionsObservable = new QuestionsObservable();
        bindFields();
    }

    private void bindFields() {
        this.question.textProperty().bind(this.questionsObservable.question);
        this.optionD.textProperty().bind(this.questionsObservable.optionD);
        this.optionC.textProperty().bind(this.questionsObservable.optionC);
        this.optionB.textProperty().bind(this.questionsObservable.optionB);
        this.optionA.textProperty().bind(this.questionsObservable.optionA);
    }

    public Integer getSelectedOptionIndex() {
        if(this.optionA.isSelected()) {
            return 1;
        } else if(this.optionB.isSelected()) {
            return 2;
        } else if(this.optionC.isSelected()) {
            return 3;
        } else if(this.optionD.isSelected()) {
            return 4;
        }
        return 0;
    }

    public void nextQuestions(ActionEvent actionEvent) {
        if(currentQuestion.isObjective()) {
            objectiveAnswers.add(new Question(
                    currentQuestion.getQuestionId(),
                    currentQuestion.getQuestion(),
                    currentQuestion.getOptionA(),
                    currentQuestion.getOptionB(),
                    currentQuestion.getOptionC(),
                    currentQuestion.getOptionD(),
                    getSelectedOptionIndex(),
                    true));
        } else {
            writeToFile(currentQuestion.getQuestionId() + " ===> " + subjectiveAnswerTextArea.getText());
        }
        this.setNextQuestion();
    }

    private void setNextQuestion() {
        if (!(currentIndex >= questionList.size())) {
            {
                Node circleNode = this.progressPane.getChildren().get(currentIndex);
                ProgressCircleFXMLController controller = (ProgressCircleFXMLController) circleNode.getUserData();
                controller.setCurrentQuestionColor();
            }
            this.currentQuestion = this.questionList.get(currentIndex);
            this.questionsObservable.setQuestion(this.currentQuestion);
            if(!this.currentQuestion.isObjective()) {
                subjectiveVBox.setVisible(true);
                subjectiveAnswerTextArea.setVisible(true);
                objectiveVBox.setVisible(false);
            } else {
                subjectiveVBox.setVisible(false);
                subjectiveAnswerTextArea.setVisible(false);
                objectiveVBox.setVisible(true);
            }
            currentIndex++;
        } else {
            hideNextQuestionButton();
            showSubmitQuizButton();
        }
    }

    private void hideNextQuestionButton() {
        this.next.setVisible(false);
    }

    private void showNextQuestionButton() {
        this.next.setVisible(true);
    }

    private void hideSubmitQuizButton() {
        this.submit.setVisible(false);
    }

    private void showSubmitQuizButton() {
        this.submit.setVisible(true);
    }

    public void submit(ActionEvent actionEvent) {
        SubmitExamRequest submitExamRequest = new SubmitExamRequest(exam, objectiveAnswers);
        Main.sendRequest(submitExamRequest);
        SubmitExamResponse response = (SubmitExamResponse) Main.getResponse();
        if(response.isSendFileStream()) {
            try {
                byte [] answerByte = Files.readAllBytes(answerFile.toPath());
                Main.oos.writeObject(answerByte);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            GuiUtil.alert(Alert.AlertType.ERROR, "Kuch locha ho gaya. Fail tum.");
        }
        videoThread.interrupt();
        FXMLLoader homepageLoader= new FXMLLoader(getClass().getResource("../fxml/ProfileScreen.fxml"));
        Stage currentStage=(Stage)submit.getScene().getWindow();
        Scene scene=null;
        try {
            scene=new Scene(homepageLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentStage.setScene(scene);
        currentStage.setTitle("Welcome");
        ProfileScreenController profileScreenController=homepageLoader.getController();
        profileScreenController.first(Main.userFullName);
    }

    private void writeToFile(String answer) {
        try {
            bufferedWriter.write(answer);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
