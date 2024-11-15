<script setup>
import { onMounted, ref } from 'vue'
import { useRouter, RouterLink, RouterView } from 'vue-router'
import { useAuth } from './helpermethods/auth.js';
import { checkGuest } from './helpermethods/helpermethods.js'

const router = useRouter()
const { isLoggedIn, logout, initializeAuth } = useAuth()

function handleLogOut(){
  logout()
  router.push('/login')
}

onMounted(async () => {
  initializeAuth()

  const hasLogIn = localStorage.getItem('JWT')
  const hasGuest = localStorage.getItem('guest-id')

  if (!hasLogIn && !hasGuest) {
    router.push('/signup')
  } else if (hasGuest) {
    try {
      const response = await checkGuest(hasGuest)
      if (response === false){
        localStorage.removeItem('guest-id')
        router.push('/signup')
      } else if (hasLogIn){
        console.log('User is logged in.')
        isLoggedIn.value = true
      }
    } catch (error) {
      console.error('Error checking guest status:', error)
      localStorage.removeItem('guest-id')
      router.push('/signup')
    }
  }
})

</script>

<template>
  <header>
    <div>
      <nav>
        <!-- Always visible links -->
        <RouterLink to="/">Home</RouterLink>

        <RouterLink to="/login" v-if="!isLoggedIn">Login</RouterLink>
        <RouterLink to="/signup" v-if="!isLoggedIn">Signup</RouterLink>

        <RouterLink to="/mypage" v-if="isLoggedIn">My page</RouterLink>
        <RouterLink to="/createPoll" v-if="isLoggedIn">Create Poll</RouterLink>

        <button v-if="isLoggedIn" @click="handleLogOut" class="logout-button">Logout</button>
      </nav>
    </div>
  </header>
  <main>
    <RouterView />
  </main>
</template>

<style scoped>
/* Existing Styles */

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

/* Error Message Styling */
.error {
  color: red;
  padding: 20px;
  background-color: #ffe6e6;
  border: 1px solid red;
  margin: 20px;
  border-radius: 5px;
}

/* Logout Button Styling */
button.logout-button {
  margin: 0 10px;
  padding: 8px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  background-color: #f44336; /* Red color for logout */
  color: white;
  font-size: 16px;
}

button.logout-button:hover {
  background-color: #d32f2f; /* Darker red on hover */
}
</style>

