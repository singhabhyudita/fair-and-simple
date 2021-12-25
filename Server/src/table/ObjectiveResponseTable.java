package table;

public class ObjectiveResponseTable {
    public final static String TABLE_NAME = "objective_response";
    public final static String COLUMN_QUESTION_ID = "questionID";
    public final static String COLUMN_REGISTRATION_NO = "registrationNo";
    public final static String COLUMN_MARKED = "marked";
    private final static String COLUMN_EXAM_ID = "examID";

    public final static String ADD_ENTRY_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?);";
    public final static String GET_ENTRY_BY_REGISTRATION_NO_AND_EXAM_ID = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + COLUMN_EXAM_ID + " = ? AND " + COLUMN_REGISTRATION_NO + " = ?";
}
