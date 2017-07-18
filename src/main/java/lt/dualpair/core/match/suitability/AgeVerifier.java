package lt.dualpair.core.match.suitability;

import org.springframework.stereotype.Component;

@Component
public class AgeVerifier {

    public boolean verify(int age, int minAge, int maxAge) {
        return age >= minAge && age <= maxAge;
    }

}
