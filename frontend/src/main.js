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

// 반응형 전역변수 선언
import { reactive, watchEffect } from "vue";

// 로그인 정보 전역 변수
const initLogin = JSON.parse(localStorage.getItem("login"));
const loginInfo = reactive(initLogin);

// loginInfo 변경 시, 값 유지
watchEffect(() => {
  localStorage.setItem("login", JSON.stringify(loginInfo));
});

app.config.globalProperties.$loginInfo = loginInfo;

/* vuetify Start */
import vuetify from "./plugins/vuetify";
/* vuetify End */

/* app Mount */
app.use(router).use(store).use(vuetify).mount("#app");
