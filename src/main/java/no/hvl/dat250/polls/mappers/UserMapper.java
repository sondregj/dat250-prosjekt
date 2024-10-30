package no.hvl.dat250.polls.mappers;

import no.hvl.dat250.polls.dto.UserDTO;
import no.hvl.dat250.polls.models.User;

/**
 * UserMapper
 */
public class UserMapper {

    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        return dto;
    }
}

