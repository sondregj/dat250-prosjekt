<script setup>
import Button from 'primevue/button'
import FloatLabel from 'primevue/floatlabel'
import Password from 'primevue/password'
import InputText from 'primevue/inputtext'
import { useRouter } from 'vue-router'
import { createNewUser, loginUser, createGuestUser } from '../helpermethods/helpermethods.js'

const username = defineModel('username')
const password = defineModel('password')
const email = defineModel('email')
const props = defineProps(['message', 'signup', 'buttonText', 'guestText'])
const router = useRouter()

const resetForm = () => {
  username.value = ''
  password.value = ''
  email.value = ''
}

const handleGuest = () => {

  try{
    createGuestUser()
    router.push("/")
  } catch (error){
    alert("Error during guest init: " + error)
  }
}

const handleSubmit = async () => {
  try{
    createNewUser(username.value, password.value, email.value);
    await new Promise(resolve => setTimeout(resolve, 400));
    loginUser(username.value, password.value);
    localStorage.removeItem('guest-id')
    console.log("Removed guest-id")
    resetForm();
    await router.push("/");
  } catch(error){
    console.error("Error during creation or login: ", error.message);
  }
}

const handleSubmitLogin = async () => {
  try{
    loginUser(username.value, password.value)
    localStorage.removeItem('guest-id')
    console.log("Removed guest-id")
    resetForm()
    await router.push("/")
  } catch(error){
    console.error("Error during creation or login: ", error.message);
  }
}
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
    <div class="input3" v-if="props.signup === true">
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
      <Button
        :label="props.buttonText"
        v-if="props.signup === true"
        @click="handleSubmit"
      ></Button>
      <Button
        :label="props.buttonText"
        v-else
        @click="handleSubmitLogin"
      ></Button>
    </div>
    <div class="button">
      <Button
        :label="props.guestText"
        @click="handleGuest"
        ></Button>
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
