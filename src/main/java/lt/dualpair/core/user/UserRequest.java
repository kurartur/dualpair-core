package lt.dualpair.core.user;

import lt.dualpair.core.socionics.RelationType;

import java.util.HashSet;
import java.util.Set;

public class UserRequest {

    private static final int DEFAULT_RADIUS = 300000; // meters

    private User user;

    private RelationType.Code relationType = RelationType.Code.DUAL;

    private int minAge;
    private int maxAge;

    private Set<Gender> genders = new HashSet<>();

    private double latitude;
    private double longitude;
    private String countryCode;
    private int radius = DEFAULT_RADIUS;

    private Set<Long> excludedOpponentIds = new HashSet<>();

    UserRequest() {}

    public Set<Long> getExcludedOpponentIds() {
        return excludedOpponentIds;
    }

    public void setExcludedOpponentIds(Set<Long> excludedOpponentIds) {
        this.excludedOpponentIds = excludedOpponentIds;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public RelationType.Code getRelationType() {
        return relationType;
    }

    public Set<Gender> getGenders() {
        return genders;
    }

    public void setGenders(Set<Gender> genders) {
        this.genders = genders;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
