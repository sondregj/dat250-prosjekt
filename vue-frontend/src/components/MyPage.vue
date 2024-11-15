<script setup>

import {
  deletePoll,
  deleteVote,
  getPollByUser,
  getVotesByUser,
} from '@/helpermethods/helpermethods.js'

import { ref, onMounted} from 'vue'
import Button from 'primevue/button'
import Card from 'primevue/card'

const polls = ref([])
const votes = ref([])
const error = ref(null)

async function handleDeletePoll(pollId) {
  try {
    await deletePoll(pollId)
    polls.value = polls.value.filter(poll => poll.id !== pollId)
  } catch (error) {
    console.log(error)
  }
}

async function handleDeleteVote(voteId) {
  try {
    await deleteVote(voteId)
    votes.value = votes.value.filter(vote => vote.id !== voteId)
  } catch (error) {
    console.log(error)
  }
}

onMounted(async () => {
  try {
    polls.value = await getPollByUser()
    votes.value = await getVotesByUser()
    console.log(votes.value)
  } catch (e) {
    error.value = e
  }
})
</script>

<template>
  <h1 v-if="polls.length > 0">Polls</h1>
  <div class="container">
    <h1 v-if="polls.length === 0">No polls created :(</h1>
    <div v-else class="poll" v-for="poll in polls" :key="poll.id">
      <Card class="card">
        <template #title>
          {{ poll.question }}
        </template>
        <template #content>
          <ul>
            <li v-for="voteoption in poll.voteOptions" :key="voteoption.id">
              <h3>{{ voteoption.caption }}</h3>
              <h4> Number of votes: {{ voteoption.votes.length }}</h4>
            </li>
          </ul>
        </template>
        <template #footer> <div class="footer">
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

  <h1 v-if="votes.length > 0">Votes</h1>
  <div class="container">
    <h1 v-if="votes.length === 0">No votes casted :(</h1>
    <div v-else class="poll" v-for="vote in votes" :key="vote.id">
      <Card class="card">
        <template #title>
          {{ vote.voteOptionCaption }}
        </template>
        <template #footer> <div class="footer">
            <Button
              label="Delete vote"
              class="delete"
              @click="handleDeleteVote(vote.id)"
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
