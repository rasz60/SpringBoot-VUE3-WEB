/* create App Start */
import { createApp } from "vue";
import App from "./App.vue";
const app = createApp(App);
/* create App End */

/*  router Start */
import router from "./router";
/*  router End */

/* vuex Start */
import store from "./store";
/* vuex End */

/* Axios Start */
import axios from "axios";
// http header 설정 (get / post / put / delete)
axios.defaults.headers.get["Content-Type"] = "application/json";
axios.defaults.headers.post["Content-Type"] = "application/json";
axios.defaults.headers.put["Content-Type"] = "application/json";
axios.defaults.headers.delete["Content-Type"] = "application/json";
// app 내에서 전역변수 선언
app.config.globalProperties.axios = axios;
/* Axios End */

/* loginInfo 반응형 전역변수 선언 Start */
import { reactive, watchEffect } from "vue";

// 로그인 정보 초기 값
var initLogin = { login: false, token: null, expired: null };

// localStorage에 저장된 loginInfo get
var storedInfo = JSON.parse(localStorage.getItem("rmfrLoginInfo"));

// localStorage에 loginInfo가 있으면 loginInfo를, 아니면 초기 값 설정
var info = storedInfo == null ? initLogin : storedInfo;
const loginInfo = reactive(info);

// loginInfo 변경 시 localStorage 변수 갱신
watchEffect(() => {
  localStorage.setItem("rmfrLoginInfo", JSON.stringify(loginInfo));
});

// loginInfo 전역 변수 선언
app.config.globalProperties.$loginInfo = loginInfo;
/* loginInfo 반응형 전역변수 선언 End */

/* vuetify Start */
import vuetify from "./plugins/vuetify";
/* vuetify End */

/* app Mount */
app.use(router).use(store).use(vuetify).mount("#app");
