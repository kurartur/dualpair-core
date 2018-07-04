package lt.dualpair.core.match;

import lt.dualpair.core.user.UserRequest;

public interface MatchFinder {

    Match findOne(UserRequest userRequest);

}
