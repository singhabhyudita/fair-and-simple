package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Stage;
import main.GuiUtil;
import main.TeacherApplication;
import request.CreateTeamRequest;
import response.CreateTeamResponse;

public class CreateTeamController {
    @FXML
    public TextField teamNameTextField;
    @FXML
    public TextField teamCodeTextField;
    @FXML
    public Button backButton;
    @FXML
    public Button createTeamButton;
    @FXML
    public TextArea teamDescriptionTextArea;

    @FXML
    public void backResponse(ActionEvent actionEvent) {
        GuiUtil.goToHome((Stage) backButton.getScene().getWindow());
    }

    @FXML
    public void createTeamResponse(ActionEvent actionEvent) {
        if(teamNameTextField.getText() == null || teamNameTextField.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Enter a non-empty team name.");
            alert.showAndWait();
        } else {
            teamNameTextField.setEditable(false);
            teamDescriptionTextArea.setEditable(false);
            createTeamButton.setDisable(true);
            backButton.setDisable(true);
            Platform.runLater(() -> {
                CreateTeamRequest request = new CreateTeamRequest(TeacherApplication.getTeacherId(), teamDescriptionTextArea.getText(), teamNameTextField.getText());
                TeacherApplication.sendRequest(request);
                CreateTeamResponse response = (CreateTeamResponse) TeacherApplication.receiveResponse();
                System.out.println("Response = " + response);
                if(response == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could Not create a team. Please try again.");
                    alert.showAndWait();
                    teamNameTextField.setEditable(true);
                    teamDescriptionTextArea.setEditable(true);
                    backButton.setDisable(false);
                    createTeamButton.setDisable(false);
                } else {
                    teamCodeTextField.setText(response.getTeamCode());
                    backButton.setDisable(false);
                }
            });
        }
    }

    public void onInput(InputMethodEvent inputMethodEvent) {
        if(teamDescriptionTextArea.getText().length() > 200) {
            teamDescriptionTextArea.setText(teamDescriptionTextArea.getText(0, 200));
            GuiUtil.alert(Alert.AlertType.WARNING, "Character limit exceeded!");
        }
    }
}
