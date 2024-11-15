import { ref } from 'vue'

const isLoggedIn = ref(false)

const initializeAuth = () => {
  const hasLogIn = localStorage.getItem('JWT')
  const hasGuest = localStorage.getItem('guest-id')

  if (hasLogIn) {
    isLoggedIn.value = true
  } else {
    isLoggedIn.value = false
  }
}

const login = () => {
  isLoggedIn.value = true
}

const logout = () => {
  localStorage.removeItem('JWT')
  localStorage.removeItem('guest-id')
  isLoggedIn.value = false
}

export function useAuth() {
  return {
    isLoggedIn,
    login,
    logout,
    initializeAuth
  }
}
