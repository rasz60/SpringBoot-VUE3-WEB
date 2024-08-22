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
        v-show="!notContents"
        class="items"
        @click="fnMove(item.itemSeq)"
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

      <v-row class="items" v-show="notContents">
        <v-col cols="12">게시물을 찾을 수 없습니다.</v-col>
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
            @click="fnMove()"
          >
            작성
          </v-btn>
        </v-col>
      </v-row>
    </div>
  </v-layout>
</template>

<script>
import NoticeBoardMethods from "@/assets/js/board/NoticeBoardMethods.js";
import NoticeBoardDatas from "@/assets/js/board/NoticeBoardDatas.js";
export default {
  name: "NoticeBoard",
  data() {
    return NoticeBoardDatas;
  },
  async created() {
    await this.getBoardList();
  },
  methods: NoticeBoardMethods,
};
</script>
