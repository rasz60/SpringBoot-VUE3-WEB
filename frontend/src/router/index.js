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

// 라우터 변경 시마다 화면을 뿌리기 전에 실행
router.beforeEach(() => {
  // local storage에 로그인 정보 가져오기
  var loginInfo = JSON.parse(localStorage.getItem("rmfrLoginInfo"));

  // 로그인 상태인 경우 진입
  if (loginInfo.login) {
    var today = new Date();

    // 만료일자가 현재 시간보다 크면 갱신
    if (loginInfo.expired > today.getTime()) {
      // 만료일자 현재 시간 +1 day
      loginInfo.expired = new Date().getTime() + 24 * 60 * 60 * 1000;
    }
    // 만료되었을 때
    else {
      // 로그인 정보 초기화
      loginInfo.login = false;
      loginInfo.credentials = null;
      loginInfo.expired = null;

      // 강제 로그아웃 안내 문구
      alert("로그인이 만료되었습니다. 다시 로그인 해주세요.");
      location.href = "/logout";
    }

    // 변경된 정보 local storage 저장
    localStorage.setItem("rmfrLoginInfo", JSON.stringify(loginInfo));
  }
});

export default router;
