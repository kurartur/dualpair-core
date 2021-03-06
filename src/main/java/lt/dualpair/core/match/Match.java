package lt.dualpair.core.match;

import org.springframework.hateoas.Identifiable;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Entity
@Table(name = "matches")
public class Match implements Serializable, Identifiable<Long> {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    /*@ManyToOne
    @JoinColumn(name = "relation_type_id")
    private RelationType relationType;*/

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL)
    private Set<MatchParty> matchParties = new HashSet<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "time")
    private Date date;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /*public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
    }*/

    public Set<MatchParty> getMatchParties() {
        return matchParties;
    }

    public void setMatchParties(MatchParty firstParty, MatchParty secondParty) {
        Assert.notNull(firstParty);
        Assert.notNull(secondParty);
        matchParties.clear();
        matchParties.add(firstParty);
        matchParties.add(secondParty);
    }

    public MatchParty getMatchParty(Long userId) {
        Assert.notNull(userId);
        Iterator<MatchParty> matchPartyIterator = matchParties.iterator();
        while (matchPartyIterator.hasNext()) {
            MatchParty matchParty = matchPartyIterator.next();
            if (matchParty.getUser().getId().equals(userId))
                return matchParty;
        }
        return null;
    }

    public MatchParty getOppositeMatchParty(Long userId) {
        Assert.notNull(userId);
        Iterator<MatchParty> matchPartyIterator = matchParties.iterator();
        while (matchPartyIterator.hasNext()) {
            MatchParty matchParty = matchPartyIterator.next();
            if (!matchParty.getUser().getId().equals(userId))
                return matchParty;
        }
        return null;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
