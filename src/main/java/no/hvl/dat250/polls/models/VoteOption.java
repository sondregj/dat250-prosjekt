package no.hvl.dat250.polls.models;

import java.util.List;

import jakarta.persistence.Entity;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caption; 
    private int presentationOrder;

    @OneToMany(mappedBy = "voteOption")
    private List<Vote> votes;

    @ManyToOne
    private Poll poll;


	public VoteOption(String caption, int presentationOrder){
        this.caption = caption;
        this.presentationOrder = presentationOrder;
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
}