package lt.dualpair.core.user;

import lt.dualpair.core.match.MatchParty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MatchPartyRepository extends Repository<MatchParty, Long> {

    @Query("select mp from MatchParty mp where mp.id = ?1")
    Optional<MatchParty> findById(Long id);

    MatchParty save(MatchParty matchParty);

}
