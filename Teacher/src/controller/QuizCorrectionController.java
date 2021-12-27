package controller;

import entity.Question;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import util.GuiUtil;
import main.Main;
import request.SubmitCorrectionRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizCorrectionController {

    @FXML
    public Label questionLabel;
    @FXML
    public TextArea answerTextArea;
    @FXML
    public Button nextQuestionButton;
    @FXML
    public Button submitCorrectionButton;
    @FXML
    public ToggleButton correctToggleButton;

    private List<Question> questions;
    private byte [] answers;
    List<Pair<Question, String>> questionAnswerList;
    private Map<Question, String> userAnswers;
    int displayedQuestionIndex = 0;
    Map<String, Integer> questionIdMarksMap;
    private String registrationNumber;
    private String examId;

    public void setValues(String examId, String registrationNumber, List<Question> questions, byte[] subjectiveAnswer) {
        submitCorrectionButton.setVisible(false);
        questionIdMarksMap = new HashMap<>();
        questionAnswerList = new ArrayList<>();
        this.registrationNumber = registrationNumber;
        this.examId = examId;
        this.questions = questions;
        this.answers = subjectiveAnswer;
        userAnswers = new HashMap<>();
        System.out.println("Setting different values");
        addSubjectiveAnswersToUserAnswers(answers);
        for(Question q : userAnswers.keySet()) System.out.println("Question = " + q.getQuestion() + " Answer = " + userAnswers.get(q));
        populateAnswersInList();
        renderQuestion();
    }

    private void renderQuestion() {
        if(displayedQuestionIndex == questionAnswerList.size()) {
            submitCorrectionResponse(null);
            return;
        }
        Question question = questionAnswerList.get(displayedQuestionIndex).getKey();
        String answer = questionAnswerList.get(displayedQuestionIndex).getValue();
        questionLabel.setText(question.getQuestion());
        answerTextArea.setText(answer);
        correctToggleResponse(null);
    }

    private void populateAnswersInList() {
        for(Question question : userAnswers.keySet()) {
            questionAnswerList.add(new Pair<>(question, userAnswers.get(question)));
        }
        for(Pair<Question, String> p : questionAnswerList)
            System.out.println("Question = " + p.getKey().getQuestion() + " Answer = " + p.getValue());
    }

    private void addSubjectiveAnswersToUserAnswers(byte[] subjectiveAnswer) {
        if (subjectiveAnswer == null) return;
        System.out.println("Adding subjective answers.");
        try {
            File temp = new File("temp.txt");
            if(!temp.exists()) temp.createNewFile();
            Files.write(temp.toPath(), subjectiveAnswer);
            FileReader reader = new FileReader(temp);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String questionId = "";
            String answer = "";
            String line;
            while((line = bufferedReader.readLine()) != null) {
                line += '\n';
                System.out.println("Reading lines => " + line);
                if(line.contains("==>")) {
                    if(!questionId.equals("")) {
                        userAnswers.put(getQuestionFromQuestionId(questionId), answer);
                        System.out.println("Question id = " + questionId + " answer = " + answer);
                    }
                    int id = line.indexOf("==>");
                    questionId = line.substring(0, id-1).trim();
                    answer = line.substring(id + 3).trim();
                } else {
                    answer = answer + line.trim();
                }
            }
            if(!questionId.equals("")) {
                userAnswers.put(getQuestionFromQuestionId(questionId), answer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Question getQuestionFromQuestionId(String questionId) {
        for(Question question : questions) {
            if(question.getQuestionId().equals(questionId)) return question;
        }
        return null;
    }

    @FXML
    public void nextQuestionResponse(ActionEvent actionEvent) {
        questionIdMarksMap.put(
                questionAnswerList.get(displayedQuestionIndex).getKey().getQuestionId(),
                correctToggleButton.isSelected() ? 0 : 1);
        displayedQuestionIndex++;
        if(displayedQuestionIndex == questionAnswerList.size()) {
            nextQuestionButton.setVisible(false);
            submitCorrectionButton.setVisible(true);
        } else {
            renderQuestion();
        }
    }

    @FXML
    public void submitCorrectionResponse(ActionEvent actionEvent) {
        SubmitCorrectionRequest request = new SubmitCorrectionRequest(this.registrationNumber, examId, questionIdMarksMap);
        System.out.println("Registration no = " + request.getRegistrationNumber());
        Main.sendRequest(request);
        System.out.println("COrrection Request sent\n");
        if(actionEvent != null)
            ((Stage) submitCorrectionButton.getScene().getWindow()).close();
        else
            GuiUtil.alert(Alert.AlertType.INFORMATION, "Exam corrected. Click on the student again to see the result.");
    }

    public void correctToggleResponse(ActionEvent actionEvent) {
        if(correctToggleButton.isSelected()) {
            correctToggleButton.setText("Incorrect");
            answerTextArea.setStyle("-fx-control-inner-background:#000000;" +
                    "-fx-font-family: Consolas; -fx-highlight-fill: #e62c44;" +
                    "-fx-highlight-text-fill: #000000; -fx-text-fill: #e62c44; ");
        } else {
            correctToggleButton.setText("Correct");
            answerTextArea.setStyle("-fx-control-inner-background:#000000;" +
                    "-fx-font-family: Consolas; -fx-highlight-fill: #03fc49;" +
                    "-fx-highlight-text-fill: #000000; -fx-text-fill: #03fc49; ");
        }
    }
}
