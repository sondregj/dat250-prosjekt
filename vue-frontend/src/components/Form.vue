<script setup>
import Button from 'primevue/button'
import FloatLabel from 'primevue/floatlabel'
import Password from 'primevue/password'
import InputText from 'primevue/inputtext'

const username = defineModel('username')
const password = defineModel('password')
const email = defineModel('email')
const props = defineProps(['message', 'showEmail', 'buttonText'])
</script>

<template>
  <div class="form">
    <h3 class="greeting">{{ props.message }}</h3>

    <div class="input1">
      <FloatLabel>
        <InputText id="username" v-model="username" />
        <label for="username">Username</label>
      </FloatLabel>
    </div>
    <div class="input3" v-if="props.showEmail === true">
      <FloatLabel>
        <InputText id="email" v-model="email" type="email" />
        <label for="email">Email</label>
      </FloatLabel>
    </div>
    <div class="input2">
      <FloatLabel>
        <Password v-model="password" :feedback="false" />
        <label for="password">Password</label>
      </FloatLabel>
    </div>
    <div class="button">
      <Button :label="props.buttonText"></Button>
    </div>
  </div>
</template>

<style scoped>
div.form {
  position: absolute;
  display: flex;
  flex-direction: column;
  height: 100%;
  width: 100%;
  justify-content: center;
  align-items: center;
}

.input1 {
  padding-bottom: 10px;
}

.input3 {
  margin-top: 20px;
  padding-bottom: 10px;
}

.input2 {
  margin-top: 20px;
}

div.button {
  padding-top: 15px;
}

h3.greeting {
  padding-bottom: 10px;
}
</style>
