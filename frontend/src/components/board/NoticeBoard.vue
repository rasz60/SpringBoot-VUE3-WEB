<template>
  <v-layout>
    <div id="board">
      <div id="title">
        <h1>NoTiCe</h1>
      </div>
      <v-row class="title">
        <v-col cols="1">
          <v-card>#</v-card>
        </v-col>
        <v-col cols="6">
          <v-card>제목</v-card>
        </v-col>
        <v-col cols="1">
          <v-card>작성자</v-card>
        </v-col>
        <v-col cols="2">
          <v-card>작성일자</v-card>
        </v-col>
        <v-col cols="1">
          <v-card>조회수</v-card>
        </v-col>
        <v-col cols="1">
          <v-card>좋아요</v-card>
        </v-col>
      </v-row>
      <v-row class="items" @click="fnMove(3)">
        <v-col cols="1"> 3 </v-col>
        <v-col cols="6" class="itemTitle">
          <span> noticeadkfaskdfasdfasdfadfqwefqefads [4] </span>
        </v-col>
        <v-col cols="1"> rassayz60 </v-col>
        <v-col cols="2"> 2024/08/06 12:00:00 </v-col>
        <v-col cols="1"> 192 </v-col>
        <v-col cols="1"> 99 </v-col>
      </v-row>
      <v-row class="items" @click="fnMove(2)">
        <v-col cols="1"> 2 </v-col>
        <v-col cols="6" class="itemTitle">
          <span> noticeadkfaskdfasdfasdfadfqwefqefads [6] </span>
        </v-col>
        <v-col cols="1"> rassayz60 </v-col>
        <v-col cols="2"> 2024/08/06 10:00:00 </v-col>
        <v-col cols="1"> 92 </v-col>
        <v-col cols="1"> 1 </v-col>
      </v-row>
      <v-row class="items" @click="fnMove(1)">
        <v-col cols="1"> 1 </v-col>
        <v-col cols="6" class="itemTitle">
          <span> noticeadkfaskdfasdfasdfadfqwefqefads [7] </span>
        </v-col>
        <v-col cols="1"> rassayz60 </v-col>
        <v-col cols="2"> 2024/08/06 09:00:00 </v-col>
        <v-col cols="1"> 12 </v-col>
        <v-col cols="1"> 9 </v-col>
      </v-row>

      <v-row>
        <v-spacer></v-spacer>
        <v-col cols="10">&lt; 1 | 2 | 3 | 4 | 5 | 6 | 7 &gt;</v-col>
        <v-col cols="1">
          <v-btn
            prepend-icon="mdi-plus-box-multiple-outline"
            color="primary"
            variant="tonal"
            @click="fnMove('form')"
          >
            작성
          </v-btn>
        </v-col>
      </v-row>
    </div>
  </v-layout>
</template>

<script>
export default {
  name: "NoticeBoard",
  methods: {
    fnMove(path) {
      this.$router.push("./notice/" + path);
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

  .v-row.items:hover {
    background-color: rgba(250, 235, 215, 0.2);
    cursor: pointer;

    span {
      text-decoration: underline;
    }
  }
}
</style>
