package lt.dualpair.core.user;

import lt.dualpair.core.location.Location;

import javax.persistence.*;

@Entity
@Table(name = "user_locations")
public class UserLocation {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private Location location;

    private UserLocation() {}

    public UserLocation(User user, Double latitude, Double longitude, String countryCode, String city) {
        this.user = user;
        this.location = new Location(latitude, longitude, countryCode, city);
    }

    public Location getLocation() {
        return location;
    }

    public Double getLatitude() {
        return location.getLatitude();
    }

    public Double getLongitude() {
        return location.getLongitude();
    }

    public String getCountryCode() {
        return location.getCountryCode();
    }

    public String getCity() {
        return location.getCity();
    }
}
