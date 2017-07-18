package lt.dualpair.core.user;

import java.util.Set;

public interface CustomUserRepository {

    Set<User> findOpponents(UserRepositoryImpl.FindOpponentsParams params);

}
