package table;

public class CoursesTable {
    public static final String TABLE_NAME = "course";
    public static final String COURSE_ID_COLUMN = "courseID";
    public static final String COURSE_NAME_COLUMN = "courseName";
    public static final String COURSE_DESC_COLUMN = "courseDescription";
    public static final String COURSE_CODE_COLUMN = "courseCode";
    public static final String TEACHER_ID_COLUMN = "teacherID";

    public static final String ADD_COURSE_QUERY = "INSERT INTO "+ TABLE_NAME
            + " (" + COURSE_NAME_COLUMN + ", " + COURSE_DESC_COLUMN +", " + COURSE_CODE_COLUMN + ", " + TEACHER_ID_COLUMN
            + ") VALUES (?, ?, ?, ?)";
    public static final String MODIFY_COURSE_CODE = "UPDATE " + TABLE_NAME
            + " SET " + COURSE_CODE_COLUMN + " = ? "
            + "WHERE " + COURSE_ID_COLUMN + " = ?";
    public static final String GET_COURSES_BY_TEACHER_ID = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + TEACHER_ID_COLUMN + " = ?";
    public static final String GET_COURSES_BY_COURSE_CODE = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + COURSE_CODE_COLUMN + " = ?";
    public static final String GET_COURSE_ID_BY_COURSE_CODE="SELECT "+COURSE_ID_COLUMN +" FROM "+TABLE_NAME
            + " WHERE "+COURSE_CODE_COLUMN +" =?;";
    public static final String GET_COURSES_BY_COURSE_ID = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + COURSE_ID_COLUMN + " = ?";
    public static final String GET_COURSE_NAME_BY_COURSE_ID = "SELECT "+ COURSE_NAME_COLUMN +" FROM "+ TABLE_NAME+" WHERE "
            +COURSE_ID_COLUMN+" =?;";
    public static final String GET_TEACHER_ID_BY_COURSE_ID = "SELECT "+ TEACHER_ID_COLUMN +" FROM "+ TABLE_NAME+" WHERE "
            +COURSE_ID_COLUMN+" =?;";
}
