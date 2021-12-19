package request;

import java.io.File;

public class TeacherChangeProfilePictureRequest extends Request {
    private final File image;

    public TeacherChangeProfilePictureRequest(File image) {
        this.image = image;
    }

    public File getImage() {
        return image;
    }
}
