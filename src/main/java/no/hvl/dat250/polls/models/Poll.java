package no.hvl.dat250.polls.models;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 * Poll
 */
@Entity
public class Poll {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;

    @ManyToOne
    private User creator;

    @OneToMany(mappedBy = "poll")
    private List<VoteOption> voteOptions; 

    public Poll(){}

    public Poll(String question, Instant publishedAt, Instant validUntil){
        this.question = question;
        this.publishedAt = publishedAt;
        this.validUntil = validUntil;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<VoteOption> getVoteOptions() {
        return voteOptions;
    }

    public void setVoteOptions(List<VoteOption> voteOptions) {
        this.voteOptions = voteOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Poll poll = (Poll) o;

        // Compare id as the unique identifier
        if (id != null ? !id.equals(poll.id) : poll.id != null) return false;
        // Optionally compare other important fields like question, publishedAt, and validUntil
        if (question != null ? !question.equals(poll.question) : poll.question != null) return false;
        if (publishedAt != null ? !publishedAt.equals(poll.publishedAt) : poll.publishedAt != null) return false;
        return validUntil != null ? validUntil.equals(poll.validUntil) : poll.validUntil == null;
    }

    @Override
    public String toString(){
        return "Id: " + this.id + " Question: " + this.question + " Creator: " + this.creator +
                " VoteOptions: " + this.voteOptions + " Lagd: " + this.publishedAt + " Gyldig til: "
                + this.validUntil;
    }
}