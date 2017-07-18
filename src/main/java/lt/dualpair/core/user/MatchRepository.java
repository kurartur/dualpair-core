package lt.dualpair.core.user;

import lt.dualpair.core.match.Match;
import lt.dualpair.core.match.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MatchRepository extends CrudRepository<Match, Long> {

    @Query("select mp.match from MatchParty mp where mp.match.id = ?2 and mp.user.id = ?1")
    Optional<Match> findOneByUser(Long userId, Long matchId);

    @Query("select mp.match from MatchParty mp where mp.user = ?1 and mp.response = ?2")
    Set<Match> findByUser(User user, Response response);

    @Query("select mp.match from MatchParty mp where mp.user = ?1")
    Set<Match> findForPossibleRemoval(User user);

    @Query(" select m from Match m, MatchParty mp1, MatchParty mp2 " +
            "where mp1.user = ?1 and mp2.user <> ?1 and mp1.match = mp2.match " +
            "and m = mp1.match and m = mp2.match " +
            "and mp1.response = lt.dualpair.core.match.Response.YES and mp2.response = lt.dualpair.core.match.Response.YES " +
            "and mp1.match.dateCreated <= ?2 " +
            "order by mp1.match.dateBecameMutual desc")
    Page<Match> findMutual(User user, Date createdBefore, Pageable pageable);

    @Query(" select m from Match m, MatchParty mp1, MatchParty mp2 " +
            "where mp1.user = ?1 and mp2.user <> ?1 and mp1.match = mp2.match " +
            "and m = mp1.match and m = mp2.match " +
            "and mp1.response <> lt.dualpair.core.match.Response.UNDEFINED " +
            "and mp1.match.dateCreated <= ?2 " +
            "order by mp1.responseDate desc")
    Page<Match> findReviewed(User user, Date createdBefore, Pageable pageable);

    @Query(" select mp1.match from MatchParty mp1, MatchParty mp2 " +
            "where mp1.user.id = ?1 and mp2.user.id <> ?1 and mp1.match = mp2.match " +
            "and mp1.response = lt.dualpair.core.match.Response.YES and mp2.response = lt.dualpair.core.match.Response.YES " +
            "and mp1.match.id = ?2")
    Optional<Match> findOneMutualByUser(Long userId, Long matchId);

    @Query("" +
            "select m from Match m, MatchParty mp1, MatchParty mp2 " +
            "where m = mp1.match and mp1.user = ?1 " +
            "   and m = mp2.match and mp2.user <> ?1 and mp2.user.id not in ?2 " +
            "   and (mp2.response = lt.dualpair.core.match.Response.UNDEFINED or mp2.response = lt.dualpair.core.match.Response.YES) and mp1.response = lt.dualpair.core.match.Response.UNDEFINED ")
    Set<Match> findNotReviewed(User user, List<Long> excludeOpponents);

}