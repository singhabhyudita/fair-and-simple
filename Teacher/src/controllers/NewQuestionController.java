package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Main;
import entity.Question;

public class NewQuestionController {
    @FXML
    public RadioButton optionARadioButton;
    @FXML
    public RadioButton optionBRadioButton;
    @FXML
    public RadioButton optionCRadioButton;
    @FXML
    public RadioButton optionDRadioButton;
    @FXML
    public Button okQuestionButton;
    @FXML
    public Button cancelQuestionButton;
    @FXML
    public TextField optionATextField;
    @FXML
    public TextField optionBTextField;
    @FXML
    public TextField optionCTextField;
    @FXML
    public TextField optionDTextField;
    @FXML
    public TextArea questionTextArea;
    @FXML
    public ToggleGroup correctAnswerToggleGroup;

    @FXML
    public void okQuestionResponse(ActionEvent actionEvent) {
        if(optionATextField.getText().length() == 0
                || optionBTextField.getText().length() == 0
                || optionCTextField.getText().length() == 0
                || optionDTextField.getText().length() == 0)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "All options have to be specified.");
            alert.showAndWait();
            return;
        }
        if(correctAnswerToggleGroup.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select a right answer.");
            alert.showAndWait();
            return;
        }
        if(questionTextArea.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Question cannot be empty.");
            alert.showAndWait();
            return;
        }
        int ans = 1;
        RadioButton selectedToggle = (RadioButton) correctAnswerToggleGroup.getSelectedToggle();
        if(selectedToggle.getText().equals("B")) ans = 2;
        else if(selectedToggle.getText().equals("C")) ans = 3;
        else if(selectedToggle.getText().equals("D")) ans = 4;
        Main.tempHolder = new Question("", //Question ID can't be given here
                questionTextArea.getText(),
                optionATextField.getText(),
                optionBTextField.getText(),
                optionCTextField.getText(),
                optionDTextField.getText(),
                ans);
        ((Stage) cancelQuestionButton.getScene().getWindow()).close();
    }

    @FXML
    public void cancelQuestionResponse(ActionEvent actionEvent) {
    }

    public void callFirst(String question, String optionA, String optionB, String optionC, String optionD, int selectedOption) {
        questionTextArea.setText(question);
        optionATextField.setText(optionA);
        optionBTextField.setText(optionB);
        optionCTextField.setText(optionC);
        optionDTextField.setText(optionD);
        if(selectedOption == 1) optionARadioButton.setSelected(true);
        else if(selectedOption == 2) optionBRadioButton.setSelected(true);
        else if(selectedOption == 3) optionCRadioButton.setSelected(true);
        else if(selectedOption == 4) optionDRadioButton.setSelected(true);
    }
}
