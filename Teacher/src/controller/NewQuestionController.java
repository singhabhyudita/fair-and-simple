package controller;

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
    public ToggleButton objectiveToggleButton;

    @FXML
    public void okQuestionResponse(ActionEvent actionEvent) {
        if(!objectiveToggleButton.isSelected() &&
                (optionATextField.getText().length() == 0
                        || optionBTextField.getText().length() == 0
                        || optionCTextField.getText().length() == 0
                        || optionDTextField.getText().length() == 0))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "All options have to be specified.");
            alert.showAndWait();
            return;
        }
        if(!objectiveToggleButton.isSelected() && correctAnswerToggleGroup.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select a right answer.");
            alert.showAndWait();
            return;
        }
        if(questionTextArea.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Question cannot be empty.");
            alert.showAndWait();
            return;
        }
        int ans = -1;
        RadioButton selectedToggle = (RadioButton) correctAnswerToggleGroup.getSelectedToggle();
        if(selectedToggle != null) {
            if(selectedToggle.getText().equals("A")) ans = 1;
            else if(selectedToggle.getText().equals("B")) ans = 2;
            else if(selectedToggle.getText().equals("C")) ans = 3;
            else if(selectedToggle.getText().equals("D")) ans = 4;
        }
        Main.tempHolder = new Question("", //Question ID can't be given here
                questionTextArea.getText(),
                optionATextField.getText(),
                optionBTextField.getText(),
                optionCTextField.getText(),
                optionDTextField.getText(),
                ans,
                !objectiveToggleButton.isSelected());
        ((Stage) cancelQuestionButton.getScene().getWindow()).close();
    }

    @FXML
    public void cancelQuestionResponse(ActionEvent actionEvent) {
    }

    public void callFirst(String question, String optionA, String optionB, String optionC, String optionD, int selectedOption) {
        questionTextArea.setText(question);
        questionTextArea.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{0,200}") ? c : null));
        optionATextField.setText(optionA);
        optionATextField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{0,200}") ? c : null));
        optionBTextField.setText(optionB);
        optionBTextField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{0,200}") ? c : null));
        optionCTextField.setText(optionC);
        optionCTextField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{0,200}") ? c : null));
        optionDTextField.setText(optionD);
        optionDTextField.setTextFormatter(new TextFormatter<>(c -> c.getControlNewText().matches(".{0,200}") ? c : null));
        if(selectedOption == 1) optionARadioButton.setSelected(true);
        else if(selectedOption == 2) optionBRadioButton.setSelected(true);
        else if(selectedOption == 3) optionCRadioButton.setSelected(true);
        else if(selectedOption == 4) optionDRadioButton.setSelected(true);
    }

    public void objectiveToggleResponse(ActionEvent actionEvent) {
        if(objectiveToggleButton.isSelected()) {
            objectiveToggleButton.setText("Subjective");
            optionARadioButton.setVisible(false);
            optionBRadioButton.setVisible(false);
            optionCRadioButton.setVisible(false);
            optionDRadioButton.setVisible(false);
            optionATextField.setVisible(false);
            optionBTextField.setVisible(false);
            optionCTextField.setVisible(false);
            optionDTextField.setVisible(false);
        } else {
            objectiveToggleButton.setText("Objective");
            optionARadioButton.setVisible(true);
            optionBRadioButton.setVisible(true);
            optionCRadioButton.setVisible(true);
            optionDRadioButton.setVisible(true);
            optionATextField.setVisible(true);
            optionBTextField.setVisible(true);
            optionCTextField.setVisible(true);
            optionDTextField.setVisible(true);
        }
    }
}
