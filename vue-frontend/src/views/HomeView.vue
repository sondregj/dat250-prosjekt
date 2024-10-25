<script setup>
import Home from '@/components/Home.vue'
import ShowPolls from '@/components/ShowPolls.vue'
</script>

<template>
  <Suspense>
    <template #default>
      <ShowPolls />
    </template>
    <template #fallback>Loading Polls...</template>
  </Suspense>
</template>

<style scoped></style>
