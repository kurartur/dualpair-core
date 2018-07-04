package lt.dualpair.core.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserResponseRepository extends Repository<UserResponse, Long> {

    UserResponse save(UserResponse userResponse);

    @Query("select ur from UserResponse ur where ur.user.id = ?1 and ur.toUser.id = ?2")
    Optional<UserResponse> findByParties(Long userId, Long toUserId);
}
