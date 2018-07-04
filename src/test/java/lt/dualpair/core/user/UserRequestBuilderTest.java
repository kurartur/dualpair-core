package lt.dualpair.core.user;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserRequestBuilderTest {

    @Test
    public void testAgeRange() throws Exception {
        UserRequest mr = new UserRequestBuilder(null).ageRange(10, 20).build();
        assertEquals(10, mr.getMinAge());
        assertEquals(20, mr.getMaxAge());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAgeRange_invalidMinAge() throws Exception {
        new UserRequestBuilder(null).ageRange(-1, 20);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAgeRange_invalidMaxAge() throws Exception {
        new UserRequestBuilder(null).ageRange(10, -1);
    }


    @Test(expected = IllegalArgumentException.class)
    public void testAgeRange_invalidAges() throws Exception {
        new UserRequestBuilder(null).ageRange(20, 10);
    }

    @Test
    public void testGenders() throws Exception {
        UserRequest mr = new UserRequestBuilder(null).genders(new HashSet<>(Arrays.asList(Gender.FEMALE, Gender.MALE))).build();
        assertTrue(mr.getGenders().contains(Gender.FEMALE));
        assertTrue(mr.getGenders().contains(Gender.MALE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenders_null() throws Exception {
        new UserRequestBuilder(null).genders(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenders_empty() throws Exception {
        new UserRequestBuilder(null).genders(new HashSet<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenders_nullElements() throws Exception {
        Set<Gender> genders = new HashSet<>();
        genders.add(null);
        new UserRequestBuilder(null).genders(genders);
    }

    @Test
    public void testLocation() throws Exception {
        UserRequest mr = new UserRequestBuilder(null).location(10.0, 11.0, "LT").build();
        assertEquals(10.0, mr.getLatitude(), 0);
        assertEquals(11.0, mr.getLongitude(), 0);
        assertEquals("LT", mr.getCountryCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLocation_nullCountry() throws Exception {
        new UserRequestBuilder(null).location(10.0, 11.0, null);
    }

    @Test
    public void testExcludeOpponents() throws Exception {
        UserRequest mr = new UserRequestBuilder(null).excludeOpponents(new HashSet<>(Arrays.asList(10L, 20L))).build();
        assertEquals(2, mr.getExcludedOpponentIds().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExcludeOpponents_empty() throws Exception {
        new UserRequestBuilder(null).excludeOpponents(new HashSet<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExcludeOpponents_null() throws Exception {
        new UserRequestBuilder(null).excludeOpponents(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExcludeOpponents_nullElements() throws Exception {
        Set<Long> ids = new HashSet<>();
        ids.add(null);
        new UserRequestBuilder(null).excludeOpponents(ids);
    }

    @Test
    public void testApplySearchParameters() throws Exception {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setMinAge(10);
        searchParameters.setMaxAge(20);
        searchParameters.setSearchMale(true);
        searchParameters.setSearchFemale(true);
        UserRequest mr = new UserRequestBuilder(null).apply(searchParameters).build();
        assertEquals(10, mr.getMinAge());
        assertEquals(20, mr.getMaxAge());
        assertTrue(mr.getGenders().contains(Gender.FEMALE));
        assertTrue(mr.getGenders().contains(Gender.MALE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testApplySearchParameters_null() throws Exception {
        new UserRequestBuilder(null).apply(null);
    }
}