package lt.dualpair.core.match;

import lt.dualpair.core.match.suitability.SuitabilityVerifier;
import lt.dualpair.core.match.suitability.VerificationContext;
import lt.dualpair.core.user.MatchRepository;
import lt.dualpair.core.user.User;
import lt.dualpair.core.user.UserTestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RepositoryMatchFinderTest {

    private RepositoryMatchFinder repositoryMatchFinder = new RepositoryMatchFinder();
    private MatchRepository matchRepository = Mockito.mock(MatchRepository.class);
    private SuitabilityVerifier suitabilityVerifier = Mockito.mock(SuitabilityVerifier.class);

    @Before
    public void setUp() throws Exception {
        repositoryMatchFinder.setMatchRepository(matchRepository);
        repositoryMatchFinder.setSuitabilityVerifier(suitabilityVerifier);
    }

    @Test
    public void testFindOne() throws Exception {
        User user = UserTestUtils.createUser();
        List<Long> exclude = Arrays.asList(1L);
        Set<Match> matchSet = new HashSet<>();
        Match match = MatchTestUtils.createMatch(1L,
                MatchPartyTestUtils.createMatchParty(1L, user, Response.UNDEFINED),
                MatchPartyTestUtils.createMatchParty(2L, UserTestUtils.createUser(2L), Response.UNDEFINED));
        matchSet.add(match);
        Mockito.when(matchRepository.findNotReviewed(user, exclude)).thenReturn(matchSet);
        Mockito.when(suitabilityVerifier.verify(Matchers.any(VerificationContext.class), Matchers.any(VerificationContext.class))).thenReturn(true);
        Match resultMatch = repositoryMatchFinder.findOne(new MatchRequestBuilder(user).excludeOpponents(exclude).build());
        Assert.assertEquals(match, resultMatch);
    }

    @Test
    public void testFindOne_notSuitable() throws Exception {
        User user = UserTestUtils.createUser();
        Set<Match> matchSet = new HashSet<>();
        Match match = MatchTestUtils.createMatch(1L,
                MatchPartyTestUtils.createMatchParty(1L, user, Response.UNDEFINED),
                MatchPartyTestUtils.createMatchParty(2L, UserTestUtils.createUser(2L), Response.UNDEFINED));
        matchSet.add(match);
        Mockito.when(matchRepository.findNotReviewed(user, Arrays.asList(-1L))).thenReturn(matchSet);
        Mockito.when(suitabilityVerifier.verify(Matchers.any(VerificationContext.class), Matchers.any(VerificationContext.class))).thenReturn(false);
        Match resultMatch = repositoryMatchFinder.findOne(new MatchRequestBuilder(user).build());
        Assert.assertNull(resultMatch);
        Mockito.verify(matchRepository, Mockito.times(1)).delete(match);
    }
}