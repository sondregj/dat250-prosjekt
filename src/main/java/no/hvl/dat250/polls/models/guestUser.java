package no.hvl.dat250.polls.models;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;
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

    @OneToMany(mappedBy = "guestId")
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


    
}
