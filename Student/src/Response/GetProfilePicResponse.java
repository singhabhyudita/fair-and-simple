package Response;

import javafx.scene.image.Image;
import java.io.Serializable;

public class GetProfilePicResponse extends Response implements Serializable {
    Image image;

    public GetProfilePicResponse(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
