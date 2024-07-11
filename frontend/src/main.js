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

/* vuetify Start */
import vuetify from "./plugins/vuetify";
/* vuetify End */

/* app Mount */
app.use(router).use(store).use(vuetify).mount("#app");
