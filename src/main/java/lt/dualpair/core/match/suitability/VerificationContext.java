package lt.dualpair.core.match.suitability;

import lt.dualpair.core.user.User;

public class VerificationContext {

    private User user;

    public VerificationContext(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
