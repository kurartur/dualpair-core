package lt.dualpair.core.location;

public abstract class LocationProvider {

    public abstract Location getLocation(double latitude, double longitude) throws LocationProviderException;

}
