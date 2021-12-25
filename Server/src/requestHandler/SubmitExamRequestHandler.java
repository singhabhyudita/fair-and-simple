package requestHandler;
import entity.Question;
import main.Server;
import request.SubmitExamRequest;
import response.SubmitExamResponse;
import table.AnswerFilesTable;
import table.ObjectiveResponseTable;
import table.ResultTable;

import java.io.*;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SubmitExamRequestHandler extends RequestHandler {

    final private Connection connection;
    final private ObjectOutputStream oos;
    final private ObjectInputStream ois;
    final private SubmitExamRequest request;
    final private String subjectivePath;

    public SubmitExamRequestHandler(Connection connection, ObjectOutputStream oos, ObjectInputStream ois, SubmitExamRequest request) {
        this.connection = connection;
        this.oos = oos;
        this.ois = ois;
        this.request = request;
        this.subjectivePath = "./answers/" + request.getExam().getCourseId() +
                "/" + request.getExam().getExamId() + "/";
    }

    @Override
    public void sendResponse(String userID) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ObjectiveResponseTable.ADD_ENTRY_QUERY);
            preparedStatement.setString(2, userID);
            for(Question answer : request.getObjectiveAnswers()) {
                preparedStatement.setString(1, answer.getQuestionId());
                preparedStatement.setInt(3, answer.getCorrectOption());
                preparedStatement.setString(4, request.getExam().getExamId());
                preparedStatement.executeUpdate();
            }

            Server.sendResponse(oos, new SubmitExamResponse(true));
            byte [] answers = (byte[]) ois.readObject();
            createFileForSubjective(answers, userID);

            preparedStatement = connection.prepareStatement(AnswerFilesTable.ADD_FILE_PATH_QUERY);
            preparedStatement.setString(1, request.getExam().getExamId());
            preparedStatement.setString(2, userID);
            preparedStatement.setString(3, request.getExam().getCourseId());
            preparedStatement.setString(4, subjectivePath + userID + ".txt");
            preparedStatement.executeUpdate();

        } catch(SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createFileForSubjective(byte [] content, String userID) {
        try {
            File directory = new File(subjectivePath);
            if(!directory.exists())
                directory.mkdirs();
            File newFile = new File(subjectivePath + userID + ".txt");
            newFile.createNewFile();
            Files.write(newFile.toPath(), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
