<script setup>

import {
  addVote,
  addVoteGuest,
  getPolls,
  getPoll,
  checkPollExpired,
} from '@/helpermethods/helpermethods.js'

import { ref, onMounted, onBeforeUnmount} from 'vue'
import Button from 'primevue/button'
import Card from 'primevue/card'
import { Client } from '@stomp/stompjs'

const polls = ref([])
const error = ref(null)
/**@type{Client} */
let client;
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
      let retrievedPoll = await getPoll(voteOption.pollId);
      polls.value
      .find(poll => poll.id === retrievedPoll.id)
      .voteOptions = retrievedPoll.voteOptions
    } } catch (error) {
    console.log(error)
  }
}

async function refreshPolls() {
  console.log("Refreshing Polls")
  polls.value = await getPolls()
}

onMounted(async () => {
  try {
    polls.value = await getPolls()
    setInterval(() => checkPollExpired(polls.value), 10 * 60 * 1000) // Runs every 10 min
    setInterval(() => refreshPolls(), 15 * 60 * 1000)
  } catch (e) {
    error.value = e
  }
})


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
        <template #footer> <div class="footer">
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
