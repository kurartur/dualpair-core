package lt.dualpair.core.match;

import lt.dualpair.core.user.UserTestUtils;

public class MatchTestUtils {

    public static Match createMatch() {
        Match match = new Match();
        match.setId(1L);
        match.setMatchParties(MatchPartyTestUtils.createMatchParty(1L, UserTestUtils.createUser(1L), Response.UNDEFINED),
                MatchPartyTestUtils.createMatchParty(2L, UserTestUtils.createUser(2L), Response.UNDEFINED));
        return match;
    }

    public static Match createMatch(Long id, MatchParty party1, MatchParty party2) {
        Match match = new Match();
        match.setId(id);
        match.setMatchParties(party1, party2);
        party1.setMatch(match);
        party2.setMatch(match);
        return match;
    }

}
