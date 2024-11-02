<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

// Check for authentication on mount
onMounted(() => {
  const hasToken = localStorage.getItem('jwt') || localStorage.getItem('guest-id')
  if (!hasToken) {
    router.push('/signup')
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

