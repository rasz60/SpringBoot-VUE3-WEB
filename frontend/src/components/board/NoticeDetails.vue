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
          @click="fnLike()"
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
              : `댓글 보기 (` + items.itemCommentsCnt + `)`
          "
        >
        </v-btn>
      </v-col>
      <v-divider></v-divider>
    </v-row>
    <v-row v-show="commentDrop">
      <v-col :cols="items.cAuth ? `11` : `12`">
        <v-chip
          id="commentTo"
          v-if="commentsTarget != null"
          prepend-icon="mdi-at"
          variant="tonal"
          color="purple"
          @click="commentsTarget = null"
        >
          {{ commentsTarget.commentRegUuid.memId }} 님에게 댓글 남기기
        </v-chip>
        <v-textarea
          rows="1"
          id="comment"
          prepend-icon="mdi-forum-outline"
          type="text"
          :label="
            items.cAuth
              ? commentEdit
                ? `댓글 수정...`
                : `댓글...`
              : `댓글을 작성하려면 로그인해주세요.`
          "
          :readonly="!items.cAuth"
          v-model="commentContents"
          variant="underlined"
        >
        </v-textarea>
      </v-col>
      <v-col cols="1">
        <v-btn
          prepend-icon="mdi-forum-outline"
          @click="fnAddReply"
          v-if="items.cAuth"
          >작성하기</v-btn
        >
      </v-col>
      <v-divider></v-divider>
    </v-row>
    <v-row
      v-show="commentDrop"
      v-for="(cmm, idx) in comments"
      :key="cmm"
      :class="`comments depth-` + cmm.commentDepth"
      @click="fnReplyTarget(idx)"
    >
      <v-col cols="2"> @{{ cmm.commentRegUuid.memId }}</v-col>
      <v-col cols="9">
        <v-icon
          v-if="cmm.commentDepth > 0"
          icon="mdi-arrow-right-bottom"
          size="small"
        />
        {{ cmm.commentContents }}&nbsp;
        <span class="rdate">
          {{ cmm.commentUpdateDate.replace("T", " ") }}
          {{ cmm.editYn ? `수정됨` : `` }}
        </span>
        <v-icon
          v-if="cmm.eauth && cmm.commentStatus == 0"
          icon="mdi-pencil-outline"
          size="small"
          @click.stop="fnEditComment(idx)"
        />
        <v-icon
          v-if="cmm.dauth && cmm.commentStatus == 0"
          icon="mdi-close"
          size="small"
          color="red"
          @click.stop="fnDelComment(idx)"
        />
        <v-btn
          variant="plain"
          :text="
            cmm.childOpen
              ? `대댓글 닫기`
              : `더보기(` + cmm.commentChildCnt + `)`
          "
          @click.stop="getReplies(items.itemUuid, cmm.commentUuid, idx)"
          v-if="cmm.childYn"
        />
      </v-col>
      <v-col cols="1">
        <v-icon
          :icon="cmm.likeComment ? `mdi-heart` : `mdi-heart-outline`"
          :color="cmm.likeComment ? `red` : ``"
          size="small"
          @click.stop="fnLike(cmm.commentUuid, idx)"
        />
        {{ cmm.commentLikeCnt }}
      </v-col>
    </v-row>
  </v-sheet>
</template>

<script>
import NoticeDetailsDatas from "@/assets/js/board/details/NoticeDetailsDatas";
import NoticeDetailsMethods from "@/assets/js/board/details/NoticeDetailsMethods";

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
    return NoticeDetailsDatas;
  },
  async created() {
    await this.getReplies(this.items.itemUuid, null, null);
  },
  mounted() {
    this.itemHeaderName = this.items.itemHeaderName
      ? "[" + this.items.itemHeaderName + "]"
      : "";
    this.itemLikes = this.items.itemLikesCnt;
    this.likeItem = this.items.likeItem;
  },
  methods: NoticeDetailsMethods,
};
</script>
