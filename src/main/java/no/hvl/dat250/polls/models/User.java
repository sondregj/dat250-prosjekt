package no.hvl.dat250.polls.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * User
 */


@Entity
@Table(name = "\"user\"") // Escapes the table name "user"
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Vote> castedVotes;

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Poll> createdPolls;

    public User(){}

    public User(String username, String email){
        this.username = username;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Vote> getCastedVotes() {
        return castedVotes;
    }

    public void setCastedVotes(List<Vote> castedVotes) {
        this.castedVotes = castedVotes;
    }

    public List<Poll> getCreatedPolls() {
        return createdPolls;
    }

    public void setCreatedPolls(List<Poll> createdPolls) {
        this.createdPolls = createdPolls;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        // Compare the id field if it's non-null, as it is the unique identifier
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        // Optionally compare other fields such as username and email
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        return email != null ? email.equals(user.email) : user.email == null;
    }

    @Override
    public String toString(){
        return "User{"+
            "id=" + id +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            '}';
    }

}

