package lt.dualpair.core.location;

public class LocationProviderException extends Exception {

    public LocationProviderException(String message, Throwable cause) {
        super(message, cause);
    }

    public LocationProviderException(String message) {
        super(message);
    }
}
