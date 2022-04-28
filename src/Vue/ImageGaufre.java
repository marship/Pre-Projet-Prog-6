package Vue;

import java.io.InputStream;

public abstract class ImageGaufre {
    
    static ImageGaufre getImageGaufre(InputStream inputStream) {
        return new ImageGaufreSwing(inputStream);
    }

    abstract <E> E image();
}
