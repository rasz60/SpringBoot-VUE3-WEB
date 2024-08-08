import { COMMON } from "@/store/mutation_types";

export const common = {
  namespaced: true,
  state: () => ({
    chk: false,
    msg: "",
  }),

  mutations: {
    [COMMON.NULL_CHK](state, v) {
      state.chk = !(v == "" || v == null);
      if (!state.chk) state.msg = "필수 입력 값을 입력해주세요.";
    },
  },

  getters: {
    getChk: (state) => {
      return state.chk ? true : state.msg;
    },
  },

  actions: {
    nullChk: ({ commit }, v) => {
      commit(COMMON.NULL_CHK, v);
    },
  },
};
