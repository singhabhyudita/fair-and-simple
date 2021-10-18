package request;
import java.io.FileInputStream;
import java.io.Serializable;

public class ChangeProfilePicRequest extends Request implements Serializable {
    private FileInputStream fileInputStream;

    public ChangeProfilePicRequest(FileInputStream fileInputStream){
        this.fileInputStream = fileInputStream;
    }

    public FileInputStream getFileInputStream() {
        return fileInputStream;
    }
}