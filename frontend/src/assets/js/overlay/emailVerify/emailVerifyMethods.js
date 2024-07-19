let timer = null;
export default {
  async fnVerifyReset() {
    this.fnDelTimer(-1); // del timer
    this.loader = await true;
    if (await this.fnSendVerifyCode() /* send code */) {
      timer = this.fnSetTimer(); // set timer
      this.loader = await false;
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
};
