export default {
  fnLoginDisplayReset() {
    this.$emit("sendMessage", { loginDisplay: false });
    this.findId = false;
    this.findPw = false;
    this.dpLogin = true;
  },
  async fnLogin() {
    if (
      this.loginChk[0](this.login.memId) == true &&
      this.loginChk[0](this.login.memPw) == true
    ) {
      let data = {
        memId: this.login.memId,
        memPw: this.login.memPw,
      };

      await this.axios
        .post("/rest/login", data)
        .then((res) => {
          if (res.status == 200) {
            // 로그인 창 닫기
            this.fnLoginDisplayReset();

            // 로그인 정보 localStorage 입력
            this.$loginInfo.login = true;
            this.$loginInfo.token = res.data;
            this.$loginInfo.expired =
              new Date().getTime() + 24 * 60 * 60 * 1000;

            if (this.$route.fullPath == "/") {
              this.$router.go(0); // referer화면이 root URL일 때는 새로고침
            } else {
              this.$router.push("/"); // root URL로 화면 전환
            }
          }
        })
        .catch((err) => {
          if (err.code == "ERR_BAD_REQUEST") {
            // 로그인 실패 메시지 (아이디, 비밀번호 확인)
            alert(err.response.data);
          } else {
            // 통신 오류
            alert("시스템 오류로 인해 로그인에 실패했습니다.");
          }
        });
    } else {
      alert("필수 입력사항을 입력해주세요.");
      return false;
    }
  },
  async fnValid() {
    await this.axios
      .get("/rest/findId/" + this.login.memEmail)
      .then((res) => {
        var cnt = res.data;

        if (cnt > 0) {
          this.find.memEmail = this.login.memEmail;
          this.overlay = true;
        } else {
          alert("가입되지 않은 이메일 주소입니다.");
          return false;
        }
      })
      .catch((err) => console.log(err));
  },
  async fnChildMessage(v) {
    this.overlay = v.overlay;
    this.verifyFlag = v.chkd;

    if (this.verifyFlag) {
      alert(
        "인증하신 메일로 rmfr에 가입된 아이디를 보내드렸습니다.\n메일에서 자세한 내용을 확인해주세요."
      );

      await this.axios
        .get("/rest/sendIdList/" + this.find.memEmail)
        .then((res) => {
          console.log(res);
          this.find.memEmail = "";
          this.login.memEmail = "";
        })
        .catch((err) => {
          console.log(err);
        });

      this.findId = false;
      return false;
    }
  },
};