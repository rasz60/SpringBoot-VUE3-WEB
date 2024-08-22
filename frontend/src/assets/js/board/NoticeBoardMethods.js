export default {
  async getBoardList() {
    var pg = this.page != 0 ? this.page - 1 : 0;
    var url =
      "/rest/boardList/" + this.itemStatus + "/" + pg + "/" + this.limit;
    await this.axios
      .get(url)
      .then((res) => {
        var rst = res.data;
        var resultCode = rst.resultCode;

        if (resultCode == 200) {
          var rstList = rst.result.boardList;
          this.list = rstList.content;

          if (rstList.content.length > 0) {
            this.page = rstList.pageable.pageNumber + 1;
            this.totalPages = rstList.totalPages;
            this.notContents = false;
          } else {
            this.page = 1;
            this.totalPages = 1;
            this.notContents = true;
          }
        } else {
          alert(rst.resultMessage);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  },
  fnMove(itemSeq) {
    var path = itemSeq != null ? "details/" + itemSeq : "from/0";
    this.$router.push("./notice/" + path);
  },
};
