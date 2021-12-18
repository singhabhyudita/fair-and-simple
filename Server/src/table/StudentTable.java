package table;

public class  StudentTable  {
    public static final String TABLE_NAME="student";
    public static final String COLUMN_REGISTRATION_NUMBER="registrationNo";
    public static final String COLUMN_FIRST_NAME="firstName";
    public static final String COLUMN_LAST_NAME="lastName";
    public static final String COLUMN_EMAIL_ID="emailID";
    public static final String COLUMN_PASSWORD="password";
    public static final String COLUMN_LAST_ACTIVE="lastActive";
    public static final String COLUMN_SALT="salt";

    public static final String QUERY_LOGIN="SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_REGISTRATION_NUMBER+" =? AND "+ COLUMN_PASSWORD+"= ?;";
    public static final String QUERY_UPDATE_LAST_ACTIVE="UPDATE "+TABLE_NAME+ " SET "+COLUMN_LAST_ACTIVE+" = CURRENT_TIMESTAMP WHERE "+COLUMN_REGISTRATION_NUMBER+" =?;";
    public static final String QUERY_REGISTER="INSERT INTO "+TABLE_NAME+" (" +COLUMN_REGISTRATION_NUMBER+
            ","+COLUMN_FIRST_NAME+","+COLUMN_LAST_NAME+","+COLUMN_EMAIL_ID+","+COLUMN_PASSWORD+") VALUES "+" (?,?,?,?,?);";
    public static final String QUERY_CHANGE_PASSWORD= "UPDATE "+ TABLE_NAME + " SET "+ COLUMN_PASSWORD +
            " = ? WHERE "+ COLUMN_REGISTRATION_NUMBER + " = ? AND "+ COLUMN_PASSWORD +" = ?;";
    public static final  String QUERY_STUDENT_DETAILS_BY_ID=" SELECT * FROM "+TABLE_NAME+ " WHERE "+COLUMN_REGISTRATION_NUMBER+" =?;";
    public static final String QUERY_STUDENT_BY_COURSE_ID= " SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_REGISTRATION_NUMBER
            + " IN (" + EnrollmentTable.QUERY_GET_STUDENTS_BY_COURSE_ID + ");";
}
