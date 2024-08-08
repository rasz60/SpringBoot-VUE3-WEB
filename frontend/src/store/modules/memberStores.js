import { MEMBER } from "../mutation_types";

export const member = {
  namespaced: true,
  state: () => ({
    chk: false,
    msg: "",
    reg: {
      id: /^(?=.*[a-z0-9])[a-z0-9_-]{6,20}$/,
      pw: /(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/,
      mail: /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
    },
    regMsg: {
      id: "6~20자리의 영문소문자, 숫자, -, _ 조합으로 입력해주세요.",
      pw: "8~16자리의 영문 소/대문자, 숫자, 특수문자($,`,~,!,@,$,!,%,*,#,^,?,&,,(,),-,_,=,+) 조합으로 입력해주세요.",
      mail: "형식에 맞는 이메일 주소를 입력해주세요. (ex> emailId@domain.com)",
    },
  }),

  mutations: {
    [MEMBER.REG_CHK](state, param) {
      state.chk = state.reg[param.type].test(param.value.trim());
      if (!state.chk) state.msg = state.regMsg[param.type];
    },
  },

  getters: {
    getChk: (state) => {
      return state.chk ? true : state.msg;
    },
  },

  actions: {
    regChk: ({ commit }, param) => {
      commit(MEMBER.REG_CHK, param);
    },
  },
};
