<script setup>

import {
  addVote,
  getPolls,
  deletePoll,
  getPoll,
  checkPollExpired,
} from '@/helpermethods/helpermethods.js'

import { ref } from 'vue'
import Button from 'primevue/button'
import Card from 'primevue/card'

const polls = ref([])
const error = ref(null)

async function handleVote(voteOption) {
  try {

  let result = null;

  if (localStorage.getItem("JWT")){
    result = await addVote(voteOption)
  } else if(localStorage.getItem("guest-id")){
    result = await addVoteGuest(voteOption)
  } else {
    alert("You have to be logged in as either a guest or a user to vote");
  }

    if (result) {
      //const pollId = voteOption.pollId
      //const voteId = result.id
      //const poll = polls.value.find(poll => poll.id === pollId)
      //if (poll) {
      //  const voption = poll.voteOptions.find(
      //    voption => voption.id === voteOption.id,
      //  )
      //  if (voption) {
      //    voption.votes.push({
      //      id: voteId,
      //      publishedAt: Date.now(),
      //      voteOptionId: voption.id,
      //      pollQuestion: poll.question,
      //      voteOptionCaption: voption.caption,
      //      pollId: poll.id,
      //    })
      //  }
      //}
      let retrievedPoll = await getPoll(voteOption.pollId);

      polls.value
      .find(poll => poll.id === retrievedPoll.id)
      .voteOptions = retrievedPoll.voteOptions
    }
  } catch (error) {
    console.log(error)
  }
}

try {
  polls.value = await getPolls()
  setInterval(() => checkPollExpired(polls.value), 10 * 60 * 1000) //will run every 10 min
} catch (e) {
  error.value = e
}

async function handleDeletePoll(pollId) {
  try {
    await deletePoll(pollId)
    polls.value = polls.value.filter(poll => poll.id !== pollId)
  } catch (error) {
    console.log(error)
  }
}
</script>

<template>
  <h1 v-if="polls.length > 0">Polls</h1>
  <div v-if="error">Error : {{ error }}</div>
  <div class="container">
    <h1 v-if="polls.length === 0">No polls :(</h1>
    <div v-else class="poll" v-for="poll in polls" :key="poll.id">
      <Card class="card">
        <template #title>
          {{ poll.question }}
        </template>
        <template #content>
          <p class="expired" v-if="poll.validUntil === 0">Poll Expired</p>
          <ul>
            <li v-for="voteoption in poll.voteOptions" :key="voteoption.id">
              <h3>{{ voteoption.caption }}</h3>
              <Button
                v-if="poll.validUntil === 0"
                label="Upvote"
                disabled
              ></Button>
              <Button
                v-else
                label="Upvote"
                @click="handleVote(voteoption)"
              ></Button>
              <h4>Number of votes: {{ voteoption.votes.length }}</h4>
            </li>
          </ul>
        </template>
        <template #footer>
          <div class="footer">
            <Button
              label="Delete Poll"
              class="delete"
              @click="handleDeletePoll(poll.id)"
            ></Button>
          </div>
        </template>
      </Card>
    </div>
  </div>
</template>

<style scoped>
div.container {
  display: flex;
  flex-wrap: wrap;
}

ul {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

li {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin: 10px;
}

Button {
  margin: 0 10px;
}

div.poll {
  margin: 20px;
  flex: 1 1 200px;
}

.card {
  width: fit-content;
  height: fit-content;
}

div.footer {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

p.expired {
  color: red;
}

</style>
