import { COMMON } from "@/store/mutation_types";
/* Axios Start */
import axios from "axios";
// http header 설정 (get / post / put / delete)
axios.defaults.headers.get["Content-Type"] = "application/json";
axios.defaults.headers.post["Content-Type"] = "application/json";
axios.defaults.headers.put["Content-Type"] = "application/json";
axios.defaults.headers.delete["Content-Type"] = "application/json";

// Axios 인스턴스 생성
const servCall = axios.create({
  baseURL: "http://localhost:8082", // 서버 URL
  withCredentials: true, // 자격 증명(쿠키) 전달을 허용
});

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
    async [COMMON.LOGIN_CHK](state) {
      await servCall
        .get("/rest/loginUserInfo")
        .then((res) => {
          var data = res.data;
          console.log(data, state.loginInfo);
          /*
          if (data != null) {
          }
          */
        })
        .catch((err) => {
          console.log(err);
        });
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
    loginChk: ({ commit }) => {
      commit(COMMON.LOGIN_CHK);
    },
  },
};
