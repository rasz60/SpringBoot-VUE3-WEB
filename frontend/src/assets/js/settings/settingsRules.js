export default {
  pwRules() {
    const rules = [];
    const pwChk = (v) => {
      if (!v) return true;
      else {
        var regExp =
          /(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/;

        if (regExp.test(v)) return true;
        return "8~16자리의 영문 소/대문자, 숫자, 특수문자($,`,~,!,@,$,!,%,*,#,^,?,&,,(,),-,_,=,+) 조합으로 입력해주세요.";
      }
    };
    rules.push(pwChk);
    return rules;
  },
  pwChkRules() {
    const rules = [];

    const pwChk = (v) => {
      if (this.memPw == v) {
        this.chk.pwChkd = true;
        return true;
      } else return "비밀번호를 확인해주세요.";
    };
    rules.push(pwChk);

    return rules;
  },
  emailRules() {
    const rules = [];

    const nullchk = (v) => {
      if (v) return true;
      return "이메일은 필수 입력사항입니다.";
    };
    rules.push(nullchk);

    const regchk = (v) => {
      var regExp =
        /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      if (regExp.test(v)) return true;
      return "형식에 맞는 이메일 주소를 입력해주세요. (ex> emailId@domain.com)";
    };
    rules.push(regchk);

    return rules;
  },
};
