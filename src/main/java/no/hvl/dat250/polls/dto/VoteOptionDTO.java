package no.hvl.dat250.polls.dto;

/**
 * VoteOptionDTO
 */
public class VoteOptionDTO {

   private Long id;
   private String caption;
   private int presentationOrder;

   public VoteOptionDTO(Long id, String caption, int presentationOrder){
       this.id = id;
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
}
