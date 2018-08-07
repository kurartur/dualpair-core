package lt.dualpair.core.photo;

public class PhotoTestUtils {

    public static Photo createPhoto(String url) {
        Photo photo = new Photo();
        photo.setSourceLink(url);
        return photo;
    }

}
