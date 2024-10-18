package no.hvl.dat250.polls.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;

/**
 * VoteOption
 */
@Entity
public class VoteOption {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String caption; 
    private int presentationOrder;

    @OneToMany(mappedBy = "voteOption", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Vote> votes;

    @ManyToOne
    private Poll poll;

    public VoteOption(){}

    public VoteOption(String caption, int presentationOrder){
        this.caption = caption;
        this.presentationOrder = presentationOrder;
        this.votes = new ArrayList<>();
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getCaption() {
        return caption;
    }


    public void setCaption(String caption) {
        this.caption = caption;
    }


    public int getPresentationOrder() {
        return presentationOrder;
    }


    public void setPresentationOrder(int presentationOrder) {
        this.presentationOrder = presentationOrder;
    }


    public List<Vote> getVotes() {
        return votes;
    }


    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }


    public Poll getPoll() {
        return poll;
    }


    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteOption that = (VoteOption) o;
        return presentationOrder == that.presentationOrder &&
            Objects.equals(id, that.id) &&
            Objects.equals(caption, that.caption);
    }
}
