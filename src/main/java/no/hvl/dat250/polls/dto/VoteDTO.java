package no.hvl.dat250.polls.dto;

import java.time.Instant;

/**
 * VoteDTO
 */
public class VoteDTO {

    private Long id;
    private Instant publishedAt;
    private Long userId;
    private Long voteOptionId;

    public VoteDTO(){}

    public VoteDTO(Long id, Instant publishedAt, Long userId, Long voteOptionId){
        this.id = id;
        this.publishedAt = publishedAt;
        this.userId = userId;
        this.voteOptionId = voteOptionId;
    }

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
        this.publishedAt = publishedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getVoteOptionId() {
        return voteOptionId;
    }

    public void setVoteOptionId(Long voteOptionId) {
        this.voteOptionId = voteOptionId;
    }
}
