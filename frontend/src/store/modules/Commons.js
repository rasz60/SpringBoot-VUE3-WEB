import { COMMON } from "@/store/mutation_types";

export const common = {
  namespaced: true,
  state: () => ({
    chk: false,
    loginInfo: {
      login: false,
      token: null,
      expired: null,
    },
    msg: "",
  }),

  mutations: {
    [COMMON.NULL_CHK](state, v) {
      state.chk = !(v == "" || v == null);
      if (!state.chk) state.msg = "필수 입력 값을 입력해주세요.";
    },
  },

  getters: {
    getCommChk: (state) => {
      return state.chk ? true : state.msg;
    },
  },

  actions: {
    nullChk: ({ commit }, v) => {
      commit(COMMON.NULL_CHK, v);
    },
  },
};
