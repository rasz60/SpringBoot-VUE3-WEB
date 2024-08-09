<template>
  <!-- details -->
  <v-sheet id="details">
    <v-row>
      <v-col cols="10" id="itemTitle">
        <h2>{{ itemHeaderName }}&nbsp;{{ items.title }}</h2>
      </v-col>
      <v-col cols="2" class="info">
        <v-icon icon="mdi-eye-outline" size="small" />
        &nbsp;{{ items.itemHitsCnt }}

        &nbsp;&nbsp;

        <v-icon
          :icon="likeItem ? `mdi-heart` : `mdi-heart-outline`"
          :color="likeItem ? `red` : ``"
          size="small"
          @click="fnLike"
        />
        &nbsp;{{ itemLikes }}
      </v-col>
      <v-divider></v-divider>
    </v-row>

    <v-row>
      <v-spacer></v-spacer>
      <v-col cols="3" class="info">
        <v-icon icon="mdi-account" size="small" />
        {{ items.itemRegUuid.memId }}

        &nbsp;&nbsp;

        <v-icon icon="mdi-clock-outline" size="small" />
        {{ items.itemRegDate }}
      </v-col>
      <v-divider></v-divider>
    </v-row>

    <v-row>
      <v-col cols="12">
        <pre id="contents">{{ items.contents }}</pre>
      </v-col>
      <v-divider></v-divider>
    </v-row>

    <v-row>
      <v-col cols="12">
        <v-chip label class="mr-2" color="primary">
          Tags&nbsp;<v-icon icon="mdi-label" />
        </v-chip>
        <v-chip
          v-for="tag in items.hashtags"
          :key="tag"
          class="mr-1"
          color="success"
          variant="tonal"
        >
          <v-icon icon="mdi-pound" start></v-icon>
          {{ tag }}
        </v-chip>
      </v-col>
      <v-divider></v-divider>
    </v-row>

    <v-row>
      <v-spacer></v-spacer>
      <v-col cols="3" class="text-right">
        <v-btn
          prepend-icon="mdi-content-save-all"
          color="warning"
          v-if="items.eAuth"
          variant="tonal"
          @click="fnMove(`edit`, seq)"
          >수정하기</v-btn
        >
        <v-btn
          prepend-icon="mdi-close"
          color="red"
          v-if="items.eAuth"
          variant="tonal"
          @click="fnItemDel"
          >삭제하기</v-btn
        >
        <v-btn
          prepend-icon="mdi-format-list-bulleted-square"
          color="default"
          variant="tonal"
          @click="fnMove()"
          >목록으로</v-btn
        >
      </v-col>
      <v-divider></v-divider>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-btn
          id="commentDrop"
          variant="plain"
          :append-icon="commentDrop ? `mdi-menu-up` : `mdi-menu-down`"
          @click.stop="commentDrop = !commentDrop"
          :text="
            commentDrop
              ? `댓글 닫기`
              : `댓글 보기 (` + this.items.itemCommentsCnt + `)`
          "
        >
        </v-btn>
      </v-col>
      <v-divider></v-divider>
    </v-row>
    <v-row v-show="commentDrop">
      <v-col cols="12">
        <v-text-field
          prepend-icon="mdi-forum-outline"
          type="text"
          label="댓글 남기기"
          variant="underlined"
        ></v-text-field>
      </v-col>
      <v-divider></v-divider>
    </v-row>
    <v-row v-show="commentDrop" class="comments">
      <v-col cols="2"> @rassayz60 </v-col>
      <v-col cols="9"> 테스트 댓글 입니다아아아.</v-col>
      <v-col cols="1">
        <v-icon icon="mdi-heart-outline" size="small" @click="fnLike" />
        99
      </v-col>
    </v-row>
  </v-sheet>
</template>

<script>
export default {
  props: {
    flag: {
      type: String,
      required: true,
    },
    seq: {
      type: String,
      required: true,
    },
    items: {
      type: Map,
      required: true,
    },
  },
  name: "NoticeDetails",
  data() {
    return {
      itemHeaderName: "",
      likeItem: false,
      itemLikes: 0,
      commentDrop: false,
    };
  },
  mounted() {
    this.itemHeaderName = this.items.itemHeaderName
      ? "[" + this.items.itemHeaderName + "]"
      : "";
    this.itemLikes = this.items.itemLikesCnt;
    this.likeItem = this.items.likeItem;
  },
  methods: {
    fnMove(flag, seq) {
      var param = {
        flag: flag,
        seq: seq,
      };

      this.$emit("sendMessage", param);
    },
    async fnLike() {
      var method = this.likeItem ? `delete` : `post`;
      var url = this.likeItem ? `/rest/item/delLike` : `/rest/item/addLike`;

      var data = {
        itemUuid: this.items.itemUuid,
      };

      await this.axios({
        method: method, // HTTP 메서드
        url: url, // 요청할 URL
        data: data, // 전송할 데이터
        headers: {
          Accept: "application/json", // 서버로부터 JSON 응답을 기대
          "Content-Type": "application/json", // 요청의 콘텐츠 타입을 JSON으로 설정
        },
      })
        .then((res) => {
          var rst = res.data;

          this.itemLikes = rst.resultMessage[0].itemLikes;
          this.likeItem =
            rst.resultCode == "200" ? !this.likeItem : this.likeItem;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    async fnItemDel() {
      if (confirm("게시물을 삭제할까요?")) {
        var data = {
          itemUuid: this.items.itemUuid,
        };

        await this.axios({
          method: "put", // HTTP 메서드
          url: "/rest/delItem", // 요청할 URL
          data: data, // 전송할 데이터
          headers: {
            Accept: "application/json", // 서버로부터 JSON 응답을 기대
            "Content-Type": "application/json", // 요청의 콘텐츠 타입을 JSON으로 설정
          },
        })
          .then((res) => {
            var rst = res.data;

            if (rst.resultCode == "200") {
              alert("게시물이 삭제되었습니다.");
              this.$router.push("/board/notice");
            }
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
  },
  watch: {
    flag(v) {
      console.log(v);
    },
  },
};
</script>

<style lang="scss">
#details {
  .v-row {
    width: 95%;
    margin-left: 2em;
    margin-right: 2em;

    .v-col {
      .v-btn {
        margin: 0.2em;
      }

      #contents {
        min-height: 20em;
        font-family: "Pretendard", -apple-system, BlinkMacSystemFont, system-ui,
          Roboto, "Helvetica Neue", "Segoe UI", "Apple SD Gothic Neo",
          "Noto Sans KR", "Malgun Gothic", sans-serif;
      }

      #commentDrop {
        font-size: 16px;
        width: 100%;
      }
    }

    #itemTitle {
      text-align: left;
    }

    .info {
      text-align: right;
    }
  }
}
</style>
