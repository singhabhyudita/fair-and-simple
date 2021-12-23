package table;

public class ObjectiveResponseTable {
    public final static String TABLE_NAME = "objective_response";
    public final static String COLUMN_QUESTION_ID = "questionID";
    public final static String COLUMN_REGISTRATION_NO = "registrationNo";
    public final static String COLUMN_MARKED = "marked";

    public final static String ADD_ENTRY_QUERY = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?);";
}
