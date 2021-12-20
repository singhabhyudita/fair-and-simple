package controller;

import entity.Question;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class QuizResultSingleQuestionFXMLController implements Initializable {
    public Label question;
    public Label optionA;
    public Label optionB;
    public Label optionC;
    public Label optionD;

    private Question questionObject;
    private String userAnswer;

    public void setValues(Question questionObject, String userAnswer) {
        this.questionObject = questionObject;
        if(userAnswer==null)
            userAnswer = "";
        this.userAnswer = userAnswer;
        System.out.println("inside setValues() :"+this.userAnswer);
        setTexts();
    }

    private void setTexts(){
        this.question.setText(this.questionObject.getQuestion());
        this.optionA.setText(this.questionObject.getOptionA());
        this.optionB.setText(this.questionObject.getOptionB());
        this.optionC.setText(this.questionObject.getOptionC());
        this.optionD.setText(this.questionObject.getOptionD());

        settingColors();
    }

    private void settingColors() {
        Label rightAnswer = null;

        if(this.questionObject.getCorrectOption()==1){
            rightAnswer = optionA;
        }
        else if(this.questionObject.getCorrectOption()==2){
            rightAnswer = optionB;
        }
        else if(this.questionObject.getCorrectOption()==3){
            rightAnswer = optionC;
        }
        else if(this.questionObject.getCorrectOption()==4){
            rightAnswer = optionD;
        }

        if(!userAnswer.trim().equalsIgnoreCase(rightAnswer.getText().trim())){
            System.out.println("userAnswer.trim():"+userAnswer.trim());
            System.out.println("rightAnswer.getText().trim():"+rightAnswer.getText().trim());
            Label userAnswer = null;
            System.out.println("optionA.getText():"+optionA.getText().trim());
            if(optionA.getText().trim().equalsIgnoreCase(this.userAnswer.trim())){
                userAnswer = optionA;
            }
            else if(optionB.getText().trim().equalsIgnoreCase(this.userAnswer.trim())){
                userAnswer = optionB;
            }

            else if(optionC.getText().trim().equalsIgnoreCase(this.userAnswer.trim())){
                userAnswer = optionC;
            }
            else if(optionD.getText().trim().equalsIgnoreCase(this.userAnswer.trim())){
                userAnswer = optionD;
            }

            userAnswer.setTextFill(Color.web("#B83227"));
            userAnswer.setText("✖ " + userAnswer.getText());
        }
        rightAnswer.setTextFill(Color.web("#26ae60"));
        rightAnswer.setText("✔ "+rightAnswer.getText());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
