package lt.dualpair.core.user;

import lt.dualpair.core.photo.Photo;
import lt.dualpair.core.socionics.Sociotype;
import lt.dualpair.core.socionics.SociotypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.*;

@Component
public class FakeUserFinder implements UserFinder {

    private SociotypeRepository sociotypeRepository;
    private UserRepository userRepository;
    private RestOperations restOperations;

    @Override
    public Optional<User> findOne(UserRequest userRequest) {
        Random random = new Random();
        Set<Gender> genders = userRequest.getGenders();
        int index = random.nextInt(genders.size());
        Gender gender = new ArrayList<>(genders).get(index);
        RandomResults randomResults = restOperations.getForObject(buildUrl(gender), RandomResults.class);
        RandomUser randomUser = randomResults.results.get(0);

        User user = new User();
        user.setGender(gender);
        user.setEmail(randomUser.email);
        user.setName(randomUser.name.first.substring(0, 1).toUpperCase() + randomUser.name.first.substring(1));
        user.setDateOfBirth(userRequest.getUser().getDateOfBirth());
        user.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit," +
                " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua." +
                " Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat." +
                " FAKE");

        // sociotypes
        Set<Sociotype> sociotypes = new HashSet<>();
        sociotypes.add(sociotypeRepository.findOppositeByRelationType(
                userRequest.getUser().getRandomSociotype().getCode1(),
                userRequest.getRelationType()));
        user.setSociotypes(sociotypes);

        user.setRelationshipStatus(RelationshipStatus.values()[new Random().nextInt(3)]);
        user.setPurposesOfBeing(getRandomPurposesOfBeing());

        // accounts
        Set<UserAccount> userAccounts = new HashSet<>();
        UserAccount userAccount = new UserAccount(user);
        userAccount.setAccountType(UserAccount.Type.FACEBOOK);
        userAccount.setAccountId(randomUser.email);
        userAccounts.add(userAccount);
        userAccount = new UserAccount(user);
        userAccount.setAccountType(UserAccount.Type.VKONTAKTE);
        userAccount.setAccountId(randomUser.email);
        userAccounts.add(userAccount);
        user.setUserAccounts(userAccounts);

        //location
        UserLocation userLocation = new UserLocation(
                user,
                userRequest.getLatitude(),
                userRequest.getLongitude(),
                userRequest.getCountryCode(),
                randomUser.location.city.substring(0, 1).toUpperCase() + randomUser.location.city.substring(1));
        user.addLocation(userLocation, 1);

        // photos
        Photo photo1 = new Photo();
        photo1.setUser(user);
        photo1.setSourceLink(randomUser.picture.large);
        Photo photo2 = new Photo();
        photo2.setUser(user);
        photo2.setSourceLink(randomUser.picture.large);
        List<Photo> photos = Arrays.asList(photo1, photo2);
        user.setPhotos(photos);

        // search parameters
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setUser(user);
        searchParameters.setMinAge(userRequest.getMinAge());
        searchParameters.setMaxAge(userRequest.getMaxAge());
        searchParameters.setSearchFemale(true);
        searchParameters.setSearchMale(true);
        user.setSearchParameters(searchParameters);

        userRepository.save(user);

        return Optional.of(user);
    }

    private Set<PurposeOfBeing> getRandomPurposesOfBeing() {
        Set<PurposeOfBeing> purposesOfBeing = new HashSet<>();
        for (int i = 0 ; i < PurposeOfBeing.values().length; i++ ) {
            purposesOfBeing.add(PurposeOfBeing.values()[new Random().nextInt(PurposeOfBeing.values().length)]);
        }
        return purposesOfBeing;
    }

    private String buildUrl(Gender gender) {
        return "https://randomuser.me/api/?gender=" + gender.name().toLowerCase();
    }

    @Autowired
    public void setSociotypeRepository(SociotypeRepository sociotypeRepository) {
        this.sociotypeRepository = sociotypeRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRestOperations(RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    public static final class RandomResults {

        public List<RandomUser> results;

    }

    public static final class RandomUser {

        public String gender;
        public RandomUserName name;
        public String email;
        public RandomUserPicture picture;
        public RandomUserLocation location;

    }

    public static final class RandomUserName {

        public String first;

    }

    public static final class RandomUserPicture {

        public String large;

    }

    public static final class RandomUserLocation {

        public String city;
    }

    /*
    * "gender": "male",
      "name": {
        "title": "mr",
        "first": "romain",
        "last": "hoogmoed"
      },
      "location": {
        "street": "1861 jan pieterszoon coenstraat",
        "city": "maasdriel",
        "state": "zeeland",
        "postcode": 69217
      },
      "email": "romain.hoogmoed@example.com",
      "login": {
        "username": "lazyduck408",
        "password": "jokers",
        "salt": "UGtRFz4N",
        "md5": "6d83a8c084731ee73eb5f9398b923183",
        "sha1": "cb21097d8c430f2716538e365447910d90476f6e",
        "sha256": "5a9b09c86195b8d8b01ee219d7d9794e2abb6641a2351850c49c309f1fc204a0"
      },
      "dob": "1983-07-14 07:29:45",
      "registered": "2010-09-24 02:10:42",
      "phone": "(656)-976-4980",
      "cell": "(065)-247-9303",
      "id": {
        "name": "BSN",
        "value": "04242023"
      },
      "picture": {
        "large": "https://randomuser.me/api/portraits/men/83.jpg",
        "medium": "https://randomuser.me/api/portraits/med/men/83.jpg",
        "thumbnail": "https://randomuser.me/api/portraits/thumb/men/83.jpg"
      },
      "nat": "NL"
    * */
}
