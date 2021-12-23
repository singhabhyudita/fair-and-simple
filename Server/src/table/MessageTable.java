package table;

public class MessageTable {
    public static final String TABLE_NAME="message";
    public static final String COLUMN_MESSAGE_ID="messageID";
    public static final String COLUMN_SENDER_ID="senderID";
    public static final String COLUMN_COURSE_ID="courseID";
    public static final String COLUMN_TEXT="text";
    public static final String COLUMN_IMAGE="image";
    public static final String COLUMN_SENT_AT="sentAt";
    public static final String COLUMN_IS_STUDENT="isStudent";

    public final static String ADD_MESSAGE_QUERY="INSERT INTO "+TABLE_NAME+" ("+COLUMN_SENDER_ID+","+
            COLUMN_COURSE_ID+","+COLUMN_TEXT+","+COLUMN_IMAGE+","+COLUMN_SENT_AT+","+COLUMN_IS_STUDENT+") VALUES (?,?,?,?,?,?);";
    public final static String GET_MESSAGES_BY_COURSE_ID="SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_COURSE_ID+" = ?;";
    public final static String GET_STUDENT_MESSAGES = "SELECT * FROM " + TABLE_NAME + " INNER JOIN "+ StudentTable.TABLE_NAME
            +" ON " + TABLE_NAME + "." + COLUMN_SENDER_ID +" = "+ StudentTable.TABLE_NAME+"."+StudentTable.COLUMN_REGISTRATION_NUMBER
            +" INNER JOIN "+CoursesTable.TABLE_NAME + " ON "+CoursesTable.TABLE_NAME+"."+CoursesTable.COURSE_ID_COLUMN+" = "+TABLE_NAME + "." +COLUMN_COURSE_ID
            +" WHERE "+MessageTable.TABLE_NAME+"."+COLUMN_COURSE_ID + " = ?";
    public final static String GET_TEACHER_MESSAGES = "SELECT * FROM " + TABLE_NAME + " INNER JOIN "+ TeacherTable.TABLE_NAME
            +" ON " + TABLE_NAME + "." + COLUMN_SENDER_ID +" = "+ TeacherTable.TABLE_NAME+"."+TeacherTable.COLUMN_TEACHER_ID
            +" INNER JOIN "+CoursesTable.TABLE_NAME + " ON "+CoursesTable.TABLE_NAME +"."+CoursesTable.COURSE_ID_COLUMN+" = "+TABLE_NAME + "." +COLUMN_COURSE_ID
            +" WHERE "+MessageTable.TABLE_NAME+"."+COLUMN_COURSE_ID + " = ?";
    public  final static String GET_NOTIFICATION="SELECT * FROM "+ TABLE_NAME+" INNER JOIN "+CoursesTable.TABLE_NAME+" ON "+ TABLE_NAME+"."
            +COLUMN_COURSE_ID+" = "+CoursesTable.TABLE_NAME+"."+CoursesTable.COURSE_ID_COLUMN + " WHERE "+TABLE_NAME+"."+COLUMN_COURSE_ID
            +" IN ( SELECT "+COLUMN_COURSE_ID+ " FROM "+EnrollmentTable.TABLE_NAME+" WHERE "+EnrollmentTable.COLUMN_REGISTGRATION_NO+" = ?) AND "
            +COLUMN_SENDER_ID+" IS NULL ;";
}
