package table;

public class ResultTable {
    public static final String TABLE_NAME = "result";
    public static final String REGISTRATION_NUMBER_COLUMN = "registrationNumber";
    public static final String EXAM_ID_COLUMN = "examID";
    public static final String MARKS_OBTAINED_COLUMN = "marksObtained";

    public static final String GET_MARKS_BY_EXAM_ID = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + EXAM_ID_COLUMN + " = ?;";
}
