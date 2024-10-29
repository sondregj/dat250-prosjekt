package no.hvl.dat250.polls.mappers;

import no.hvl.dat250.polls.dto.VoteOptionDTO;
import no.hvl.dat250.polls.models.VoteOption;

public class VoteOptionMapper {

    public static VoteOptionDTO toDTO(VoteOption voteOption) {
        if (voteOption == null) {
            return null;
        }

        VoteOptionDTO dto = new VoteOptionDTO();
        dto.setId(voteOption.getId());
        dto.setCaption(voteOption.getCaption());
        dto.setPresentationOrder(voteOption.getPresentationOrder());

        return dto;
    }

    public static VoteOption toEntity(VoteOptionDTO dto) {
        if (dto == null) {
            return null;
        }

        VoteOption voteOption = new VoteOption();
        voteOption.setId(dto.getId());
        voteOption.setCaption(dto.getCaption());
        voteOption.setPresentationOrder(dto.getPresentationOrder());

        // Note: You should set the poll separately, as it may require fetching from the database.

        return voteOption;
    }
}
