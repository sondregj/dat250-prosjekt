# DAT250 - FeedApp Project
## Tech stack
For the tech stack we have chosen to use:
- Relational database: PostgreSQL
- NoSQL: MongoDB
- Message broker: RabbitMQ
- Backend: Spring
- Frontend: Vue (Extra technology)
- Security: Spring Security
- Container engine: Docker
- CI pipeline: Github actions
- Build tool: Gradle
## Class Diagram
```mermaid
classDiagram
   class User
   class Vote
   class Poll
   class VoteOption
   class guestUser

   

   User "1" *--> "0..*" Vote : voted
   User "1" *--> "0..*" Poll : created
   Poll "1" *--> "2..*" VoteOption 
   guestUser "1" *--> "0..*" Vote : voted
   Vote "0..*" o--> "1" VoteOption


    class User{
      id: Long
      username: String
      email: String
      password: String
      castedVotes: List<Vote>
      createdPolls: List
    }

    class Poll {
      id: Long
      question: String
      publishedAt: Instant
      validUntil: Instant
      creator: User
      voteOptions: List
      
    }
    class Vote{
        id: Long
        publishedAt: Instant
        guest: guestUser
        user: User
        voteOption: VoteOption
    }

    class VoteOption {
        id: Long
        caption: String
        presentationorder: int
        votes: List
        poll: Poll
    }

    class guestUser {
        guestId: String
        validUntil: Instant
        votes: List

    }

```

## User stories
![user-story.png](user-story.png)


## Authors
- [Sondre Gjellestad](https://github.com/sondregj)
- [Vebjørn Fjeldstad](https://github.com/602822)
- [Jonas Vestbø](https://github.com/h598999)
- [Andreas Søland Henriksen](https://github.com/andreashenriksen)
