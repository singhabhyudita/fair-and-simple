package table;

public class EnrollmentTable {
    public static final String TABLE_NAME="enrollment";
    public static final String COLUMN_COURSE_ID="courseID";
    public static final String COLUMN_REGISTGRATION_NO="registrationNo";

    public static final  String QUERY_JOIN_COURSE_BY_ID="INSERT INTO "+TABLE_NAME+" VALUES (?,?);";
    public static final String QUERY_GET_COURSES_BY_ID="SELECT "+ COLUMN_COURSE_ID +" FROM "+TABLE_NAME+" WHERE "+
            COLUMN_REGISTGRATION_NO +" =?;";
    public static final String QUERY_GET_STUDENTS_BY_COURSE_ID="SELECT "+ COLUMN_REGISTGRATION_NO + " FROM "+
            TABLE_NAME+ " WHERE "+COLUMN_COURSE_ID+" =?;";
}
