package lt.dualpair.core.user;

import java.util.Optional;

public interface UserFinder {

    Optional<User> findOne(UserRequest request);

}
