package lt.dualpair.core.user;

import lt.dualpair.core.match.Match;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

public interface MatchRepository extends CrudRepository<Match, Long> {

    @Query("select mp.match from MatchParty mp where mp.match.id = ?2 and mp.user.id = ?1")
    Optional<Match> findOneByUser(Long userId, Long matchId);

    @Query("select mp.match from MatchParty mp where mp.user = ?1")
    Set<Match> findForPossibleRemoval(User user);

    @Query(" select m from Match m, MatchParty mp1 " +
            "where mp1.user = ?1 and m = mp1.match " +
            "and mp1.match.date <= ?2 " +
            "order by mp1.match.date desc")
    Page<Match> fetchMatches(User user, Date createdBefore, Pageable pageable);

}
