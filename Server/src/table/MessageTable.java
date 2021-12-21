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

}
