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
          var resultCode = rst.resultCode;

          if (resultCode == 200) {
            var rstItem = rst.result.item;

            this.items.itemUuid = rstItem.itemUuid;
            if (rstItem.itemHeader != null)
              this.items.itemHeader = rstItem.itemHeader.itemHeaderId;
            this.items.itemHeaderName = rstItem.itemHeaderName;
            this.items.title = rstItem.itemTitle;
            this.items.contents = rstItem.itemContents;
            this.items.itemKeywords = rstItem.itemKeywords;
            if (this.items.itemKeywords) {
              this.items.hashtags = this.items.itemKeywords
                .substring(1)
                .split("#");
            }
            this.items.itemStatus = rstItem.itemStatus;
            this.items.itemLikesCnt = rstItem.itemLikesCnt;
            this.items.itemHitsCnt = rstItem.itemHitsCnt;
            this.items.itemCommentsCnt = rstItem.itemCommentsCnt;
            this.items.itemRegUuid = rstItem.itemRegUuid;
            this.items.itemRegDate = rstItem.itemRegDate.replace("T", " ");
            this.items.likeItem = rstItem.likeItem;
            this.items.eAuth = rstItem.eauth;
            this.items.dAuth = rstItem.dauth;
            this.items.cAuth = rstItem.cauth;
          } else {
            alert(rst.resultMessage);
            this.$router.push("/board/notice");
          }
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
