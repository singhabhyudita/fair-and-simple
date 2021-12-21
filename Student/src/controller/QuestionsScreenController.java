package controller;

import com.github.sarxos.webcam.Webcam;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
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
import javafx.stage.Stage;
import org.controlsfx.control.Notifications;
import request.GetQuestionsRequest;
import request.SubmitExamRequest;
import response.GetQuestionsResponse;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URL;
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

        public void setQuestion(Question question) {
            this.question.setValue(question.getQuestion());
            this.optionA.setValue(question.getOptionA());
            this.optionB.setValue(question.getOptionB());
            this.optionC.setValue(question.getOptionC());
            this.optionD.setValue(question.getOptionD());
            this.answer.setValue(((Integer)question.getCorrectOption()).toString());
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

    private Webcam webcam;
    private DatagramSocket sendVideoSocket;

    //    listeners


    //NON FXML FIELDS
    private Exam exam;
    private List<Question> questionList;
    private Question currentQuestion;
    int currentIndex = 0;
    private QuestionsObservable questionsObservable;
    private Map<Question, String> studentAnswers = new HashMap<>();
    private Integer numberOfRightAnswers = 0;
    private Student student;

    public void setStudent(Student student) {
        this.student = student;
    }

    //    timer fields
    private long min, sec, hr, totalSec = 0; //250 4 min 10 sec
    private Timer timer;


    //    METHODS AND CONSTRUCTOR
    public void setQuiz(Exam exam) {
        this.exam = exam;
        System.out.println("Now this.exam = " + this.exam);
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
//                        System.out.println("After 1 sec...");
                        convertTime();
                        if (totalSec <= 0) {
                            timer.cancel();
                            time.setText("00:00:00");
                            // saveing data to database
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

    public void setData(int proctorPort, List<Question> questions) {
            while(!setupProctoringStuff(proctorPort)) {
                GuiUtil.alert(Alert.AlertType.ERROR, "Could not access you camera. Close all other applications and try again!!");
            }
            this.questionList = questions;
            Collections.shuffle(this.questionList);
            renderProgress();
            setNextQuestion();
            setTimer();
    }

    private boolean setupProctoringStuff(int proctorPort) {
        for(Webcam w : Webcam.getWebcams()) {
            try {
                webcam = Webcam.getWebcamByName(w.getName());
                webcam.open();
                break;
            } catch (Exception ignored) {}
        }
        if(webcam == null)
            return false;
        try {
            sendVideoSocket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Thread videoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramPacket packet;
                while(true) {
//                    try {
//                        Thread.sleep(500);
                        BufferedImage image = webcam.getImage();
                        String registrationNumber = Main.userRegistrationNumber;
                        byte [] imageByte = UdpUtil.bufferedImageToByteArray(image);
                        byte [] registrationNumberByte = UdpUtil.objectToByteArray(registrationNumber);
                        System.out.println("image being sent by Registraiton number = " + registrationNumber);
                        Object [] wrapped = {registrationNumberByte, imageByte};
                        UdpUtil.sendObjectToPort(wrapped, proctorPort);
//                    } catch (InterruptedException ignored) {}
                }
            }
        });
        videoThread.setDaemon(true);
        videoThread.start();
        return true;
    }

    private void renderProgress() {
        for (int i = 0; i < this.questionList.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    getClass()
                            .getResource("../fxml/ProgressCircleFXML.fxml"));
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

        this.optionA.setSelected(true);

    }

    private void bindFields() {
        this.question.textProperty().bind(this.questionsObservable.question);
        this.optionD.textProperty().bind(this.questionsObservable.optionD);
        this.optionC.textProperty().bind(this.questionsObservable.optionC);
        this.optionB.textProperty().bind(this.questionsObservable.optionB);
        this.optionA.textProperty().bind(this.questionsObservable.optionA);
    }
    public String getRightAnswer(Question question) {
        if(question.getCorrectOption() == 1) {
            return question.getOptionA();
        }
        else if(question.getCorrectOption() == 2) {
            return question.getOptionB();
        }
        else if(question.getCorrectOption() == 3) {
            return question.getOptionC();
        }
        return question.getOptionD();
    }

    public void nextQuestions(ActionEvent actionEvent) {
        boolean isRight = false;
        {
            // checking answer
            JFXRadioButton selectedButton = (JFXRadioButton) options.getSelectedToggle();
            String userAnswer = selectedButton.getText();
            String rightAnswer = getRightAnswer(this.currentQuestion);
            if (userAnswer.trim().equalsIgnoreCase(rightAnswer.trim())) {
                isRight = true;
                this.numberOfRightAnswers++;
            }

            // saving Answer to hashMap
            studentAnswers.put(this.currentQuestion, userAnswer);
        }
        Node circleNode = this.progressPane.getChildren().get(currentIndex - 1);
        ProgressCircleFXMLController controller = (ProgressCircleFXMLController) circleNode.getUserData();


        if (isRight) {
            controller.setRightAnsweredColor();
        } else {
            controller.setWrongAnsweredColor();
        }
        this.setNextQuestion();
    }

    private void setNextQuestion() {
        if (!(currentIndex >= questionList.size())) {

            {
                // chaning the color
                Node circleNode = this.progressPane.getChildren().get(currentIndex);
                ProgressCircleFXMLController controller = (ProgressCircleFXMLController) circleNode.getUserData();
                controller.setCurrentQuestionColor();
            }

            this.currentQuestion = this.questionList.get(currentIndex);
            //Shuffling options
//            List<String> options = new ArrayList<>();
//            options.add(this.currentQuestion.getOptionA());
//            options.add(this.currentQuestion.getOptionB());
//            options.add(this.currentQuestion.getOptionC());
//            options.add(this.currentQuestion.getOptionD());
//            Collections.shuffle(options);
//
//            this.currentQuestion.setOptionA(options.get(0));
//            this.currentQuestion.setOptionB(options.get(1));
//            this.currentQuestion.setOptionC(options.get(2));
//            this.currentQuestion.setOptionD(options.get(3));

//            this.question.setText(this.currentQuestion.getQuestion());
//            this.optionA.setText(options.get(0));
//            this.optionB.setText(options.get(1));
//            this.optionC.setText(options.get(2));
//            this.optionD.setText(options.get(3));

            this.questionsObservable.setQuestion(this.currentQuestion);
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
        System.out.println("I am inside submit! I am inside submit! I am inside submit! I am inside submit! I am inside submit! I am inside submit! ");
        System.out.println("in submit() : " + this.exam.getExamId());
        SubmitExamRequest submitExamRequest = new SubmitExamRequest(this.exam,numberOfRightAnswers);
        Main.sendRequest(submitExamRequest);
        openResultScreen();
    }


    private void openResultScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../fxml/QuizResultFXML.fxml"));
        Stage stage=(Stage)submit.getScene().getWindow();
        Scene scene=null;
        try {
            scene=new Scene(fxmlLoader.load());
            QuizResultFXMLController controller = fxmlLoader.getController();
            controller.setValues(this.studentAnswers , numberOfRightAnswers , exam , questionList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setTitle("Your result");
    }
}
