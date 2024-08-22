export default {
  fnMove(flag, seq) {
    var param = {
      flag: flag,
      seq: seq,
    };

    this.$emit("sendMessage", param);
  },
  async fnLike(id, idx) {
    var cmmChk = id != null; // true - comments / false - boardItem
    var likeYn = cmmChk ? this.comments[idx].likeComment : this.likeItem;

    var method = likeYn ? `delete` : `post`;
    var url = likeYn ? `/rest/item/delLike` : `/rest/item/addLike`;

    var uuid = id ? id : this.items.itemUuid;

    await this.axios({
      method: method, // HTTP 메서드
      url: url, // 요청할 URL
      data: uuid, // 전송할 데이터
      headers: {
        Accept: "application/json", // 서버로부터 JSON 응답을 기대
        "Content-Type": "text/plain", // 요청의 콘텐츠 타입을 JSON으로 설정
      },
    })
      .then((res) => {
        var rst = res.data;
        var resultCode = rst.resultCode;

        if (resultCode == 200) {
          var cnt = rst.result.itemLikes;

          if (cmmChk) {
            // comment 일 때
            var like = this.comments[idx].likeComment;
            this.comments[idx].likeComment =
              rst.resultCode == 200 ? !like : like;
            this.comments[idx].commentLikeCnt = cnt;
          } else {
            // boardItem 일 때
            this.itemLikes = cnt;
            this.likeItem =
              rst.resultCode == 200 ? !this.likeItem : this.likeItem;
          }
        } else {
          alert(rst.resultMessage);
        }
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
          alert(rst.resultMessage);
          if (rst.resultCode != "500") {
            this.$router.push("/board/notice");
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  },
  async fnAddReply() {
    var itemUuid = this.items.itemUuid;
    var pId = "";
    var depth = 0;

    if (this.commentsTarget != null) {
      if (this.commentEdit) {
        // 댓글 수정
        pId = this.commentsTarget.parentComment.commentUuid;
        depth = this.commentsTarget.commentDepth;
      } else {
        // 대댓글 작성
        pId = this.commentsTarget.commentUuid;
        depth = Number(this.commentsTarget.commentDepth) + 1;

        if (depth > 5) {
          alert("더 이상의 대댓글을 작성할 수 없습니다.");
          return false;
        }
      }
    }

    var data = {
      itemUuid: itemUuid,
      commentParentUuid: pId,
      commentContents: this.commentContents,
      commentDepth: depth,
    };

    await this.axios
      .post("/rest/addReply", data)
      .then((res) => {
        var rst = res.data;
        var resultCode = rst.resultCode;

        alert(rst.resultMessage);
        if (resultCode == "200") this.$router.go(0);
      })
      .catch((err) => {
        console.log(err);
      });
  },
  async getReplies(itemId, parentId, idx) {
    if (parentId != null && this.comments[idx].childOpen) {
      this.childCmmCtrl(1, idx);
      return false;
    }

    var param = "/" + itemId;
    param += parentId ? "/" + parentId : "/0";

    await this.axios
      .get("/rest/getReplies" + param)
      .then((res) => {
        var rst = res.data;
        var resultCode = rst.resultCode;

        if (resultCode == 200) {
          var cmms = rst.result.comments;

          if (parentId) {
            this.childCmmCtrl(0, idx, cmms);
          } else {
            this.comments = cmms;
          }
        } else {
          alert(rst.resultMessage);
        }
      })
      .catch((err) => {
        console.log(err);
      });
  },
  fnEditComment(idx) {
    var target = this.comments[idx];

    this.commentContents = target.commentContents;

    this.commentsTarget = target.parentComment;
    this.commentEdit = true;

    document.querySelector("#comment").focus();
  },
  fnReplyTarget(idx) {
    this.commentsTarget = this.comments[idx];
    this.commentEdit = false;

    document.querySelector("#comment").focus();
  },
  async fnDelComment(idx) {
    var delUuid = this.comments[idx].commentUuid;

    await this.axios({
      method: "put", // HTTP 메서드
      url: "/rest/delComment", // 요청할 URL
      data: delUuid, // 전송할 데이터
      headers: {
        Accept: "application/json", // 서버로부터 JSON 응답을 기대
        "Content-Type": "text/plain", // 요청의 콘텐츠 타입을 JSON으로 설정
      },
    })
      .then((res) => {
        var rst = res.data;
        var resultCode = rst.resultCode;

        alert(rst.resultMessage);
        if (resultCode == 200) {
          this.$router.go(0);
        } else if (resultCode == 401) {
          this.$router.push("/");
        }
      })
      .catch((err) => {
        console.log(err);
      });
  },
  childCmmCtrl(type, idx, rst) {
    var nx = idx + 1;
    var tmp = [];

    // 더보기 댓글 열기
    if (type == 0) {
      // 선택한 댓글의 다음 index 부터 반복
      for (var i = nx; i - nx < rst.length; i++) {
        this.comments.splice(i, 0, rst[i - nx]); // rst 배열 값을 하나씩 추가
      }
      this.comments[idx].childOpen = true; // 선택한 댓글의 자식 댓글 오픈 여부 변경
    }
    // 더보기 댓글 닫기, 하위 댓글까지 모두 닫기
    else {
      var childCnt = 0; // 하위 대댓글 개수
      tmp.push(this.comments[idx]); // 닫기 대상이 되는 부모 댓글 배열에 추가

      // 선택한 댓글 다음 index 부터 반복
      for (var j = nx; j < this.comments.length; j++) {
        var pCmm = this.comments[j].parentComment; // 해당 index의 부모 댓글

        // 부모 댓글이 있을 때
        if (pCmm != null) {
          // tmp 배열 안에 있는 부모 댓글과 일치하는지 확인
          for (var k = 0; k < tmp.length; k++) {
            if (tmp[k].commentUuid == pCmm.commentUuid) {
              // 일치하면 해당 댓글의 자식 댓글도 모두 닫혀야하므로 해당 댓글을 대상 부모 댓글 배열에 추가
              tmp.push(this.comments[j]);
              childCnt++;
              break;
            }
          }
        }
      }
      this.comments.splice(nx, childCnt); // 선택 댓글 다음 index 부터 자식 댓글 개수 만큼 잘라내기
      this.comments[idx].childOpen = false; // 선택한 댓글의 자식 댓글 오픈 여부 변경
    }
  },
};
