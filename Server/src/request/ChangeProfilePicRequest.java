package request;
import java.io.Serializable;

public class ChangeProfilePicRequest extends Request implements Serializable {
    private byte[] image;

    public ChangeProfilePicRequest(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
}