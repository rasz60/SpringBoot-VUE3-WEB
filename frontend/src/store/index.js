import { createStore } from "vuex";

export default createStore({
  state: {},
  getters: {},
  mutations: {
    nullChk: (v, name) => {
      if (v) return true;
      else return name + "은(는) 필수 입력사항입니다.";
    },
  },
  actions: {},
  modules: {},
});
