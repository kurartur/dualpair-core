package lt.dualpair.core.match;

import lt.dualpair.core.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "match_parties")
public class MatchParty {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Response response = Response.UNDEFINED;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "response_time")
    private Date responseDate;

    public MatchParty() {}

    public MatchParty(Match match, User user) {
        this.match = match;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
        responseDate = new Date();
    }
}