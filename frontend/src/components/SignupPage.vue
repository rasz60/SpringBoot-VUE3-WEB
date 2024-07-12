<template>
  <v-form @submit.prevent id="signup" ref="signupFrm">
    <v-row>
      <v-col cols="11">
        <v-text-field
          label="* ID"
          v-model="member.memId"
          :rules="idRules"
        ></v-text-field>
      </v-col>
      <v-col cols="1" class="btnCols">
        <v-btn prepend-icon="mdi-account-check-outline" @click="fnIdDupChk"
          >중복확인</v-btn
        >
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-text-field
          type="password"
          label="* Password"
          v-model="member.memPw"
          :rules="pwRules"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-text-field
          type="password"
          label="* Password Check"
          v-model="pwChk"
          :rules="pwChkRules"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="11">
        <v-text-field
          label="* e-mail"
          v-model="member.memEmail"
          :rules="emailRules"
        ></v-text-field>
      </v-col>
      <v-col cols="1" class="btnCols">
        <v-btn prepend-icon="mdi-email-check-outline">인증하기</v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-text-field label="Phone" v-model="member.memPhone"></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="11">
        <v-text-field
          label="Zipcode"
          readonly
          v-model="member.zipcode"
        ></v-text-field>
      </v-col>
      <v-col cols="1" class="btnCols">
        <v-btn
          prepend-icon="mdi-map-marker-outline"
          @click.stop="execDaumPostcode"
          >주소찾기</v-btn
        >
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-text-field
          label="Address1"
          readonly
          v-model="member.memAddr1"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-text-field label="Address2" v-model="member.memAddr2"></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-spacer></v-spacer>
      <v-col cols="2">
        <v-btn prepend-icon="mdi-account-edit-outline" @click="validate"
          >회원가입</v-btn
        >
      </v-col>
      <v-spacer></v-spacer>
    </v-row>
  </v-form>
</template>

<script>
export default {
  name: "SignupPage",
  data() {
    return {
      member: {
        memId: "",
        memPw: "",
        memEmail: "",
        memPhone: "",
        zipcode: "",
        memAddr1: "",
        memAddr2: "",
      },
      pwChk: "",
      chk: {
        idDupChkd: false,
        pwChkd: false,
        emailChkd: false,
      },
    };
  },
  computed: {
    idRules() {
      const rules = [];

      const idNullChk = (v) => {
        if (v) return true;
        return "아이디는 필수 입력사항입니다.";
      };
      rules.push(idNullChk);

      const idRegChk = (v) => {
        var regExp = /^(?=.*[a-z0-9])[a-z0-9_-]{6,20}$/;

        if (regExp.test(v.trim())) return true;
        return "6~20자리의 영문소문자, 숫자, -, _ 조합으로 입력해주세요.";
      };

      rules.push(idRegChk);

      return rules;
    },
    pwRules() {
      const rules = [];
      const nullchk = (v) => {
        if (v) return true;
        return "비밀번호는 필수 입력사항입니다.";
      };
      rules.push(nullchk);

      const regchk = (v) => {
        var regExp =
          /(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/;

        if (regExp.test(v)) return true;
        return "8~16자리의 영문 소/대문자, 숫자, 특수문자($,`,~,!,@,$,!,%,*,#,^,?,&,,(,),-,_,=,+) 조합으로 입력해주세요.";
      };
      rules.push(regchk);

      return rules;
    },
    pwChkRules() {
      const rules = [];

      const nullchk = (v) => {
        if (v) return true;
        return "비빌번호를 확인해주세요.";
      };
      rules.push(nullchk);

      const pwChk = (v) => {
        if (v == this.member.memPw) return true;
        return "비밀번호를 확인해주세요.";
      };
      rules.push(pwChk);

      return rules;
    },
    emailRules() {
      const rules = [];

      const nullchk = (v) => {
        if (v) return true;
        return "이메일은 필수 입력사항입니다.";
      };
      rules.push(nullchk);

      const regchk = (v) => {
        var regExp =
          /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if (regExp.test(v)) return true;
        return "형식에 맞는 이메일 주소를 입력해주세요. (ex> emailId@domain.com)";
      };
      rules.push(regchk);

      return rules;
    },
  },
  mounted() {
    this.loadDaumPostcodeScript();
  },
  methods: {
    fnIdDupChk() {
      var chk = false;
      for (var i = 0; i < this.idRules.length; i++) {
        chk = this.idRules[i](this.member.memId);
      }

      if (chk) {
        this.axios
          .get("/signup/idDupChk/" + this.member.memId)
          .then((res) => {
            if (res.data > 0) {
              alert("중복되는 아이디가 존재합니다.");
            } else {
              alert("사용 가능한 아이디 입니다.");
              this.idDupChkd = true;
            }
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        alert("형식에 맞는 id를 입력해주세요.");
      }
    },
    // 다음 주소 api script tag 추가
    loadDaumPostcodeScript() {
      const script = document.createElement("script");
      // 다음 주소 api cdn
      script.src =
        "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js";
      script.onload = () => {
        this.isScriptLoaded = true; // 스크립트가 로드되면 isScriptLoaded를 true로 설정
      };
      document.head.appendChild(script);
    },

    // 다음 주소 검색 호출
    execDaumPostcode() {
      if (window.daum && window.daum.Postcode) {
        // 팝업 호출
        this.popup = new window.daum.Postcode({
          oncomplete: (data) => {
            // 우편번호 검색 완료 후의 처리 로직
            this.member.zipcode = data.zonecode;
            this.member.memAddr1 = data.address;
          },
        });

        this.popup.open();
      }
      // 오류 처리
      else {
        console.error("Daum Postcode 스크립트가 로드되지 않았습니다.");
      }
    },
    async validate() {
      let chk = await this.$refs.signupFrm.validate();

      chk = chk.valid ? 0 : -1;

      if (chk == 0) this.frmSubmit();
      else if (chk == 1) alert("아이디 중복을 확인해주세요.");
      else if (chk == 2) alert("비밀번호를 확인해주세요.");
      else if (chk == 3) alert("이메일 인증을 확인해주세요.");
      else alert("가입 정보를 다시 확인해주세요.");
    },

    async frmSubmit() {
      if (confirm("회원으로 가입할까요?")) {
        await this.axios
          .post("/signup", this.member)
          .then((res) => {
            alert(res.data + "님의 가입을 환영합니다!");
          })
          .catch((err) => console.log(err));
      }
    },
  },
  watch: {
    memId() {
      this.idDupChk = false;
    },
    memPw(v) {
      this.chk.pwChkd = v == this.pwChk;
    },
    pwChk(v) {
      this.chk.pwChkd = v == this.member.memPw;
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style>
form#signup {
  margin-top: 1em;
  width: 70%;

  .btnCols {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: -1.5em;
  }
}
</style>
