<template>
  <v-form @submit.prevent id="signup" ref="signupFrm">
    <v-row>
      <v-col cols="11">
        <v-text-field
          label="* ID"
          v-model="member.mId"
          :rules="idRules"
        ></v-text-field>
      </v-col>
      <v-col cols="1" class="btnCols">
        <v-btn prepend-icon="mdi-account-check-outline">중복확인</v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-text-field
          label="* Password"
          v-model="member.mPw"
          :rules="pwRules"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-text-field
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
          v-model="member.mEmail"
          :rules="emailRules"
        ></v-text-field>
      </v-col>
      <v-col cols="1" class="btnCols">
        <v-btn prepend-icon="mdi-email-check-outline">인증하기</v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-text-field label="Phone"></v-text-field>
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
        <v-btn prepend-icon="mdi-map-marker-outline">주소찾기</v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-text-field
          label="Address1"
          readonly
          v-model="member.mAddr1"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-text-field label="Address2" v-model="member.mAddr2"></v-text-field>
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
        mId: "",
        mPw: "",
        mEmail: "",
        mPhone: "",
        zipcode: "",
        mAddr1: "",
        mAddr2: "",
      },
      pwChk: "",
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
        if (v == this.member.mPw) return true;
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
  methods: {
    validate() {
      let chk = this.$refs.signupFrm.validate();
      if (chk.valid) this.frmSubmit();
    },

    frmSubmit() {
      if (confirm("회원으로 가입할까요?"))
        this.axios.post("/signup", JSON.stringify(this.member)).then().error();
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
