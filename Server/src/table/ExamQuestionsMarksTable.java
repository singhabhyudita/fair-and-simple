package table;

public class ExamQuestionsMarksTable {
    public static final String TABLE_NAME = "exam_questions_marks";
    public static final String COLUMN_EXAM_ID = "examID";
    public static final String COLUMN_QUESTION_ID = "questionID";
    public static final String COLUMN_REGISTRATION_NO = "registrationNo";
    public static final String COLUMN_CORRECT = "correct";

    public static final String ADD_ENTRY_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?);";
    public static final String GET_ENTRY_BY_QUESTION_ID = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + COLUMN_QUESTION_ID + " = ? AND " + COLUMN_REGISTRATION_NO + " = ?;";
    public static final String GET_ENTRY_BY_EXAM_ID_AND_REGISTRATION_NO = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + COLUMN_EXAM_ID + " = ? AND " + COLUMN_REGISTRATION_NO + " = ?;";
}
