<script setup>
import Button from 'primevue/button'
import FloatLabel from 'primevue/floatlabel'
import Password from 'primevue/password'
import InputText from 'primevue/inputtext'
import { useRouter } from 'vue-router'
import { createNewUser, loginUser, createGuestUser } from '../helpermethods/helpermethods.js'
import { useAuth } from '@/helpermethods/auth.js'
import { ref } from 'vue'

const username = defineModel('username')
const password = defineModel('password')
const email = defineModel('email')
const props = defineProps(['message', 'signup', 'buttonText', 'guestText'])
const router = useRouter()
const { login } = useAuth()
const error = ref(null)

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
    const response = await createNewUser(username.value, password.value, email.value);
    console.log(response)
    if (response){
    console.log('response was')
    await new Promise(resolve => setTimeout(resolve, 400));
    const logInResponse = await loginUser(username.value, password.value);
    if (logInResponse){
    console.log('loginresponse was')
    login()
    resetForm()
    await router.push("/");
    }
    } else{
    resetForm()
    error.value = "Error creating user, email, password and username cannot be empty";
    }
  } catch(error){
    console.error("Error during creation or login: ", error.message);
  }
}

const handleSubmitLogin = async () => {
  try{
    const response = await loginUser(username.value, password.value)
    console.log(response)
    if (response){
      login()
      resetForm()
      error.value = null;
      await router.push("/")
    }
      resetForm()
      error.value = "Wrong username or password";
  } catch(error){
    console.error("Error during creation or login: ", error.message);
    error.value = "An unexpected error occurred during login.";
  }
}
</script>

<template>
  <div class="form">
    <div v-if="error" class="error-message">{{ error }}</div>
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
    <div class="guest-button">
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
div.guest-button {
  padding-top: 15px;
}

h3.greeting {
  padding-bottom: 10px;
}
.error-message {
  color: red;
  margin-bottom: 10px;
}
</style>
