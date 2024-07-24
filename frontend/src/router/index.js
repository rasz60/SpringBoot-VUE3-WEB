import { createRouter, createWebHistory } from "vue-router";
import MainPage from "@/components/MainPage.vue";
import SignupPage from "@/components/SignupPage.vue";

const routes = [
  {
    path: "/",
    name: "MainPage",
    component: MainPage,
  },
  {
    path: "/signup",
    name: "SignupPage",
    component: SignupPage,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  scrollBehavior() {
    return { top: 0 };
  },
  routes,
});

router.beforeEach(() => {
  var loginInfo = JSON.parse(localStorage.getItem("rmfrLoginInfo"));
  if (loginInfo != null && loginInfo.login) {
    var today = new Date();

    if (loginInfo.expired > today.getTime()) {
      loginInfo.expired = new Date().getTime() + 24 * 60 * 60 * 1000;
    } else {
      loginInfo.login = false;
      loginInfo.credentials = null;
      loginInfo.expired = null;
      alert("로그인이 만료되었습니다. 다시 로그인 해주세요.");
      location.href = "/logout";
    }
    localStorage.setItem("rmfrLoginInfo", JSON.stringify(loginInfo));
  }
});
/*
const isAuthenticationMember = (to, from, next) => {
  if (localStorage.getItem("login") == "true") {
    next();
  } else {
    alert("로그인이 필요합니다.");
    next(from);
  }
};
*/
export default router;
