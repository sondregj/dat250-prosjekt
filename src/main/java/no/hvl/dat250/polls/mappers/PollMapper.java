
package no.hvl.dat250.polls.mappers;

import java.util.ArrayList;
import java.util.List;

import no.hvl.dat250.polls.dto.PollDTO;
import no.hvl.dat250.polls.dto.VoteOptionDTO;
import no.hvl.dat250.polls.models.Poll;
import no.hvl.dat250.polls.models.VoteOption;

/**
 * PollMapper
 */
public class PollMapper {

    public static PollDTO toDTO(Poll poll) {
        if (poll == null) {
            return null;
        }

        PollDTO dto = new PollDTO();
        dto.setId(poll.getId());
        dto.setQuestion(poll.getQuestion());
        dto.setPublishedAt(poll.getPublishedAt());
        dto.setValidUntil(poll.getValidUntil());
        dto.setCreatorUsername(poll.getCreator() != null ? poll.getCreator().getUsername() : null);

        if (poll.getVoteOptions() != null) {
            List<VoteOptionDTO> voteOptionDTOs = new ArrayList<>();
            for (VoteOption option : poll.getVoteOptions()) {
                voteOptionDTOs.add(VoteOptionMapper.toDTO(option));
            }
            dto.setVoteOptions(voteOptionDTOs);
        }

        return dto;
    }

    public static Poll toEntity(PollDTO dto) {
        if (dto == null) {
            return null;
        }

        Poll poll = new Poll();
        poll.setId(dto.getId());
        poll.setQuestion(dto.getQuestion());
        poll.setPublishedAt(dto.getPublishedAt());
        poll.setValidUntil(dto.getValidUntil());

        // Note: You should set the creator separately, as it may require fetching from the database.

        if (dto.getVoteOptions() != null) {
            List<VoteOption> voteOptions = new ArrayList<>();
            for (VoteOptionDTO optionDTO : dto.getVoteOptions()) {
                VoteOption option = VoteOptionMapper.toEntity(optionDTO);
                option.setPoll(poll); // Set back-reference
                voteOptions.add(option);
            }
            poll.setVoteOptions(voteOptions);
        }

        return poll;
    }
}

