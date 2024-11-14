<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { checkGuest, getPolls} from './helpermethods/helpermethods.js'

const router = useRouter()

// Check for authentication on mount
onMounted(async() => {
  const hasLogIn = localStorage.getItem('JWT')
  const hasGuest = localStorage.getItem('guest-id')
  if (!hasLogIn && !hasGuest) {
    router.push('/signup')
  } else if (hasGuest){
    let response = await checkGuest(hasGuest)
    if (response === false){
      localStorage.clear('guest-id')
      router.push('/signup')
    } else if(hasLogIn){
      console.log('Might have to relog in')
    }
  }
})
</script>

<template>
  <header>
    <div>
      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/login">Login</RouterLink>
        <!-- TODO: add logout if logged in -->
        <RouterLink to="/mypage">My page</RouterLink>
        <RouterLink to="/signup">Signup</RouterLink>
        <RouterLink to="/createPoll">Create Poll</RouterLink>
      </nav>
    </div>
  </header>
  <main>
    <RouterView />
  </main>
</template>

<style scoped></style>

