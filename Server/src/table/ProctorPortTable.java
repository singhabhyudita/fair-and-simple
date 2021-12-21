package table;

public class ProctorPortTable {
    public static final String TABLE_NAME = "proctor_port";
    public static final String COLUMN_EXAM_ID = "examID";
    public static final String COLUMN_PROCTOR_PORT = "proctorPort";

    public static final String ADD_PROCTOR_PORT_FOR_EXAM = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?);";
    public static final String GET_PORT_BY_EXAM_ID = "SELECT * FROM " + TABLE_NAME + " WHERE "
            + COLUMN_EXAM_ID + " = ?;";
}
