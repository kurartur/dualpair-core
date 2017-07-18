package lt.dualpair.core.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface UserRepository extends Repository<User, Long>, CustomUserRepository {

    Optional<User> findById(Long id);

    <S extends User> S save(S entity);

    @Query("select u from User u join u.userAccounts ua where ua.accountId = ?1 and ua.accountType = ?2")
    Optional<User> findByAccountId(String id, UserAccount.Type type);

}
