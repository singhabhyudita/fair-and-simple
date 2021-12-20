package table;

public class ExamTable {
    public static final String TABLE_NAME = "exam";
    public static final String EXAM_ID_COLUMN = "examID";
    public static final String COURSE_ID_COLUMN = "courseID";
    public static final String COURSE_NAME_COLUMN = "courseID";
    public static final String TEACHER_ID_COLUMN = "teacherID";
    public static final String PROCTOR_ID_COLUMN = "proctorID";
    public static final String TITLE_COLUMN = "title";
    public static final String DESCRIPTION_COLUMN = "description";
    public static final String MAXIMUM_MARKS_COLUMN = "maximumMarks";
    public static final String START_TIME_COLUMN = "startTime";
    public static final String END_TIME_COLUMN = "endTime";

    public static final String GET_CLASHING_EXAMS_BY_TEACHER = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + TEACHER_ID_COLUMN + " = ? AND NOT (" + START_TIME_COLUMN + " > ? OR "
            + END_TIME_COLUMN + " < ?);";
    public static final String ADD_EXAM_DETAILS = "INSERT INTO " + TABLE_NAME + " ("
            + COURSE_ID_COLUMN + ", " + PROCTOR_ID_COLUMN + ", "
            + TITLE_COLUMN + ", " + DESCRIPTION_COLUMN + ", " + MAXIMUM_MARKS_COLUMN + ", "
            + START_TIME_COLUMN + ", " + END_TIME_COLUMN + ", " + TEACHER_ID_COLUMN + ") VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
    public static final String GET_EXAM_ID = "SELECT " + EXAM_ID_COLUMN + " FROM " + TABLE_NAME
            + " WHERE " + TEACHER_ID_COLUMN + " = ? AND " + START_TIME_COLUMN + " = ? AND "
            + END_TIME_COLUMN + " = ?;";
    public static final String DELETE_EXAM_BY_ID = "DELETE FROM " + TABLE_NAME + "WHERE "
            + EXAM_ID_COLUMN + " = ?;";
    public static final String GET_EXAM_BY_COURSE_ID= "SELECT * FROM "+ TABLE_NAME+" WHERE "+ COURSE_ID_COLUMN+" =?;";
    public static final String GET_EXAM_BY_TEACHER_ID = "SELECT * FROM " + TABLE_NAME
            + " INNER JOIN " + CoursesTable.TABLE_NAME + " ON "
            + TABLE_NAME + "." + COURSE_ID_COLUMN + " = "+CoursesTable.TABLE_NAME +"." + CoursesTable.COURSE_ID_COLUMN
            + " WHERE " + TABLE_NAME + "." + TEACHER_ID_COLUMN + " = ?";
    public static final String GET_EXAM_BY_EXAM_ID = "SELECT * FROM " + TABLE_NAME + " WHERE " + EXAM_ID_COLUMN +
            " = ?;";
    public static final String GET_UPCOMING_EXAMS_STUDENT = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + COURSE_ID_COLUMN +" IN (" + "SELECT " + COURSE_ID_COLUMN + " FROM " + EnrollmentTable.TABLE_NAME
            + " WHERE " + EnrollmentTable.COLUMN_REGISTGRATION_NO + " = ? ) AND " + START_TIME_COLUMN + " >= ?;";
    public static final String GET_EXAMS_HISTORY_STUDENT = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + COURSE_ID_COLUMN +" IN (" + "SELECT " + COURSE_ID_COLUMN + " FROM " + EnrollmentTable.TABLE_NAME
            + " WHERE " + EnrollmentTable.COLUMN_REGISTGRATION_NO + " = ? ) AND " + START_TIME_COLUMN + " < ?;";
    public static final String GET_PROCTORING_DUTY_BY_TEACHER_ID = "SELECT * FROM " + TABLE_NAME
            + " WHERE " + PROCTOR_ID_COLUMN + " = ?;";
//    public static final String GET_EXAM_BY_TEACHER_ID = "SELECT * FROM " + TABLE_NAME + " WHERE "
//            + TEACHER_ID_COLUMN + " = ?";
}
