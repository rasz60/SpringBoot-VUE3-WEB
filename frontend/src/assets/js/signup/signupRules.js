export default {
  idRules() {
    const rules = [];

    const idNullChk = (v) => {
      if (v) return true;
      return "아이디는 필수 입력사항입니다.";
    };
    rules.push(idNullChk);

    const idRegChk = (v) => {
      var regExp = /^(?=.*[a-z0-9])[a-z0-9_-]{6,20}$/;

      if (regExp.test(v.trim())) return true;
      return "6~20자리의 영문소문자, 숫자, -, _ 조합으로 입력해주세요.";
    };

    rules.push(idRegChk);

    return rules;
  },
  pwRules() {
    const rules = [];
    const nullchk = (v) => {
      if (v) return true;
      return "비밀번호는 필수 입력사항입니다.";
    };
    rules.push(nullchk);

    const regchk = (v) => {
      var regExp =
        /(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/;

      if (regExp.test(v)) return true;
      return "8~16자리의 영문 소/대문자, 숫자, 특수문자($,`,~,!,@,$,!,%,*,#,^,?,&,,(,),-,_,=,+) 조합으로 입력해주세요.";
    };
    rules.push(regchk);

    return rules;
  },
  pwChkRules() {
    const rules = [];

    const nullchk = (v) => {
      if (v) return true;
      else return "비빌번호를 확인해주세요.";
    };
    rules.push(nullchk);

    const pwChk = (v) => {
      if (this.memPw != "" && v == this.memPw) {
        this.chk.pwChkd = true;
        return true;
      } else {
        return "비밀번호를 확인해주세요.";
      }
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
