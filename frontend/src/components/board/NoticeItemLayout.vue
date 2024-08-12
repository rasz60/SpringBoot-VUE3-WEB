<script setup>
import NoticeDetails from "./NoticeDetails.vue";
import NoticeForm from "./NoticeForm.vue";
</script>

<template>
  <v-layout id="noticeLayout">
    <div id="title">
      <h1>NoTiCe</h1>
    </div>
    <NoticeDetails
      v-if="flag == 'details' && items.itemUuid != ``"
      @sendMessage="fnSendMessage"
      :flag="flag"
      :seq="seq"
      :items="items"
    />
    <NoticeForm
      v-if="flag != 'details'"
      @sendMessage="fnSendMessage"
      :flag="flag"
      :seq="seq"
      :items="items"
    />
  </v-layout>
</template>

<script>
export default {
  name: "NoticeItemLayout",
  components: {
    NoticeDetails,
    NoticeForm,
  },
  data: () => ({
    flag: "",
    seq: "",
    readonly: false,
    items: {
      itemUuid: "",
      title: "",
      keywords: "",
      contents: "",
      itemStatus: 0,
      itemHeader: "",
      itemHeaderName: "",
      itemKeywords: "",
      itemRegUuid: "",
      itemRegDate: "",
      itemLikesCnt: 0,
      itemHitsCnt: 0,
      likeItem: false,
      headers: [],
      hashtags: [],
    },
  }),
  async created() {
    this.seq = this.$route.params.seq;
    this.flag = this.$route.params.flag;

    if (this.flag == "details") {
      await this.getDetails();
    }
  },
  methods: {
    fnSendMessage(v) {
      this.flag = v.flag;
      this.seq = v.seq;

      var param = this.flag ? "/" + this.flag : "";
      param += this.seq ? "/" + this.seq : "";

      this.$router.push("/board/notice" + param);
    },
    async getDetails() {
      var seq = this.$route.params.seq;
      await this.axios
        .get("/rest/item/" + seq)
        .then((res) => {
          var rst = res.data;

          console.log(rst);

          this.items.itemUuid = rst.itemUuid;
          if (rst.itemHeader != null)
            this.items.itemHeader = rst.itemHeader.itemHeaderId;
          this.items.itemHeaderName = rst.itemHeaderName;
          this.items.title = rst.itemTitle;
          this.items.contents = rst.itemContents;
          this.items.itemKeywords = rst.itemKeywords;
          if (this.items.itemKeywords) {
            this.items.hashtags = this.items.itemKeywords
              .substring(1)
              .split("#");
          }
          this.items.itemStatus = rst.itemStatus;
          this.items.itemLikesCnt = rst.itemLikesCnt;
          this.items.itemHitsCnt = rst.itemHitsCnt;
          this.items.itemCommentsCnt = rst.itemCommentsCnt;
          this.items.itemRegUuid = rst.itemRegUuid;
          this.items.itemRegDate = rst.itemRegDate.replace("T", " ");
          this.items.likeItem = rst.likeItem;
          this.items.eAuth = rst.eauth;
          this.items.dAuth = rst.dauth;
          this.items.cAuth = rst.cauth;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  watch: {
    async flag(v) {
      if (v == "edit") {
        await this.getDetails();
      }
    },
  },
};
</script>

<style lang="scss">
#noticeLayout {
  display: block;
  width: 100%;

  div#title {
    border-left: 6px solid red;
    padding-left: 2em;
    margin-left: 1em;
    margin-top: 1em;
    margin-bottom: 1.5em;
  }
}
</style>
