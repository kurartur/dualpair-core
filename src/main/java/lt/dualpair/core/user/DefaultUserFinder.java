package lt.dualpair.core.user;

import lt.dualpair.core.socionics.Sociotype;
import lt.dualpair.core.socionics.SociotypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DefaultUserFinder implements UserFinder {

    private NamedParameterJdbcTemplate jdbcTemplate;
    private UserRepository userRepository;
    private SociotypeRepository sociotypeRepository;

    @Autowired
    public DefaultUserFinder(JdbcTemplate jdbcTemplate, UserRepository userRepository,
                             SociotypeRepository sociotypeRepository) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.userRepository = userRepository;
        this.sociotypeRepository = sociotypeRepository;
    }

    private static final String SEARCH_QUERY = "" +
            "SELECT u.id FROM users u\n" +
            "LEFT JOIN user_responses ur1 ON ur1.to_user_id = u.id AND ur1.user_id = :userId\n" +
            "LEFT JOIN user_responses ur2 ON ur2.user_id = u.id AND ur2.to_user_id = :userId\n" +
            "INNER JOIN users_sociotypes us ON us.user_id = u.id\n" +
            "INNER JOIN search_parameters sp ON sp.user_id = u.id\n" +
            "INNER JOIN (SELECT ul.*, ( 6371 * acos( cos( radians(:userLatitude) ) * cos( radians( ul.latitude ) ) * cos( radians( ul.longitude ) - radians(:userLongitude) ) + sin( radians(:userLatitude) ) * sin( radians( ul.latitude ) ) ) ) AS distance\n" +
            "   FROM user_locations ul\n" +
            "   LEFT OUTER JOIN user_locations ulm ON ul.id = ulm.id AND ul.id < ulm.id\n" +
            "   WHERE ulm.id IS NULL) ul ON ul.user_id = u.id\n" +
            "WHERE ur1.id is null and (ur2.response <> 'N' or ur2.response is null) and u.id <> :userId\n" +
            "   AND u.age >= :minAge AND u.age <= :maxAge\n" +
            "   AND u.gender IN (:genders)\n" +
            "   AND us.sociotype_id = :sociotypeId\n" +
            "   AND sp.min_age <= :userAge AND sp.max_age >= :userAge\n" +
            "   AND ((sp.search_male = 'Y' AND 'M' = :userGender) OR (sp.search_female = 'Y' AND 'F' = :userGender))\n" +
            "   AND ul.country_code = :country\n" +
            "   AND ul.distance <= :radius\n" +
            "   AND u.id NOT IN (:exclude)\n" +
            "ORDER BY ul.distance DESC";

    @Override
    public Optional<User> findOne(UserRequest request) {
        Assert.notNull(request);

        User user = request.getUser();
        Long userId = user.getId();
        Sociotype sociotype = sociotypeRepository.findOppositeByRelationType(user.getRandomSociotype().getCode1(), request.getRelationType());

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        parameters.addValue("userLatitude", request.getLatitude());
        parameters.addValue("userLongitude", request.getLongitude());
        parameters.addValue("minAge", request.getMinAge());
        parameters.addValue("maxAge", request.getMaxAge());
        parameters.addValue("genders", request.getGenders().stream().map(Gender::getCode).collect(Collectors.toSet()));
        parameters.addValue("sociotypeId", sociotype.getId());
        parameters.addValue("userAge", user.getAge());
        parameters.addValue("userGender", user.getGender().getCode());
        parameters.addValue("country", user.getRecentLocation().getCountryCode());
        parameters.addValue("radius", request.getRadius() / 1000);
        parameters.addValue("exclude", request.getExcludedOpponentIds().isEmpty() ? new HashSet<>(Collections.singletonList(-1)) : request.getExcludedOpponentIds());

        List<Long> ids = jdbcTemplate.queryForList(SEARCH_QUERY, parameters, Long.class);

        if (ids.size() > 0)
            return userRepository.findById(ids.get(0));
        return Optional.empty();
    }
}
