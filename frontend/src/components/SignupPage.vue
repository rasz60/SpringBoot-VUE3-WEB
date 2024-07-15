<template>
  <v-overlay v-model="overlay" id="overlay" scroll-strategy="block" persistent>
    <v-card
      class="py-8 px-6 text-center mx-auto ma-4"
      max-width="400"
      width="100%"
    >
      <div class="d-flex">
        <v-spacer></v-spacer>
        <v-icon icon="mdi-close" @click="fnDelTimer" />
      </div>

      <h3 class="text-h6 mb-4">Email Verified</h3>
      <div class="text-body-2">
        {{ member.memEmail }}로 발송된 인증번호를 <br />아래 칸에 입력해주세요.
      </div>

      <div class="py-3">
        <span id="timer">03:00</span>
      </div>

      <v-sheet color="surface">
        <v-otp-input v-model="otp" type="text" variant="solo"></v-otp-input>
      </v-sheet>

      <v-btn
        class="my-4"
        color="purple"
        height="40"
        text="Verify"
        variant="flat"
        width="70%"
        @click="fnValidCode"
      ></v-btn>

      <div class="text-caption">
        인증번호를 받지 못했나요?
        <a href="#" @click="fnVerifyReset">다시 발송하기</a>
      </div>
    </v-card>
  </v-overlay>
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
          :readonly="chk.emailChkd"
        ></v-text-field>
      </v-col>
      <v-col cols="1" class="btnCols">
        <v-btn
          :prepend-icon="
            chk.emailChkd ? `mdi-email-check` : `mdi-email-check-outline`
          "
          @click="fnMailVerify"
          :text="chk.emailChkd ? `인증완료` : `인증하기`"
        ></v-btn>
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
          @click.stop="fnExecDaumPostcode"
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
        <v-btn prepend-icon="mdi-account-edit-outline" @click="fnValidate"
          >회원가입</v-btn
        >
      </v-col>
      <v-spacer></v-spacer>
    </v-row>
  </v-form>
</template>

<script>
import signupData from "@/assets/js/signup/signupData";
import signupRules from "@/assets/js/signup/signupRules";
import signupMethods from "@/assets/js/signup/signupMethods";

export default {
  name: "SignupPage",
  data() {
    return signupData;
  },
  mounted() {
    this.fnLoadDaumPostcodeScript();
  },
  computed: signupRules,
  methods: signupMethods,
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
