package request;

public class ChangeTeacherProfilePicRequest extends Request {
    private byte[] image;

    public ChangeTeacherProfilePicRequest(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
}
