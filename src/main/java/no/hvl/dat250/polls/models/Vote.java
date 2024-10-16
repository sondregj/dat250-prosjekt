package no.hvl.dat250.polls.models;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

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
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(precision = 8)
    private Instant publishedAt;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vote_option_id")
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
