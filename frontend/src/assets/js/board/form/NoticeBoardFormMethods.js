import { mapActions } from "vuex";

export default {
  ...mapActions("common", ["nullChk"]),
  initValue() {
    this.item.title = "";
    this.item.keywords = "";
    this.item.contents = "";
    this.item.itemStatus = 0;
    this.item.itemHeader = "";
    this.item.itemKeywords = "";
    this.item.hashtags = [];
  },
  async getHeaders() {
    await this.axios
      .get("/rest/itemHeaders")
      .then((res) => {
        var rst = res.data;
        var resultCode = rst.resultCode;

        if (resultCode == 200) {
          var data = rst.result.itemHeaders;
          for (var i = 0; i < data.length; i++) {
            this.headers[i] = {
              title: data[i].itemHeaderName,
              value: data[i].itemHeaderId,
            };
          }
        } else {
          console.log(resultCode);
          alert(rst.resultMessage);
          if (resultCode > 400 && resultCode < 500) location.href = "/logout";
        }
      })
      .catch((err) => {
        console.log(err);
      });
  },
  fnMove(flag, seq) {
    var param = "";

    param += flag ? "/" + flag : "";
    param += seq ? "/" + seq : "";

    this.$router.push("/board/notice" + param);
  },
  fnAddTags($event) {
    var k = $event.keyCode;
    console.log($event.keyCode);
    if (k == 13) {
      if (!this.fnKeywordChk()) {
        this.item.keywords = "";
        return false;
      }

      if (this.item.hashtags.length < 30) {
        this.item.itemKeywords += "#" + this.item.keywords.trim();
        this.item.keywords = "";
      } else {
        alert("해시태그를 30개 이상 등록할 수 없습니다.");
      }
    } else {
      return false;
    }
  },
  fnKeywordChk() {
    var chk = true;
    var t = this.item.keywords.trim();

    if (t.includes("#")) {
      alert("해시태그는 '#'을 포함할 수 없습니다.");
      return false;
    }

    var bytes = this.getByteSize(this.item.itemKeywords + "#" + t);

    if (bytes > 1000) {
      alert("모든 해시태그를 합쳐 1000bytes를 초과할 수 없습니다.");
      return false;
    }

    if (t) {
      for (var i = 0; i < this.item.hashtags.length; i++) {
        if (this.item.hashtags[i] == t) {
          chk = false;
          break;
        }
      }
    } else {
      chk = false;
    }
    return chk;
  },
  getByteSize(str) {
    let byteSize = 0;
    for (let i = 0; i < str.length; i++) {
      const code = str.charCodeAt(i);
      if (code <= 0x7f) {
        byteSize += 1; // ASCII 문자 (1 바이트)
      } else if (code <= 0x7ff) {
        byteSize += 2; // 2 바이트
      } else if (code <= 0xffff) {
        byteSize += 3; // 3 바이트
      } else {
        byteSize += 4; // 4 바이트 (JavaScript에서 UTF-16 서러게이트 페어 사용)
      }
    }
    return byteSize;
  },
  fnTagDel(k) {
    var tmp = "";
    for (var i = 0; i < this.item.hashtags.length; i++) {
      if (this.item.hashtags[i] != k) {
        tmp += "#" + this.item.hashtags[i];
      }
    }
    this.item.itemKeywords = tmp;
  },
  async fnValidate(status) {
    var chk = await this.$refs.boardFrm.validate();

    if (chk.valid) {
      var data = {
        itemUuid: this.item.itemUuid,
        itemTitle: this.item.title,
        itemHeaderId: this.item.itemHeader,
        itemStatus: status,
        itemKeywords: this.item.itemKeywords,
        itemContents: this.item.contents,
      };
      await this.axios({
        method: "post", // HTTP 메서드
        url: "/rest/board/save", // 요청할 URL
        data: data, // 전송할 데이터
        headers: {
          Accept: "application/json", // 서버로부터 JSON 응답을 기대
          "Content-Type": "application/json", // 요청의 콘텐츠 타입을 JSON으로 설정
        },
      })
        .then((res) => {
          var resultCode = res.data.resultCode;
          alert(res.data.resultMessage);
          if (resultCode == 200) {
            this.initValue();
            this.$router.push("/board/notice");
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  },
};
