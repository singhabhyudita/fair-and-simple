package response;

import javax.swing.*;
import java.io.Serializable;

public class GetProfilePicResponse extends Response implements Serializable {
    private ImageIcon imageIcon;

    public GetProfilePicResponse(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;
    }

    public ImageIcon getImageIcon() {
        return imageIcon;
    }
}
