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
        <v-col cols="1">
          <v-card>말머리</v-card>
        </v-col>
        <v-col cols="5">
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

      <v-row
        v-for="item in list"
        :key="item"
        class="items"
        @click="fnMove(`details/` + item.itemSeq)"
      >
        <v-col cols="1">{{ item.itemSeq }}</v-col>
        <v-col cols="1">{{ item.itemHeaderName }}</v-col>
        <v-col cols="5" class="itemTitle">
          <span> {{ item.itemTitle }} ({{ item.itemCommentsCnt }}) </span>
        </v-col>
        <v-col cols="1"> {{ item.itemRegUuid.memId }} </v-col>
        <v-col cols="2"> {{ item.itemRegDate.replace("T", " ") }} </v-col>
        <v-col cols="1"> {{ item.itemHitsCnt }} </v-col>
        <v-col cols="1"> {{ item.itemLikesCnt }} </v-col>
      </v-row>

      <v-row>
        <v-spacer></v-spacer>
        <v-col cols="10">
          <div class="text-center">
            <v-pagination
              v-model="page"
              :length="totalPages"
              total-visible="6"
              @click="getBoardList"
              next-icon="mdi-menu-right"
              prev-icon="mdi-menu-left"
            ></v-pagination>
          </div>
        </v-col>
        <v-col cols="1">
          <v-btn
            prepend-icon="mdi-plus-box-multiple-outline"
            color="primary"
            variant="tonal"
            @click="fnMove('form/0')"
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
  data() {
    return {
      itemStatus: 1,
      list: [],
      notContents: false,
      totalPages: 0,
      page: 0,
      limit: 10,
    };
  },
  async created() {
    await this.getBoardList();
  },
  methods: {
    async getBoardList() {
      var pg = this.page != 0 ? this.page - 1 : 0;

      await this.axios
        .get("/rest/boardList/" + this.itemStatus + "/" + pg + "/" + this.limit)
        .then((res) => {
          var rst = res.data;
          this.list = rst.content;
          if (rst.content.length > 0) {
            this.page = rst.pageable.pageNumber + 1;
            this.totalPages = rst.totalPages;
            this.notContents = false;
          } else {
            this.page = 1;
            this.totalPages = 1;
            this.notContents = true;
          }

          console.log(this.page, this.totalPages, this.notContents);
        })
        .catch((err) => {
          console.log(err);
        });
    },
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
    background-color: rgba(255, 233, 228, 0.2);
    cursor: pointer;

    span {
      text-decoration: underline;
    }
  }
}
</style>
