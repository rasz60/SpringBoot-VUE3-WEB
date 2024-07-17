let timer = null;
export default {
  init() {
    this.memId = "";
    this.memPw = "";
    this.memEmail = "";
    this.memPhone = "";
    this.zipcode = "";
    this.memAddr1 = "";
    this.memAddr2 = "";
    this.pwChk = "";
    this.chk.idDupChkd = false;
    this.chk.pwChkd = false;
    this.chk.emailChkd = false;
    this.overlay = false;
    this.limitTime = 179;
    this.verifyCode = "";
    this.otp = "";
  },
  fnRuleChk(type) {
    var rules = null;
    var value = "";
    var chk = false;

    switch (type) {
      case 0:
        rules = this.idRules;
        value = this.memId;
        break;
      case 1:
        rules = this.pwRules;
        value = this.memPw;
        break;
      case 2:
        rules = this.pwChkRules;
        value = this.pwChk;
        break;
      default:
        rules = this.emailRules;
        value = this.memEmail;
        break;
    }

    for (var i = 0; i < rules.length; i++) {
      chk = rules[i](value);
      if (chk != true) {
        alert(chk);
        chk = false;
        break;
      }
    }

    return chk;
  },
  fnIdDupChk() {
    var chk = this.fnRuleChk(0);

    if (chk) {
      this.axios
        .get("/rest/signup/idDupChk/" + this.memId)
        .then((res) => {
          if (res.data > 0) {
            alert("중복되는 아이디가 존재합니다.");
          } else {
            alert("사용 가능한 아이디 입니다.");
            this.chk.idDupChkd = true;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    } else {
      alert("형식에 맞는 id를 입력해주세요.");
    }
  },
  async fnMailVerify() {
    var chk = this.chk.emailChkd;
    if (!chk) {
      chk = this.fnRuleChk(3); // check rules
      if (chk) chk = await this.fnSendVerifyCode(); // send code
      if (chk) this.overlay = true; // open overlay
      if (chk) timer = this.fnSetTimer(); // set timer
    } else {
      if (confirm("인증이 완료된 메일을 변경할까요?")) {
        this.memEmail = "";
        this.otp = "";
        this.verifyCode = "";
        this.chk.emailChkd = false;
      }
    }
  },
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
      .get("/rest/signup/verifyCode/" + this.memEmail)
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
      var m = "0" + Math.floor(time / 60);
      var s = Math.floor(time % 60);
      s = s < 10 ? "0" + s : s;
      timerSpan.innerHTML = m + ":" + s;
      time--;
    }, 1000);
    return interval;
  },
  fnDelTimer(type) {
    clearInterval(timer);
    if (type != -1) {
      this.verifyCode = "";
      this.overlay = false;
    }
  },
  /* timer end */
  /* valid code start */
  fnValidCode() {
    var otp = window.btoa(this.otp);
    console.log(otp, this.verifyCode);
    if (this.verifyCode == otp) {
      alert("이메일 인증이 완료되었습니다.");
      this.chk.emailChkd = true;
      this.fnDelTimer();
    } else {
      alert("인증번호를 다시 확인해주세요.");
    }
  },
  /* valid code end */
  // 다음 주소 api script tag 추가
  fnLoadDaumPostcodeScript() {
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
  fnExecDaumPostcode() {
    if (window.daum && window.daum.Postcode) {
      // 팝업 호출
      this.popup = new window.daum.Postcode({
        oncomplete: (data) => {
          // 우편번호 검색 완료 후의 처리 로직
          this.zipcode = data.zonecode;
          this.memAddr1 = data.address;
        },
      });

      this.popup.open();
    }
    // 오류 처리
    else {
      console.error("Daum Postcode 스크립트가 로드되지 않았습니다.");
    }
  },
  async fnValidate() {
    let chk = await this.$refs.signupFrm.validate();

    if (chk.valid) {
      chk = !this.chk.idDupChkd ? 1 : !this.chk.emailChkd ? 3 : 0;
    } else {
      chk = !this.chk.pwChkd ? 2 : -1;
    }

    if (chk == 0) this.fnFrmSubmit();
    else if (chk == 1) alert("아이디 중복을 확인해주세요.");
    else if (chk == 2) alert("비밀번호를 확인해주세요.");
    else if (chk == 3) alert("이메일을 인증해주세요.");
    else alert("가입 정보를 다시 확인해주세요.");
  },

  async fnFrmSubmit() {
    if (confirm("회원으로 가입할까요?")) {
      var data = {
        memId: this.memId,
        memPw: this.memPw,
        memEmail: this.memEmail,
        memPhone: this.memPhone,
        zipcode: this.zipcode,
        memAddr1: this.memAddr1,
        memAddr2: this.memAddr2,
      };
      await this.axios
        .post("/rest/signup", data)
        .then((res) => {
          alert(res.data + "님의 가입을 환영합니다!");
          this.$router.push("/");
        })
        .catch((err) => console.log(err));
    }
  },
};