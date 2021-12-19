package table;

public class ExamQuestionsTable {
    public static final String TABLE_NAME = "exam_questions";
    public static final String QUESTION_ID_COLUMN = "questionID";
    public static final String EXAM_ID_COLUMN = "examID";
    public static final String QUESTION_COLUMN = "question";
    public static final String OPTION_A_COLUMN = "optionA";
    public static final String OPTION_B_COLUMN = "optionB";
    public static final String OPTION_C_COLUMN = "optionC";
    public static final String OPTION_D_COLUMN = "optionD";
    public static final String CORRECT_OPTION_COLUMN = "correctOption";

    public static final String ADD_QUESTION_QUERY = "INSERT INTO " + TABLE_NAME
            +" ( " + EXAM_ID_COLUMN + ", " + QUESTION_COLUMN + ", "
            + OPTION_A_COLUMN + ", " + OPTION_B_COLUMN + ", " + OPTION_C_COLUMN
            + ", " + OPTION_D_COLUMN + ", " + CORRECT_OPTION_COLUMN + ") "
            + "VALUES (?, ?, ?, ?, ?, ?, ?);";
    public static final String DELETE_QUESTIONS_BY_EXAM_ID = "DELETE FROM " + TABLE_NAME
            + " WHERE " + EXAM_ID_COLUMN + " = ?;";
    public static final String GET_EXAM_QUESTIONS_BY_EXAM_ID = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + EXAM_ID_COLUMN + " = ?;";
}
