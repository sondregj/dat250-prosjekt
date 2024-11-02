package no.hvl.dat250.polls.models;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Vote
 */
@Entity
public class Vote implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Instant publishedAt;

    @ManyToOne
    @Column(name="guest_id", nullable = true)
    private String guestId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = true)
    @JsonBackReference(value = "votes-User")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vote_option_id")
    @JsonBackReference(value = "votes-voteOption")
    private VoteOption voteOption;

    public Vote(Instant publishedAt){
        this.publishedAt = publishedAt.atZone(ZoneOffset.UTC).toInstant().truncatedTo(ChronoUnit.SECONDS);
    }


    public Vote() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt.truncatedTo(ChronoUnit.SECONDS);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VoteOption getVoteOption() {
        return voteOption;
    }

    public void setVoteOption(VoteOption voteOption) {
        this.voteOption = voteOption;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

    @JsonProperty("pollId")
    public Long getPollId() {
        return voteOption != null && voteOption.getPoll() != null ? voteOption.getPoll().getId() : null;
    }

    @JsonProperty("pollQuestion")
    public String getPollQuestion() {
        return voteOption != null && voteOption.getPoll() != null ? voteOption.getPoll().getQuestion() : null;
    }

    @JsonProperty("voteOptionId")
    public Long getVoteOptionId() {
        return voteOption != null ? voteOption.getId() : null;
    }

    @JsonProperty("voteOptionCaption")
    public String getVoteOptionCaption() {
        return voteOption != null ? voteOption.getCaption() : null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(id, vote.id) &&
               Objects.equals(publishedAt, vote.publishedAt) &&
               Objects.equals(user, vote.user) &&
               Objects.equals(voteOption, vote.voteOption);
    }

    @Override
    public String toString() {
        return "Vote{" +
            "id=" + id +
            ", publishedAt=" + publishedAt +
            ", user=" + (user != null ? user.getId() : "null") +
            ", voteOption=" + (voteOption != null ? voteOption.getId() : "null") +
            '}';
    }
}
