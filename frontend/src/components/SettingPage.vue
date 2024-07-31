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
  <v-form @submit.prevent id="settings" ref="settingFrm">
    <v-row v-show="!beforeChkd">
      <v-col cols="11">
        <v-text-field
          type="password"
          label="Current Password"
          v-model="currPw"
        ></v-text-field>
      </v-col>
      <v-col cols="1" class="btnCols">
        <v-btn prepend-icon="" @click="fnCurrPwChkd">확인하기</v-btn>
      </v-col>
    </v-row>
    <v-row v-show="beforeChkd">
      <v-col cols="12">
        <v-text-field
          label="* ID"
          v-model="memId"
          readonly="true"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row v-show="beforeChkd">
      <v-col cols="12">
        <v-text-field
          type="password"
          label="* Password"
          v-model="memPw"
          :rules="pwRules"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row v-show="beforeChkd">
      <v-col cols="12">
        <v-text-field
          type="password"
          label="* Password Check"
          v-model="pwChk"
          :rules="pwChkRules"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row v-show="beforeChkd">
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
    <v-row v-show="beforeChkd">
      <v-col cols="12">
        <v-text-field label="Phone" v-model="memPhone"></v-text-field>
      </v-col>
    </v-row>
    <v-row v-show="beforeChkd">
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
    <v-row v-show="beforeChkd">
      <v-col cols="12">
        <v-text-field
          label="Address1"
          readonly
          v-model="memAddr1"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row v-show="beforeChkd">
      <v-col cols="12">
        <v-text-field label="Address2" v-model="memAddr2"></v-text-field>
      </v-col>
    </v-row>
    <v-row v-show="beforeChkd">
      <v-spacer></v-spacer>
      <v-col cols="2" class="text-right">
        <v-btn prepend-icon="mdi-account-edit-outline" @click="fnValidate"
          >수정하기</v-btn
        >
      </v-col>
      <v-col cols="2" class="text-left">
        <v-btn
          prepend-icon="mdi-account-remove-outline"
          color="red"
          @click="fnDeleteUser"
          >탈퇴하기</v-btn
        >
      </v-col>
      <v-spacer></v-spacer>
    </v-row>
  </v-form>
</template>

<script>
import settingsData from "@/assets/js/settings/settingsData";
import signupRules from "@/assets/js/signup/signupRules";

export default {
  name: "SignupPage",
  data() {
    return settingsData;
  },
  created() {
    this.init();
  },
  mounted() {
    this.fnLoadDaumPostcodeScript();
  },
  computed: signupRules,
  methods: {
    async init() {
      if (this.$loginInfo.login) {
        await this.axios
          .get("/rest/loginUserInfo")
          .then((res) => {
            this.memId = res.data.memId;
            this.memEmail = res.data.memEmail;
            this.memPhone = res.data.memPhone;
            this.zipcode = res.data.zipcode;
            this.memAddr1 = res.data.memAddr1;
            this.memAddr2 = res.data.memAddr2;
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        alert("다시 로그인해주세요.");
        location.href = "/logout";
      }
    },
    async fnCurrPwChkd() {
      if (this.currPw) {
        var data = {
          memPw: this.currPw,
        };
        await this.axios
          .post("/rest/currPwChkd", JSON.stringify(data), {
            headers: { "Content-Type": "application/json" },
          })
          .then((res) => {
            var chk = res.data;

            if (chk) {
              this.currPw = "";
              this.beforeChkd = true;
            } else {
              alert("비밀번호를 다시 확인해주세요.");
            }
          })
          .catch((err) => {
            alert(
              "시스템 오류가 발생하였습니다. 지속될 경우 관리자에게 문의해주세요."
            );
            console.log(err);
          });
      } else {
        alert("비밀번호를 입력해주세요.");
      }
    },
    fnExecDaumPostcode() {},
    fnLoadDaumPostcodeScript() {},
    fnMailVerify() {},
    fnValidate() {},
    fnDeleteUser() {},
  },
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
