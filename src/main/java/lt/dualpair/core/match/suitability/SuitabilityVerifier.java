package lt.dualpair.core.match.suitability;

import lt.dualpair.core.user.SearchParameters;
import lt.dualpair.core.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SuitabilityVerifier {

    private AgeVerifier ageVerifier;
    private GenderVerifier genderVerifier;

    @Autowired
    public SuitabilityVerifier(AgeVerifier ageVerifier, GenderVerifier genderVerifier) {
        this.ageVerifier = ageVerifier;
        this.genderVerifier = genderVerifier;
    }

    public boolean verify(VerificationContext context1, VerificationContext context2) {

        User user1 = context1.getUser();
        User user2 = context2.getUser();

        SearchParameters searchParameters1 = user1.getSearchParameters();
        SearchParameters searchParameters2 = user2.getSearchParameters();

        if (!ageVerifier.verify(user1.getAge(), searchParameters2.getMinAge(), searchParameters2.getMaxAge())
                || !ageVerifier.verify(user2.getAge(), searchParameters1.getMinAge(), searchParameters1. getMaxAge())) {
            return false;
        }

        if (!genderVerifier.verify(user1.getGender(), searchParameters2.getSearchGenders())
                || !genderVerifier.verify(user2.getGender(), searchParameters1.getSearchGenders())) {
            return false;
        }

        return false;
    }

}
