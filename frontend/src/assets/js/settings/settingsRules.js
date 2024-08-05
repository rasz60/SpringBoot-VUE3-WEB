import { mapGetters } from "vuex";
export default {
  ...mapGetters("member", ["getChk"]),
  pwRules() {
    const rules = [];

    const pwNullChk = (v) => {
      this.nullChk(v);
      return this.getChk;
    };
    rules.push(pwNullChk);

    const regchk = (v) => {
      var param = { type: "pw", value: v };
      this.regChk(param);
      return this.getChk;
    };
    rules.push(regchk);

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

    const mailNullChk = (v) => {
      this.nullChk(v);
      return this.getChk;
    };
    rules.push(mailNullChk);

    const regchk = (v) => {
      var param = { type: "mail", value: v };
      this.regChk(param);
      return this.getChk;
    };
    rules.push(regchk);

    return rules;
  },
};
