<script setup>
import MyPage from '@/components/MyPage.vue';
</script>

<template>
  <Suspense>
    <template #default>
      <MyPage />
    </template>
    <template #fallback>Loading my page...</template>
  </Suspense>
</template>

<style scoped></style>
