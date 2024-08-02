export default {
  fnRuleChk(type) {
    var rules = null;
    var value = "";
    var chk = false;

    switch (type) {
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
  async init() {
    if (this.$loginInfo.login) {
      await this.axios
        .get("/rest/loginUserInfo")
        .then((res) => {
          this.beforeChkd = false;
          this.currPw = "";
          this.memId = res.data.memId;
          this.memPw = "";
          this.pwChk = "";
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
  fnResetEmail() {
    if (this.chk.emailChkd) {
      if (confirm("인증된 이메일 주소를 변경할까요?")) {
        this.chk.emailChkd = false;
        this.memEmail = "";
      }
    }
  },
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
  fnMailVerify() {
    var chk = this.chk.emailChkd;
    if (!chk) {
      chk = this.fnRuleChk(3); // check rules
      if (chk) this.overlay = true; // open overlay
    } else {
      if (confirm("인증이 완료된 메일을 변경할까요?")) {
        this.memEmail = "";
        this.chk.emailChkd = false;
      }
    }
  },
  fnChildMessage(obj) {
    this.overlay = obj.overlay;
    this.chk.emailChkd = obj.chkd;
  },
  async fnValidate() {
    let chk = await this.$refs.settingFrm.validate();

    if (chk.valid) {
      chk = !this.chk.emailChkd ? 1 : 0;
    } else {
      chk = !this.chk.pwChkd ? 2 : -1;
    }

    if (chk == 0) this.fnFrmSubmit();
    else if (chk == 1) alert("이메일을 인증해주세요.");
    else if (chk == 2) alert("비밀번호를 확인해주세요.");
    else alert("가입 정보를 다시 확인해주세요.");
  },
  async fnFrmSubmit() {
    if (confirm("회원 정보를 수정할까요?")) {
      var data = {
        memId: this.memId,
        memPw: this.memPw,
        memEmail: this.memEmail,
        memPhone: this.memPhone,
        zipcode: this.zipcode,
        memAddr1: this.memAddr1,
        memAddr2: this.memAddr2,
      };
      await this.axios({
        method: "post", // HTTP 메서드
        url: "/rest/member/settings", // 요청할 URL
        data: data, // 전송할 데이터
        headers: {
          Accept: "application/json", // 서버로부터 JSON 응답을 기대
          "Content-Type": "application/json", // 요청의 콘텐츠 타입을 JSON으로 설정
        },
      })
        .then((res) => {
          var resultCode = res.data.resultCode;

          if (resultCode == "200") {
            alert("정보 수정이 완료되었습니다.");
            this.init();
          } else if (resultCode == "400") {
            alert("회원 정보를 찾을 수 없습니다.");
            location.href = "/logout";
          } else {
            alert(
              "시스템 오류가 발생하였습니다. 지속될 경우 관리자에게 문의해주세요."
            );
            return false;
          }
        })
        .catch((err) => console.log(err));
    }
  },
  async fnDeleteUser() {
    if (confirm("rmfr에 가입된 정보를 삭제할까요?")) {
      await this.axios
        .get("/rest/member/delete")
        .then((res) => {
          var resultCode = res.data.resultCode;

          if (resultCode == 200) {
            alert("회원 탈퇴가 완료되었습니다.");
            this.init();
            this.$loginInfo.login = false;
            this.$loginInfo.token = null;
            this.$loginInfo.expired = null;
            location.href = "/logout";
          } else {
            alert(
              "시스템 오류가 발생하였습니다. 지속될 경우 관리자에게 문의해주세요."
            );
            return false;
          }
        })
        .catch((err) => {
          alert(
            "시스템 오류가 발생하였습니다. 지속될 경우 관리자에게 문의해주세요."
          );
          console.log(err);
          return false;
        });
    }
  },
};
