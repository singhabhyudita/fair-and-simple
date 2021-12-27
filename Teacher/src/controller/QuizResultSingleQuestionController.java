package controller;

import entity.Question;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

import java.util.Objects;

public class QuizResultSingleQuestionController {
    @FXML
    public Label question;
    @FXML
    public Label optionA;
    @FXML
    public Label optionB;
    @FXML
    public Label optionC;
    @FXML
    public Label optionD;
    @FXML
    public TextArea answerTextArea;

    private Question questionObject;
    private String userAnswer;

    public void setValues(Question question, String s, int marks) {
        this.questionObject = question;
        System.out.println("string sent = " + s);
        if(s==null)
            s="";
        this.userAnswer = s;
        this.question.setText(question.getQuestion());
        if(question.isObjective()) {
            answerTextArea.setVisible(false);
            optionA.setVisible(true);
            optionB.setVisible(true);
            optionC.setVisible(true);
            optionD.setVisible(true);
            optionA.setText(question.getOptionA());
            optionB.setText(question.getOptionB());
            optionC.setText(question.getOptionC());
            optionD.setText(question.getOptionD());
            settingColors();
        } else {
            answerTextArea.setVisible(true);
            optionA.setVisible(false);
            optionB.setVisible(false);
            optionC.setVisible(false);
            optionD.setVisible(false);
            answerTextArea.setText(s);
            answerTextArea.setEditable(false);
            if(marks != -1) {
                answerTextArea.setStyle("-fx-control-inner-background:#000000;" +
                        "-fx-font-family: Consolas; -fx-highlight-fill: #03fc49;" +
                        "-fx-highlight-text-fill: #000000; -fx-text-fill: #03fc49; ");
            } else {
                answerTextArea.setStyle("-fx-control-inner-background:#000000;" +
                        "-fx-font-family: Consolas; -fx-highlight-fill: #e62c44;" +
                        "-fx-highlight-text-fill: #000000; -fx-text-fill: #e62c44; ");
            }
        }
    }

    private void settingColors() {
        Label rightAnswer = null;

        if(this.questionObject.getCorrectOption()==1) rightAnswer = optionA;
        else if(this.questionObject.getCorrectOption()==2) rightAnswer = optionB;
        else if(this.questionObject.getCorrectOption()==3) rightAnswer = optionC;
        else if(this.questionObject.getCorrectOption()==4) rightAnswer = optionD;

        System.out.println("Right answer = " + rightAnswer);
        System.out.println("user answer = " + this.userAnswer);

        if(!Objects.equals(this.userAnswer.trim(), rightAnswer.getText().trim())) {
            Label userAnswer = null;

            if(Objects.equals(optionA.getText(), this.userAnswer)) userAnswer = optionA;
            else if(Objects.equals(optionB.getText(), this.userAnswer)) userAnswer = optionB;
            else if(Objects.equals(optionC.getText(), this.userAnswer)) userAnswer = optionC;
            else if(Objects.equals(optionD.getText(), this.userAnswer)) userAnswer = optionD;

            if(userAnswer != null) {
                userAnswer.setTextFill(Color.web("#B83227"));
                userAnswer.setText("✖ " + userAnswer.getText());
            }
        }
        if(userAnswer != null) {
            rightAnswer.setTextFill(Color.web("#26ae60"));
            rightAnswer.setText("✔ "+rightAnswer.getText());
        }
    }
}
