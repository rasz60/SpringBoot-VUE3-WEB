<template>
  <v-form id="form" ref="boardFrm">
    <div id="title">
      <h1>NoTiCe</h1>
    </div>

    <v-row>
      <v-col cols="2">
        <v-select
          label="말머리"
          v-model="itemHeader"
          :readonly="readonly"
          :items="headers"
          variant="underlined"
        ></v-select>
      </v-col>
      <v-col cols="10">
        <v-text-field
          type="text"
          label="제목"
          variant="underlined"
          :readonly="readonly"
          v-model="title"
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
          :readonly="readonly"
          v-model="contents"
          :rules="contentRules"
        ></v-textarea>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <v-text-field
          v-if="!readonly"
          variant="underlined"
          :readonly="readonly"
          type="text"
          label="#tags"
          @keyup="fnAddTags"
          v-model="keywords"
        ></v-text-field>
      </v-col>
    </v-row>

    <v-row id="keywords">
      <v-col cols="12">
        <v-chip
          v-for="tag in hashtags"
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
          v-if="!readonly"
          variant="tonal"
          @click="fnValidate(0)"
          >임시저장</v-btn
        >
        <v-btn
          prepend-icon="mdi-content-save-all"
          color="primary"
          v-if="!readonly"
          variant="tonal"
          @click="fnValidate(1)"
          >등록하기</v-btn
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
  name: "NoticeForm",
  data: () => ({
    title: "",
    readonly: false,
    keywords: "",
    contents: "",
    itemStatus: 0,
    itemHeader: "",
    itemKeywords: "",
    headers: [],
    hashtags: [],
  }),
  created() {
    var flag = this.$route.params.flag;
    this.readonly = flag == "details";
    if (flag != "form") {
      this.getDetails();
    } else {
      this.getHeaders();
    }
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
      this.title = "";
      this.keywords = "";
      this.contents = "";
      this.itemStatus = 0;
      this.itemHeader = "";
      this.itemKeywords = "";
      this.hashtags = [];
    },
    async getHeaders() {
      await this.axios
        .get("/rest/itemHeaders")
        .then((res) => {
          var data = res.data;
          for (var i = 0; i < data.length; i++) {
            this.headers[i] = {
              title: data[i].itemHeaderName,
              value: data[i].itemHeaderId,
            };
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    fnMove(seq) {
      this.$router.push("/board/notice" + (seq ? "/" + seq : ""));
    },
    fnAddTags($event) {
      var k = $event.keyCode;

      if (k == 13) {
        if (!this.fnKeywordChk()) {
          this.keywords = "";
          return false;
        }

        if (this.hashtags.length < 30) {
          this.itemKeywords += "#" + this.keywords.trim();
          this.keywords = "";
        } else {
          alert("해시태그를 30개 이상 등록할 수 없습니다.");
        }
      } else {
        return false;
      }
    },
    fnKeywordChk() {
      var chk = true;
      var t = this.keywords.trim();

      if (t.includes("#")) {
        alert("해시태그는 '#'을 포함할 수 없습니다.");
        return false;
      }

      var bytes = this.getByteSize(this.itemKeywords + "#" + t);

      if (bytes > 1000) {
        alert("모든 해시태그를 합쳐 1000bytes를 초과할 수 없습니다.");
        return false;
      }

      if (t) {
        for (var i = 0; i < this.hashtags.length; i++) {
          if (this.hashtags[i] == t) {
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
      for (var i = 0; i < this.hashtags.length; i++) {
        if (this.hashtags[i] != k) {
          tmp += "#" + this.hashtags[i];
        }
      }
      this.itemKeywords = tmp;
    },
    async fnValidate(status) {
      var chk = await this.$refs.boardFrm.validate();

      if (chk.valid) {
        this.itemStatus = status;

        var data = {
          itemTitle: this.title,
          itemHeaderId: this.itemHeader,
          itemStatus: this.itemStatus,
          itemKeywords: this.itemKeywords,
          itemContents: this.contents,
        };
        await this.axios({
          method: "post", // HTTP 메서드
          url: "/rest/board/reg", // 요청할 URL
          data: data, // 전송할 데이터
          headers: {
            Accept: "application/json", // 서버로부터 JSON 응답을 기대
            "Content-Type": "application/json", // 요청의 콘텐츠 타입을 JSON으로 설정
          },
        })
          .then((res) => {
            var rst = res.data.resultCode;
            if (rst == "200") {
              alert("게시물 등록이 완료되었습니다.");
              this.initValue();
              this.$router.push("/board/notice");
            }
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
    async getDetails() {
      var seq = this.$route.params.seq;
      await this.axios
        .get("/rest/item/" + seq)
        .then((res) => {
          var rst = res.data;

          this.itemHeader = rst.itemHeader.itemHeaderName;
          this.title = rst.itemTitle;
          this.contents = rst.itemContents;
          this.itemKeywords = rst.itemKeywords;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  watch: {
    itemKeywords(v) {
      if (v) this.hashtags = v.substring(1).split("#");
      else this.hashtags = [];
    },
  },
};
</script>

<style>
#form {
  width: 100%;

  div#title {
    border-left: 6px solid red;
    padding-left: 2em;
    margin-left: 1em;
    margin-top: 1em;
    margin-bottom: 1.5em;
  }
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
