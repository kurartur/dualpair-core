package lt.dualpair.core.match;

import lt.dualpair.core.user.User;

public class MatchPartyTestUtils {

    public static MatchParty createMatchParty(Long id, User user, Response response) {
        MatchParty matchParty = new MatchParty();
        matchParty.setId(id);
        matchParty.setUser(user);
        matchParty.setResponse(response);
        return matchParty;
    }

}
