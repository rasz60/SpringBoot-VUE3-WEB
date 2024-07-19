<template>
  <v-skeleton-loader
    class="py-8 px-6 text-center mx-auto ma-4"
    min-width="400"
    width="100%"
    type="card"
    v-if="loader"
  ></v-skeleton-loader>
  <v-card
    class="py-8 px-6 text-center mx-auto ma-4"
    max-width="400"
    width="100%"
    v-else
  >
    <div class="d-flex">
      <v-spacer></v-spacer>
      <v-icon icon="mdi-close" @click="fnDelTimer" />
    </div>

    <h3 class="text-h6 mb-4">Email Verified</h3>
    <div class="text-body-2">
      {{ mailAddr }}로 발송된 인증번호를 <br />아래 칸에 입력해주세요.
    </div>

    <div class="py-3">
      <span id="timer">02:59</span>
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
</template>

<script>
import emailVerifyMethods from "@/assets/js/overlay/emailVerify/emailVerifyMethods";

export default {
  props: ["memEmail"],
  data() {
    return {
      limitTime: 179,
      verifyCode: "",
      otp: "",
      mailAddr: this.memEmail,
      loader: true,
    };
  },
  async created() {
    var chk = await this.fnSendVerifyCode();
    if (chk) this.fnSetTimer();
    this.loader = false;
  },
  methods: emailVerifyMethods,
};
</script>
