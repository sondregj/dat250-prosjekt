// package no.hvl.dat250.polls.UnitTests.messagingrabbitmq;
//
// import no.hvl.dat250.polls.messagingrabbitmq.PollProducer;
// import no.hvl.dat250.polls.models.Poll;
// import no.hvl.dat250.polls.models.User;
// import no.hvl.dat250.polls.models.Vote;
// import no.hvl.dat250.polls.models.VoteOption;
// import org.junit.jupiter.api.*;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;
//
// import java.time.Instant;
// import java.util.List;
//
// @SpringBootTest
// @ActiveProfiles("test")
// public class PollProducerTest {
//
//     private final static PollProducer pollProducer = new PollProducer();
//
//     private static Poll poll;
//     private static VoteOption voteOption1;
//     private static VoteOption voteOption2;
//
//     @BeforeAll
//     static void setUpPoll() {
//         poll = new Poll("Which way do you eat a pizza?", Instant.now(), Instant.now().plusSeconds(3600));
//         voteOption1 = new VoteOption("From the top", 1);
//         voteOption2 = new VoteOption("From the bottom", 2);
//         User user = new User();
//
//         user.setId(1L);
//         user.setUsername("Bartholomew");
//         user.setEmail("Bartholomew@mail.com");
//         user.setPassword("password");
//
//         voteOption1.setId(1L);
//         voteOption1.setPoll(poll);
//
//         voteOption2.setId(2L);
//         voteOption2.setPoll(poll);
//
//         poll.getVoteOptionMutable().add(voteOption1);
//         poll.getVoteOptionMutable().add(voteOption2);
//         poll.setId(1L);
//         poll.setCreator(user);
//         user.setCreatedPolls(List.of(poll));
//
//         pollProducer.send(poll);
//     }
//
//     @Test
//     void voteOnPoll() {
//         User user = new User();
//         Vote vote = new Vote();
//
//         user.setId(2L);
//         user.setUsername("Baldwin");
//         user.setEmail("Baldwin@mail.com");
//         user.setPassword("password123");
//
//         vote.setId(1L);
//         vote.setPublishedAt(Instant.now());
//         vote.setUser(user);
//         vote.setVoteOption(voteOption1);
//
//         voteOption1.getVotes().add(vote);
//         user.setCastedVotes(List.of(vote));
//
//         pollProducer.send(poll);
//     }
//
//     @Test
//     void voteOnAnotherOption() {
//         User user = new User();
//         Vote vote = new Vote();
//
//         user.setId(3L);
//         user.setUsername("Cletus");
//         user.setEmail("Cletus@mail.com");
//         user.setPassword("password1");
//
//         vote.setId(2L);
//         vote.setPublishedAt(Instant.now());
//         vote.setUser(user);
//         vote.setVoteOption(voteOption2);
//
//         voteOption2.getVotes().add(vote);
//         user.setCastedVotes(List.of(vote));
//
//         pollProducer.send(poll);
//     }
//
//     @AfterAll
//     static void deleteVoteOnPoll() {
//         for (VoteOption voteOption : poll.getVoteOptions()) {
//             System.out.println(voteOption.getVotes().size());
//             if (!voteOption.getVotes().isEmpty()) {
//                 voteOption.setVotes(List.of());
//                 pollProducer.send(poll);
//                 return;
//             }
//         }
//     }
// }
//
