package lt.dualpair.core.user;

public class UserLocationTestUtils {

    public static UserLocation createUserLocation(double latLon, String countryCode) {
        return new UserLocation(new User(), latLon, latLon, countryCode, "Vilnius");
    }

}