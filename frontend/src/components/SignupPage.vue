<script setup>
import VerifyDialog from "@/components/overlay/EmailVerifyDialog.vue";
</script>
<template>
  <v-overlay v-model="overlay" id="overlay" scroll-strategy="block" persistent>
    <VerifyDialog
      ref="verifyDialog"
      @sendMessage="fnChildMessage"
      :memEmail="memEmail"
    />
  </v-overlay>
  <v-form @submit.prevent id="signup" ref="signupFrm">
    <v-row>
      <v-col cols="11">
        <v-text-field
          label="* ID"
          v-model="memId"
          :rules="idRules"
        ></v-text-field>
      </v-col>
      <v-col cols="1" class="btnCols">
        <v-btn
          :prepend-icon="
            chk.idDupChkd ? `mdi-account-check` : `mdi-account-check-outline`
          "
          :variant="chk.idDupChkd ? `tonal` : `elevated`"
          :color="chk.idDupChkd ? `primary` : ``"
          @click="fnIdDupChk"
          >중복확인</v-btn
        >
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-text-field
          type="password"
          label="* Password"
          v-model="memPw"
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
          v-model="memEmail"
          :rules="emailRules"
          :readonly="chk.emailChkd"
        ></v-text-field>
      </v-col>
      <v-col cols="1" class="btnCols">
        <v-btn
          :prepend-icon="
            chk.emailChkd ? `mdi-email-check` : `mdi-email-check-outline`
          "
          :variant="chk.emailChkd ? `tonal` : `elevated`"
          :color="chk.emailChkd ? `primary` : `default`"
          @click="fnMailVerify"
          :text="chk.emailChkd ? `인증완료` : `인증하기`"
        ></v-btn>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-text-field label="Phone" v-model="memPhone"></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="11">
        <v-text-field label="Zipcode" readonly v-model="zipcode"></v-text-field>
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
          v-model="memAddr1"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-text-field label="Address2" v-model="memAddr2"></v-text-field>
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
  created() {
    this.init();
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
      this.chk.pwChkd = v == this.memPw;
    },
  },
};
</script>
