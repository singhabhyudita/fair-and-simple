package table;

public class TeacherTable {
    public static final String TABLE_NAME="teacher";
    public static final String COLUMN_TEACHER_ID="teacherID";
    public static final String COLUMN_FIRST_NAME="firstName";
    public static final String COLUMN_LAST_NAME="lastName";
    public static final String COLUMN_EMAIL_ID="emailID";
    public static final String COLUMN_PASSWORD="password";
    public static final String COLUMN_LAST_ACTIVE="lastActive";
    public static final String COLUMN_SALT="salt";
    public static final String COLUMN_PROFILE_PIC="profilePic";

    public static final String QUERY_LOGIN="SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_TEACHER_ID+" =? AND "+ COLUMN_PASSWORD+"= ?;";
    public static final String QUERY_UPDATE_LAST_ACTIVE="UPDATE "+TABLE_NAME+ " SET "+COLUMN_LAST_ACTIVE+" = CURRENT_TIMESTAMP WHERE "+COLUMN_TEACHER_ID+" =?;";
    public static final String QUERY_REGISTER="INSERT INTO "+TABLE_NAME+" (" +COLUMN_TEACHER_ID+
            ","+COLUMN_FIRST_NAME+","+COLUMN_LAST_NAME+","+COLUMN_EMAIL_ID+","+COLUMN_PASSWORD+","+COLUMN_PROFILE_PIC+") VALUES "+" (?,?,?,?,?,?);";
    public static final String GET_TEACHER_BY_ID_PASSWORD = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + COLUMN_TEACHER_ID + " = ? AND " + COLUMN_PASSWORD + " = ?;";
    public static final String CHANGE_PASSWORD_QUERY = "UPDATE " + TABLE_NAME
            + " SET " + COLUMN_PASSWORD + " = ? "
            + " WHERE " + COLUMN_TEACHER_ID + " = ?;";
    public static final String GET_TEACHER_NAME_BY_ID="SELECT "+COLUMN_FIRST_NAME+","+COLUMN_LAST_NAME+" FROM "+TABLE_NAME+" WHERE "+ COLUMN_TEACHER_ID+" =?;";
    public static final String CHANGE_PROFILE_PIC_QUERY = "UPDATE "+ TABLE_NAME
            + " SET " + COLUMN_PROFILE_PIC + " = ? "
            + " WHERE " + COLUMN_TEACHER_ID + " = ?;";
    public static final String GET_PROFILE_PIC_BY_TEACHER_ID = "SELECT " + COLUMN_PROFILE_PIC + " FROM " + TABLE_NAME
            + " WHERE " + COLUMN_TEACHER_ID + " = ?;";
}
