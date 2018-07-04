package lt.dualpair.core.user;

import lt.dualpair.core.socionics.SociotypeRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public class DefaultUserFinderTest {

    private JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private SociotypeRepository sociotypeRepository = mock(SociotypeRepository.class);
    private DefaultUserFinder finder;

    @Before
    public void setUp() throws Exception {
        finder = new DefaultUserFinder(jdbcTemplate, userRepository, sociotypeRepository);
    }

    @Test
    public void findOne_whenRequestIsNull_exceptionThrown() {
        try {
            finder.findOne(null);
            fail();
        } catch (IllegalArgumentException iae) {}
    }
}