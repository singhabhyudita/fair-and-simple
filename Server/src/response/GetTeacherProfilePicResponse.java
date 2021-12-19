package response;

import javax.swing.*;

public class GetTeacherProfilePicResponse extends Response {
    private ImageIcon imageIcon;

    public GetTeacherProfilePicResponse(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }
}
