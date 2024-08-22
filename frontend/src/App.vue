<script setup>
import HeaderView from "@/views/HeaderView.vue";
import ContentView from "@/views/ContentView.vue";
</script>

<template>
  <v-app>
    <v-container fluid>
      <HeaderView />
      <ContentView />
    </v-container>
  </v-app>
</template>

<script>
export default {
  component: {
    HeaderView,
    ContentView,
  },
};
</script>

<style lang="scss">
@import "@/assets/style/common.scss";
</style>
