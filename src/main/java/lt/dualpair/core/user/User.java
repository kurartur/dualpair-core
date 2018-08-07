package lt.dualpair.core.user;

import lt.dualpair.core.photo.Photo;
import lt.dualpair.core.socionics.Sociotype;
import org.apache.commons.lang3.Validate;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final int NAME_LENGTH = 100;
    private static final int DESCRIPTION_LENGTH = 255;
    public static final int MAX_NUMBER_OF_PHOTOS = 6;

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date dateUpdated;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private Set<UserAccount> userAccounts;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "users_sociotypes", joinColumns = {
            @JoinColumn(name = "user_id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "sociotype_id",
                    nullable = false, updatable = false) })
    private Set<Sociotype> sociotypes = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private SearchParameters searchParameters = new SearchParameters();

    private String name;

    @Embedded
    private AgeInfo ageInfo = new AgeInfo(null);

    private Gender gender;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<Photo> photos = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<UserLocation> locations = new ArrayList<>();

    @Column(name = "relationship_status")
    private RelationshipStatus relationshipStatus = RelationshipStatus.NONE;

    @ElementCollection(targetClass = PurposeOfBeing.class)
    @CollectionTable(name = "user_purposes_of_being", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "purpose", nullable = false)
    private Set<PurposeOfBeing> purposesOfBeing = new HashSet<>();

    public User() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date date_updated) {
        this.dateUpdated = date_updated;
    }

    public Set<UserAccount> getUserAccounts() {
        return Collections.unmodifiableSet(userAccounts);
    }

    public void setUserAccounts(Set<UserAccount> userAccounts) {
        this.userAccounts = userAccounts;
    }

    public void addUserAccount(UserAccount userAccount) {
        Assert.notNull(userAccount);
        userAccounts.add(userAccount);
    }

    public void removeAccount(UserAccount.Type accountType) {
        userAccounts.removeIf(userAccount -> userAccount.getAccountType() == accountType);
    }

    public Set<Sociotype> getSociotypes() {
        return Collections.unmodifiableSet(sociotypes);
    }

    public Sociotype getRandomSociotype() {
        if (sociotypes.size() == 1) {
            return sociotypes.iterator().next();
        } else {
            return new ArrayList<>(sociotypes).get((Math.random() <= 0.5) ? 1 : 2);
        }
    }

    public void setSociotypes(Set<Sociotype> sociotypes) {
        Validate.notNull(sociotypes, "Sociotypes are mandatory");
        if (sociotypes.size() > 2 || sociotypes.size() == 0) {
            throw new IllegalArgumentException("Invalid sociotype code count. Must be 1 or 2");
        }
        this.sociotypes = sociotypes;
    }

    public SearchParameters getSearchParameters() {
        return searchParameters;
    }

    public void setSearchParameters(SearchParameters searchParameters) {
        this.searchParameters = searchParameters;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        Assert.hasText(name);
        if (name.length() > NAME_LENGTH) {
            throw new IllegalArgumentException("Name length can't be greater than " + NAME_LENGTH);
        }
        this.name = name;
    }

    public Integer getAge() {
        return ageInfo == null ? null : ageInfo.getAge();
    }

    public Date getDateOfBirth() {
        return ageInfo == null ? null : ageInfo.getDateOfBirth();
    }

    public void setDateOfBirth(Date dateOfBirth) {
        Assert.notNull(dateOfBirth);
        this.ageInfo = new AgeInfo(dateOfBirth);
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (description.length() > DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException("Description length can't be greater than " + DESCRIPTION_LENGTH);
        }
        this.description = description;
    }

    public List<Photo> getPhotos() {
        return Collections.unmodifiableList(photos);
    }

    public void deletePhoto(Photo photo) {
        photos.remove(photo);
    }

    public void addPhoto(Photo photo) {
        Assert.isTrue(photos.size() < MAX_NUMBER_OF_PHOTOS, "User can have max " + MAX_NUMBER_OF_PHOTOS + " photos.");
        photos.add(photo);
    }

    public void setPhotos(List<Photo> photos) {
        Assert.isTrue(photos.size() <= MAX_NUMBER_OF_PHOTOS, "User can have max " + MAX_NUMBER_OF_PHOTOS + " photos.");
        this.photos.clear();
        this.photos.addAll(photos);
    }

    public List<UserLocation> getLocations() {
        return locations;
    }

    public UserLocation getRecentLocation() {
        return locations.isEmpty() ? null : locations.get(locations.size() - 1);
    }

    public void addLocation(UserLocation location, int locationCount) {
        locations.removeIf(loc -> locations.indexOf(loc) <= (locations.size() - locationCount));
        locations.add(location);
    }

    public RelationshipStatus getRelationshipStatus() {
        return relationshipStatus;
    }

    public void setRelationshipStatus(RelationshipStatus relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }

    public Set<PurposeOfBeing> getPurposesOfBeing() {
        return purposesOfBeing;
    }

    public void setPurposesOfBeing(Set<PurposeOfBeing> purposesOfBeing) {
        this.purposesOfBeing = purposesOfBeing;
    }

}
