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
let timer = null;
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
  methods: {
    async fnVerifyReset() {
      this.fnDelTimer(-1); // del timer
      if (await this.fnSendVerifyCode() /* send code */) {
        alert("인증번호를 재전송하였습니다.");
        timer = this.fnSetTimer(); // set timer
      }
    },
    /* send code start */
    async fnSendVerifyCode() {
      var chk = false;
      await this.axios
        .get("/rest/signup/verifyCode/" + this.mailAddr)
        .then((res) => {
          this.verifyCode = res.data.token;
          chk = true;
        })
        .catch(() => {
          alert("다시 시도해주세요.");
        });
      return chk;
    },
    /* send code end */
    /* timer start */
    fnSetTimer() {
      var time = this.limitTime;
      let interval = setInterval(function () {
        if (time == 0) {
          alert("인증시간이 만료되었습니다.");
          clearInterval(timer);
        }
        var timerSpan = document.querySelector("#timer");

        if (timerSpan && timerSpan != null) {
          var m = "0" + Math.floor(time / 60);
          var s = Math.floor(time % 60);
          s = s < 10 ? "0" + s : s;

          timerSpan.innerHTML = m + ":" + s;
          time--;
        } else {
          clearInterval(timer);
        }
      }, 1000);
      timer = interval;
    },
    fnDelTimer(type) {
      clearInterval(timer);
      if (type != -1) {
        this.verifyCode = "";
        this.$emit("sendMessage", { overlay: false, chkd: false });
      }
    },
    /* timer end */
    /* valid code start */
    fnValidCode() {
      var otp = window.btoa(this.otp);
      console.log(otp, this.verifyCode);
      if (this.verifyCode == otp) {
        alert("이메일 인증이 완료되었습니다.");
        this.fnDelTimer();
        this.$emit("sendMessage", { overlay: false, chkd: true });
      } else {
        alert("인증번호를 다시 확인해주세요.");
      }
    },
    /* valid code end */
  },
};
</script>
