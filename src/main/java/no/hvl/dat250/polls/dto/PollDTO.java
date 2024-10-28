package no.hvl.dat250.polls.dto;

import java.time.Instant;
import java.util.List;

/**
 * PollDTO
 */
public class PollDTO {
    
    private Long id;
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private String creatorUsername;
    private List<VoteOptionDTO> voteOptions;

    public PollDTO(Long id, String question, Instant publishedAt, Instant validUntil, 
                   String creatorUsername, List<VoteOptionDTO> voteOptions){
        this.id = id;
        this.question = question;
        this.publishedAt = publishedAt;
        this.validUntil = validUntil;
        this.creatorUsername = creatorUsername;
        this.voteOptions = voteOptions;
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

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public List<VoteOptionDTO> getVoteOptions() {
        return voteOptions;
    }

    public void setVoteOptions(List<VoteOptionDTO> voteOptions) {
        this.voteOptions = voteOptions;
    }
}
