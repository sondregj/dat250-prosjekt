package no.hvl.dat250.polls.models;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * guestUser
 */
@Entity
public class guestUser {

    private final static TemporalAmount STANDARD_DURATION_HOURS = Duration.ofHours(3);

    @Id
    private String guestId;
    private Instant validUntil;

    @OneToMany(mappedBy = "guest")
    private List<Vote> votes;

    public guestUser(){
        this.guestId = generateGuestId();
        this.validUntil = Instant.now().plus(STANDARD_DURATION_HOURS);
    }

    private String generateGuestId(){
        return UUID.randomUUID().toString();
    }

    public void extendValidUntil(){
        this.validUntil = Instant.now().plus(STANDARD_DURATION_HOURS);
    }

    public void setValidUntil(Instant newValidUntil){
        this.validUntil = newValidUntil;
    }

    public Instant getValidUntil(){
        return this.validUntil;
    }

    public String getGuestId(){
        return this.guestId;
    }

    public void setGuestId(String newId){
        this.guestId = newId;
    }

     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        guestUser guestUser = (guestUser) o;
        return Objects.equals(guestId, guestUser.guestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guestId);
    }

    @Override
    public String toString() {
        return "guestUser{" +
                "guestId='" + guestId + '\'' +
                ", validUntil=" + validUntil +
                ", votes=" + votes +
                '}';
    }
}
