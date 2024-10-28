package no.hvl.dat250.polls.dto;

public class UserCreationDTO {

    private String username;
    private String email;
    private String password;

    public UserCreationDTO(String username, String email, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
