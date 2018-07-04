package lt.dualpair.core.user;

import lt.dualpair.core.location.Location;
import org.springframework.util.Assert;

import java.util.Set;

public class UserRequestBuilder {

    private UserRequest userRequest = new UserRequest();

    public UserRequestBuilder(User user) { // TODO
        userRequest.setUser(user);
    }

    public static UserRequestBuilder findFor(User user) {
        Assert.notNull(user);
        Assert.notEmpty(user.getSociotypes());
        Assert.notNull(user.getAge());
        Assert.notNull(user.getGender());

        UserRequestBuilder builder = new UserRequestBuilder(user);

        UserLocation userLocation = user.getRecentLocation();
        Assert.notNull(userLocation);

        Location location = userLocation.getLocation();
        builder.location(location.getLatitude(), location.getLongitude(), location.getCountryCode());

        return builder;
    }

    public UserRequestBuilder ageRange(int minAge, int maxAge) {
        if (minAge < 0 || maxAge < 0) {
            throw new IllegalArgumentException("Age can't be negative");
        }
        if (minAge > maxAge) {
            throw  new IllegalArgumentException("Min age can't be higher than max age");
        }
        userRequest.setMinAge(minAge);
        userRequest.setMaxAge(maxAge);
        return this;
    }

    public UserRequestBuilder genders(Set<Gender> genders) {
        Assert.notEmpty(genders);
        Assert.noNullElements(genders.toArray());
        userRequest.setGenders(genders);
        return this;
    }

    public UserRequestBuilder location(double latitude, double longitude, String countryCode) {
        Assert.notNull(countryCode);
        userRequest.setLatitude(latitude);
        userRequest.setLongitude(longitude);
        userRequest.setCountryCode(countryCode);
        return this;
    }

    public UserRequestBuilder excludeOpponents(Set<Long> opponentIds) {
        Assert.notEmpty(opponentIds);
        Assert.noNullElements(opponentIds.toArray());
        userRequest.setExcludedOpponentIds(opponentIds);
        return this;
    }

    public UserRequestBuilder apply(SearchParameters searchParameters) {
        Assert.notNull(searchParameters);
        ageRange(searchParameters.getMinAge(), searchParameters.getMaxAge());
        genders(searchParameters.getSearchGenders());
        return this;
    }

    public UserRequest build() {
        return userRequest;
    }

}
