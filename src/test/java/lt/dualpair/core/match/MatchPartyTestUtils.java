package lt.dualpair.core.match;

import lt.dualpair.core.user.User;

public class MatchPartyTestUtils {

    public static MatchParty createMatchParty(Long id, User user) {
        MatchParty matchParty = new MatchParty();
        matchParty.setId(id);
        matchParty.setUser(user);
        return matchParty;
    }

}
