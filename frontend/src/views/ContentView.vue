<script setup></script>

<template>
  <v-main>
    <RouterView />
  </v-main>
</template>

<script>
export default {};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="scss">
.v-main {
  margin-top: 5.5em;
  min-height: 85vh;
  height: auto;
  display: flex;
  justify-content: center;
}
</style>
