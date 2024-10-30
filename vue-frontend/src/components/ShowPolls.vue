<script setup>
import {addVote, getPolls, getVoteOptions, deletePoll} from '@/helpermethods/helpermethods.js'
import {ref} from 'vue'
import Button from "primevue/button";
import Card from "primevue/card";

const polls = ref([])
const error = ref(null)
const voteOptions = ref([])

async function handleVote(voteOption){
  addVote(voteOption);
  polls.value = await getPolls();
  voteOptions.value = await getVoteOptions();
}

try {
  polls.value = await getPolls()
  voteOptions.value = await getVoteOptions()

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
          <ul>
            <li v-for="voteoption in voteOptions.filter(option => option.pollId === poll.id)" :key="voteoption.id">
              <h3>{{ voteoption.caption }}</h3>
              <Button label="Upvote" @click="handleVote(voteoption)"></Button>
              <h4>Number of votes: {{voteoption.votes.length}}</h4>
            </li>
          </ul>
        </template>
        <template #footer>
          <div class="footer">
            <Button label="Delete Poll" class="delete" @click="handleDeletePoll(poll.id)" ></Button>
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


</style>
