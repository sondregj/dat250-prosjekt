<script setup>
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import FloatLabel from 'primevue/floatlabel'
import Button from 'primevue/button'
import Form from './Form.vue'
</script>

<template>
  <Form message="Welcome back!" :showEmail="false" buttonText="Login" />
</template>

<style scoped></style>
