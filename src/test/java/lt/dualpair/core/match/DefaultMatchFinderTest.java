package lt.dualpair.core.match;

import lt.dualpair.core.location.DistanceCalculator;
import lt.dualpair.core.socionics.RelationType;
import lt.dualpair.core.socionics.RelationTypeRepository;
import lt.dualpair.core.socionics.Sociotype;
import lt.dualpair.core.socionics.SociotypeRepository;
import lt.dualpair.core.user.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import java.util.*;

public class DefaultMatchFinderTest {

    private DefaultMatchFinder defaultMatchFinder = new DefaultMatchFinder();
    private UserRepository userRepository = Mockito.mock(UserRepository.class);
    private SociotypeRepository sociotypeRepository = Mockito.mock(SociotypeRepository.class);
    private DistanceCalculator distanceCalculator = Mockito.mock(DistanceCalculator.class);
    private RelationTypeRepository relationTypeRepository = Mockito.mock(RelationTypeRepository.class);

    @Before
    public void setUp() throws Exception {
        defaultMatchFinder.setUserRepository(userRepository);
        defaultMatchFinder.setSociotypeRepository(sociotypeRepository);
        defaultMatchFinder.setDistanceCalculator(distanceCalculator);
        defaultMatchFinder.setRelationTypeRepository(relationTypeRepository);
        RelationType relationType = new RelationType.Builder().id(1).code(RelationType.Code.DUAL).build();
        Mockito.when(relationTypeRepository.findByCode(RelationType.Code.DUAL)).thenReturn(Optional.of(relationType));
    }

    @Test
    public void testFindOne() throws Exception {
        User user = UserTestUtils.createUser(1L, Sociotype.Code1.EII);
        User opponent = UserTestUtils.createUser(2L, Sociotype.Code1.LSE);
        Sociotype pairSociotype = createSociotype(Sociotype.Code1.LSE);
        opponent.addLocation(UserLocationTestUtils.createUserLocation(12, "LT"), 1);
        Set<User> opponents = new HashSet<>(Collections.singletonList(opponent));
        Mockito.when(sociotypeRepository.findOppositeByRelationType(Sociotype.Code1.EII, RelationType.Code.DUAL)).thenReturn(pairSociotype);
        Mockito.when(userRepository.findOpponents(Matchers.any(UserRepositoryImpl.FindOpponentsParams.class))).thenReturn(opponents);
        Mockito.doReturn(300000.0).when(distanceCalculator).calculate(10, 10, 12, 12);
        Match resultMatch = defaultMatchFinder.findOne(new MatchRequestBuilder(user).location(10, 10, "LT").build());
        Assert.assertNotNull(resultMatch);
        Assert.assertEquals(user, resultMatch.getMatchParty(1L).getUser());
        Assert.assertEquals(opponent, resultMatch.getOppositeMatchParty(1L).getUser());
        Assert.assertEquals((Integer) 300000, resultMatch.getDistance());
        Assert.assertEquals(RelationType.Code.DUAL, resultMatch.getRelationType().getCode());
    }

    @Test
    public void testFindOne_tooFar() throws Exception {
        User user = UserTestUtils.createUser(1L, Sociotype.Code1.EII);
        User opponent = UserTestUtils.createUser(2L, Sociotype.Code1.LSE);
        Sociotype pairSociotype = createSociotype(Sociotype.Code1.LSE);
        opponent.addLocation(UserLocationTestUtils.createUserLocation(12, "LT"), 1);
        Set<User> opponents = new HashSet<>(Collections.singletonList(opponent));
        Mockito.when(sociotypeRepository.findOppositeByRelationType(Sociotype.Code1.EII, RelationType.Code.DUAL)).thenReturn(pairSociotype);
        Mockito.when(userRepository.findOpponents(Matchers.any(UserRepositoryImpl.FindOpponentsParams.class))).thenReturn(opponents);
        Mockito.doReturn(300000.1).when(distanceCalculator).calculate(10, 10, 12, 12);
        Match resultMatch = defaultMatchFinder.findOne(new MatchRequestBuilder(user).location(10, 10, "LT").build());
        Assert.assertNull(resultMatch);
    }

    @Test
    public void testFindOne_closest() throws Exception {
        User user = UserTestUtils.createUser(1L, Sociotype.Code1.EII);
        User opponent1 = UserTestUtils.createUser(2L, Sociotype.Code1.LSE);
        User opponent2 = UserTestUtils.createUser(3L, Sociotype.Code1.LSE);
        User opponent3 = UserTestUtils.createUser(4L, Sociotype.Code1.LSE);
        Sociotype pairSociotype = createSociotype(Sociotype.Code1.LSE);
        opponent1.addLocation(UserLocationTestUtils.createUserLocation(12, "LT"), 5);
        opponent2.addLocation(UserLocationTestUtils.createUserLocation(13, "LT"), 5);
        opponent3.addLocation(UserLocationTestUtils.createUserLocation(14, "LT"), 5);
        Set<User> opponents = new LinkedHashSet<>();
        opponents.add(opponent1);
        opponents.add(opponent2);
        opponents.add(opponent3);
        Mockito.when(sociotypeRepository.findOppositeByRelationType(Sociotype.Code1.EII, RelationType.Code.DUAL)).thenReturn(pairSociotype);
        Mockito.when(userRepository.findOpponents(Matchers.any(UserRepositoryImpl.FindOpponentsParams.class))).thenReturn(opponents);
        Mockito.doReturn(300000.0).when(distanceCalculator).calculate(10, 10, 12, 12);
        Mockito.doReturn(299999.7).when(distanceCalculator).calculate(10, 10, 13, 13);
        Mockito.doReturn(299999.8).when(distanceCalculator).calculate(10, 10, 14, 14);
        Match resultMatch = defaultMatchFinder.findOne(new MatchRequestBuilder(user).location(10, 10, "LT").build());
        Assert.assertNotNull(resultMatch);
        Assert.assertEquals(user, resultMatch.getMatchParty(1L).getUser());
        Assert.assertEquals(opponent2, resultMatch.getOppositeMatchParty(1L).getUser());
        Assert.assertEquals((Integer)299999, resultMatch.getDistance());
    }

    @Test
    public void testFindOnee_noMatches() throws Exception {
        User user = UserTestUtils.createUser(1L, Sociotype.Code1.EII);
        Sociotype pairSociotype = createSociotype(Sociotype.Code1.LSE);
        SearchParameters searchParameters = new SearchParameters();
        Mockito.when(sociotypeRepository.findOppositeByRelationType(Sociotype.Code1.EII, RelationType.Code.DUAL)).thenReturn(pairSociotype);
        Mockito.when(userRepository.findOpponents(Matchers.any(UserRepositoryImpl.FindOpponentsParams.class))).thenReturn(new HashSet<>());
        Assert.assertNull(defaultMatchFinder.findOne(new MatchRequestBuilder(user).build()));
    }

    private Sociotype createSociotype(Sociotype.Code1 code1) {
        return new Sociotype.Builder().code1(code1).build();
    }
}