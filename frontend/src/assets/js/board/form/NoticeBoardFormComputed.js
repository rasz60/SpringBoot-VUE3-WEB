import { mapGetters } from "vuex";

export default {
  ...mapGetters("common", ["getCommChk"]),
  titleRules() {
    const rules = [];

    const notNulls = (v) => {
      this.nullChk(v);
      return this.getChk;
    };
    rules.push(notNulls);

    const byteChk = (v) => {
      if (this.getByteSize(v) <= 200) return true;
      else return "200 bytes를 초과할 수 없습니다.";
    };
    rules.push(byteChk);

    return rules;
  },
  contentRules() {
    const rules = [];

    const notNulls = (v) => {
      this.nullChk(v);
      return this.getChk;
    };
    rules.push(notNulls);

    return rules;
  },
};
