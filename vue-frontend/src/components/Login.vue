<script setup>
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import FloatLabel from 'primevue/floatlabel'
import Button from 'primevue/button';


const username = defineModel('username')
const password = defineModel('password')
</script>

<template>
  <div class="form">
    <h3 class="greeter">Welcome back!</h3>
    <div class="input1">
    <FloatLabel>
      <InputText id="username" v-model="username" />
      <label for="username">Username</label>
    </FloatLabel>
    </div>
    <div class="input2">
      <FloatLabel>
        <Password v-model="password" :feedback="false" />
        <label for="password">Password</label>
      </FloatLabel>
    </div>
    <div class="button">
      <Button label="Submit"></Button>
    </div>


  </div>

</template>

<style scoped>
html,
body {
  height: 100%;
  margin: 0;
}

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
  padding-bottom: 20px;
}

.input2 {
  padding-top: 10px;
}


div.button {
  padding-top: 15px;
}

h3.greeter {
  padding-bottom: 10px;
}

</style>
