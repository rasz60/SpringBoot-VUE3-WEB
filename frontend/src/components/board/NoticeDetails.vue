<template>
  <v-layout>
    <div id="board">
      <div id="title">
        <h1>NoTiCe</h1>
      </div>
      <p>details</p>
    </div>
  </v-layout>
</template>

<script>
export default {
  name: "NoticeDetails",
  methods: {
    fnMove(seq) {
      this.$router.push("/board/notice/" + seq);
    },
  },
};
</script>

<style lang="scss">
#board {
  width: 100%;

  div#title {
    border-left: 6px solid red;
    padding-left: 2em;
    margin-left: 1em;
    margin-top: 1em;
    margin-bottom: 1.5em;
  }
  .v-row {
    width: 95%;
    margin-left: 2em;
    margin-right: 2em;
    .v-col {
      text-align: center;

      .v-card {
        padding: 0.4em;
      }
    }
    .v-col.itemTitle {
      text-align: left;
    }
  }

  .v-row.items {
    border-bottom: 1px solid lightgray;
  }
}
</style>
