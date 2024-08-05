import { mapActions } from "vuex";

export default {
  ...mapActions("member", ["nullChk", "regChk"]),
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
      if (chk) this.overlay = true; // open overlay
    } else {
      if (confirm("인증이 완료된 메일을 변경할까요?")) {
        this.memEmail = "";
        this.otp = "";
        this.verifyCode = "";
        this.chk.emailChkd = false;
      }
    }
  },

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
  fnChildMessage(obj) {
    this.overlay = obj.overlay;
    this.chk.emailChkd = obj.chkd;
  },
};
