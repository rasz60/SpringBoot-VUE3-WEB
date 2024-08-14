<template>
  <!-- form -->
  <v-form id="form" ref="boardFrm">
    <v-row>
      <v-col cols="2">
        <v-select
          label="말머리"
          v-model="item.itemHeader"
          :items="headers"
          variant="underlined"
        ></v-select>
      </v-col>
      <v-col cols="10">
        <v-text-field
          type="text"
          label="제목"
          variant="underlined"
          v-model="item.title"
          :rules="titleRules"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-textarea
          label="내용"
          rows="10"
          variant="outlined"
          v-model="item.contents"
          :rules="contentRules"
        ></v-textarea>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <v-text-field
          variant="underlined"
          type="text"
          label="#tags"
          @keyup="fnAddTags"
          v-model="item.keywords"
        ></v-text-field>
      </v-col>
    </v-row>

    <v-row id="keywords">
      <v-col cols="12">
        <v-chip
          v-for="tag in item.hashtags"
          :key="tag"
          @click="fnTagDel(tag)"
          color="success"
          variant="tonal"
        >
          <v-icon icon="mdi-pound" start></v-icon>
          {{ tag }}
          <v-icon icon="mdi-close-circle" end></v-icon>
        </v-chip>
      </v-col>
    </v-row>
    <v-row>
      <v-spacer></v-spacer>
      <v-col cols="3" class="text-right">
        <v-btn
          prepend-icon="mdi-content-save-all-outline"
          color="secondary"
          v-if="item.itemStatus == 0"
          variant="tonal"
          @click="fnValidate(0)"
          >임시저장</v-btn
        >
        <v-btn
          prepend-icon="mdi-content-save-all"
          color="primary"
          variant="tonal"
          @click="fnValidate(1)"
          >저장하기</v-btn
        >
        <v-btn
          prepend-icon="mdi-format-list-bulleted-square"
          color="default"
          variant="tonal"
          @click="fnMove()"
          >목록으로</v-btn
        >
      </v-col>
    </v-row>
  </v-form>
</template>

<script>
import { mapGetters, mapActions } from "vuex";

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
  name: "NoticeForm",
  data() {
    return {
      item: {
        itemUuid: "",
        title: "",
        keywords: "",
        contents: "",
        itemStatus: 0,
        itemHeader: "",
        itemHeaderName: "",
        itemKeywords: "",
        itemLikesCnt: 0,
        itemHitsCnt: 0,
        hashtags: [],
      },
      headers: [],
    };
  },
  async created() {
    await this.getHeaders();
    this.item = this.items;
  },
  computed: {
    ...mapGetters("common", ["getChk"]),
    titleRules() {
      const rules = [];

      const notNulls = (v) => {
        this.nullChk(v);
        return this.getChk;
      };
      rules.push(notNulls);

      const byteChk = (v) => {
        if (this.getByteSize(v) <= 200) return true;
        else return "200 bytes를 초과할 수 없습니다.";
      };
      rules.push(byteChk);

      return rules;
    },
    contentRules() {
      const rules = [];

      const notNulls = (v) => {
        this.nullChk(v);
        return this.getChk;
      };
      rules.push(notNulls);

      return rules;
    },
  },
  methods: {
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
            alert(rst.resultMessage);
            if (resultCode == 400) location.href = "/logout";
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
  },
  watch: {
    "item.itemKeywords"(v) {
      if (v) this.item.hashtags = v.substring(1).split("#");
      else this.item.hashtags = [];
    },
  },
};
</script>

<style lang="scss">
#form {
  width: 100%;

  .v-row {
    width: 95%;
    margin-left: 2em;
    margin-right: 2em;

    .v-col {
      text-align: center;

      .v-btn {
        margin: 0.2em;
      }
    }
  }

  #keywords {
    margin-top: 0;
    overflow-x: auto;
    .v-col {
      text-align: left;
    }
  }

  .v-chip {
    margin: 0.2em;
  }
}
</style>
