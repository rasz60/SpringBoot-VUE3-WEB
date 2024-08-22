export default {
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
};
