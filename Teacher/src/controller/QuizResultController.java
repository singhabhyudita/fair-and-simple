package controller;

import entity.Question;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import response.GetResultForStudentResponse;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QuizResultController {
    public PieChart attempedChart;
    public PieChart scoreChart;
    public VBox questionsContainer;

    private Map<Question, String> userAnswers ;
    private Integer numberOfRightAnswers = 0;
    private List<Question> questionList;
    private Integer notAttemped = 0 ;
    private Integer attemped = 0 ;
    private GetResultForStudentResponse response;

    private void renderQuestions(){
        for(int i = 0 ; i < this.questionList.size() ; i ++){
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().
                    getResource("../views/QuizResultSingleQuestionView.fxml"));
            try {
                Node node = fxmlLoader.load();
                QuizResultSingleQuestionController controller= fxmlLoader.getController();
                System.out.println("question = " + this.questionList.get(i).getQuestion());
                System.out.println("answer = " + this.userAnswers.get(this.questionList.get(i)));
                controller.setValues(this.questionList.get(i),
                        this.userAnswers.get(this.questionList.get(i)),
                        getMarksForSubjectiveQuestion(this.questionList.get(i)));
                questionsContainer.getChildren().add(node);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getMarksForSubjectiveQuestion(Question question) {
        return response.getSubjectiveQuestionMarks().getOrDefault(question.getQuestionId(), -1);
    }


    private void setValuesToChart(){
        ObservableList<PieChart.Data> attempedData = this.attempedChart.getData();
        System.out.println("Attemped = " + attemped);
        attempedData.add(new PieChart.Data(String.format("Attemped (%d)", this.attemped) , this.attemped));
        attempedData.add(new PieChart.Data(String.format("Not Attemped (%d)", response.getQuestions().size() - this.attemped) , response.getQuestions().size() - this.attemped));

        ObservableList<PieChart.Data> scoreChartDatata = this.scoreChart.getData();
        scoreChartDatata.add(new PieChart.Data(
                String.format("Right Answers (%d)", this.numberOfRightAnswers) , this.numberOfRightAnswers));
        scoreChartDatata.add(new PieChart.Data(
                String.format("Wrong Answers (%d)", this.attemped - this.numberOfRightAnswers) , this.attemped-this.numberOfRightAnswers));


    }

    public void setValues(GetResultForStudentResponse response) {
        System.out.println("Inside QuizResultController.setValues()");
        this.response = response;
        numberOfRightAnswers = 0;
        attemped = 0;
        notAttemped = 0;
        userAnswers = new HashMap<>();
        System.out.println("response.getObjectiveAnswers.length = " + response.getObjectiveAnswers().size());
        for(Question answer : response.getObjectiveAnswers()) {
            System.out.println("Finding question for " + answer.getQuestion());
            for(Question question : response.getQuestions()) {
                if(Objects.equals(question.getQuestionId(), answer.getQuestionId())) {
                    System.out.println("Question = " + question.getQuestionId());
                    System.out.println("Correct answer = " + question.getCorrectOption());
                    System.out.println("Answerd option = " + answer.getCorrectOption());
                    userAnswers.put(question, getOptionValue(question, answer.getCorrectOption()));
                    System.out.println("getOptionValue: "+getOptionValue(question, answer.getCorrectOption()));
                    if(question.getCorrectOption() == answer.getCorrectOption()) numberOfRightAnswers++;
                    if(answer.getCorrectOption() == -1) notAttemped++;
                    else attemped++;
                }
            }
        }
        for(String questionId : response.getSubjectiveQuestionMarks().keySet()) {
            if(response.getSubjectiveQuestionMarks().get(questionId) == 1) numberOfRightAnswers++;
        }
        addSubjectiveAnswersToUserAnswers(response.getSubjectiveAnswer(), response);
        this.questionList = response.getQuestions();

        System.out.println("User Answers = ");
        for(Question question : userAnswers.keySet()) {
            System.out.println("Question = " + question.getQuestion() + " answer = " + userAnswers.get(question));
        }

        setValuesToChart();
        renderQuestions();
    }

    private void addSubjectiveAnswersToUserAnswers(byte[] subjectiveAnswer, GetResultForStudentResponse response) {
        if(subjectiveAnswer == null) return; // quiz had no subjective questions.
        try {
            File temp = new File("temp.txt");
            if(!temp.exists()) temp.createNewFile();
            System.out.println("TEmp file = " + temp);
            System.out.println("Subjective answer = " + subjectiveAnswer);
            Files.write(temp.toPath(), subjectiveAnswer);
            FileReader reader = new FileReader(temp);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String questionId = "";
            String answer = "";
            String line;
            while((line = bufferedReader.readLine()) != null) {
                if(line.contains("==>")) {
                    if(!questionId.equals("")) {
                        userAnswers.put(getQuestionFromQuestionId(questionId, response), answer);
                        attemped++;
                    }
                    int id = line.indexOf("==>");
                    questionId = line.substring(0, id-1).trim();
                    answer = line.substring(id + 3).trim();
                } else {
                    answer = answer + line.trim();
                }
            }
            if(!questionId.equals("")) {
                userAnswers.put(getQuestionFromQuestionId(questionId, response), answer);
                attemped++;
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Question getQuestionFromQuestionId(String questionId, GetResultForStudentResponse response) {
        for(Question question : response.getQuestions()) {
            if(question.getQuestionId().equals(questionId)) return question;
        }
        return null;
    }

    private String getOptionValue(Question question, Integer op) {
        System.out.println("Option = " + op);
        System.out.println(question.toString());
        if(op == 1) return question.getOptionA();
        else if(op == 2) return question.getOptionB();
        else if(op == 3) return question.getOptionC();
        else if(op == 4) return question.getOptionD();
        return "";
    }
}
