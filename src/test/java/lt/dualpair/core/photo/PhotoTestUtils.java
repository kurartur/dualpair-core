package lt.dualpair.core.photo;

import lt.dualpair.core.user.UserAccount;

public class PhotoTestUtils {

    public static Photo createPhoto(UserAccount.Type accountType, String idOnAccount, String url) {
        Photo photo = new Photo();
        photo.setAccountType(accountType);
        photo.setIdOnAccount(idOnAccount);
        photo.setSourceLink(url);
        return photo;
    }

}
