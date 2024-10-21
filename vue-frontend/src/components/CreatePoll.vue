<script setup>
import FloatLabel from 'primevue/floatlabel'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import Tag from 'primevue/Tag'
import { ref } from 'vue'

const question = defineModel('question')
const voteOption = defineModel('voteOption')
let voteOptions = ref([])

function addVoteOption() {
  voteOptions.value = [...voteOptions.value, voteOption.value]
  voteOption.value = ''
}

function deleteLatest() {
  voteOptions.value.pop()
}
</script>

<template>
  <div class="container">
    <h3>Create a new Poll</h3>
    <div class="question">
      <FloatLabel>
        <InputText id="question" v-model="question" />
        <label for="question">Question</label>
      </FloatLabel>
    </div>
    <div class="voteoptions">
      <FloatLabel>
        <InputText id="voteoptions" v-model="voteOption" />
        <label for="voteoptions">Vote Options (minimum 2)</label>
      </FloatLabel>
      <Button label="Add" @click="addVoteOption"></Button>
    </div>
    <div class="displayVoteOptions">
      <p>Vote Options added:</p>
      <p v-if="voteOptions.length === 0">None</p>
      <ul v-else>
        <li v-for="voteOption in voteOptions">
          <Tag severity="success" :value="voteOption" rounded></Tag>
        </li>
        <Button label="Delete latest" @click="deleteLatest"></Button>
      </ul>
    </div>
    <Button v-if="voteOptions.length >= 2" label="Submit"></Button>
    <Button v-else label="Submit" disabled></Button>
  </div>
</template>

<style scoped>
h3 {
  margin-bottom: 30px;
}

div.voteoptions {
  display: flex;
  flex-direction: row;
  padding-left: 65px;
}

Button {
  margin-left: 10px;
}

div.container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: absolute;
  width: 100%;
  height: 100%;
}

div.question {
  margin-bottom: 40px;
  margin-top: 10px;
}

div.displayVoteOptions {
  margin: 20px;
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
}

ul {
  display: flex;
  flex-direction: row;
  list-style: none;
  padding: 0;
}

p {
  padding-right: 5px;
}

li {
  margin: 5px;
}
</style>
