package no.hvl.dat250.polls.mappers;

import no.hvl.dat250.polls.dto.VoteDTO;
import no.hvl.dat250.polls.models.Vote;

/**
 * VoteMapper
 */
public class VoteMapper {

    public VoteDTO toDTO(Vote vote) {
        if (vote == null) {
            return null;
        }

        VoteDTO dto = new VoteDTO();
        dto.setId(vote.getId());
        dto.setPublishedAt(vote.getPublishedAt());
        dto.setUserId(vote.getUser().getId());
        dto.setVoteOptionId(vote.getVoteOptionId());

        return dto;
    }
}
