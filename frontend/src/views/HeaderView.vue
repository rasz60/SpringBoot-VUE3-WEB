<script setup>
import LoginDialog from "@/components/overlay/LoginDialog.vue";
import DropMenu from "@/components/overlay/DropMenu.vue";
</script>
<template>
  <v-overlay
    v-model="loginDisplay"
    id="overlay"
    scroll-strategy="block"
    persistent
  >
    <LoginDialog @sendMessage="setLoginDisplay" />
  </v-overlay>

  <v-overlay v-model="menuDrop" id="menuOverlay" scroll-strategy="block">
    <DropMenu @sendMessage="setMenuDrop" />
  </v-overlay>

  <v-layout id="header">
    <v-app-bar id="headerMenu">
      <template v-slot:prepend>
        <v-app-bar-title id="logo">
          <a @click="fnMove('/')">
            <v-icon icon="mdi-alpha-r" class="logo-icons alpha" />
            <v-icon icon="mdi-alpha-m" class="logo-icons alpha" />
            <v-icon icon="mdi-alpha-f" class="logo-icons alpha" />
            <v-icon icon="mdi-alpha-r" class="logo-icons alpha" />
            <v-icon icon="mdi-help" class="logo-icons" />
          </a>
        </v-app-bar-title>
        <div id="drop">
          <v-btn
            prepend-icon="mdi-plus"
            id="menuDropdown"
            @click="fnOverlay(0)"
            text="Menus"
          />
        </div>
      </template>

      <template v-slot:append>
        <div id="buttonBox">
          <v-btn
            class="headerBtn"
            @click="fnMove('/signup')"
            v-show="!loginFlag"
          >
            <v-icon icon="mdi-account-plus"></v-icon>
            <v-tooltip location="bottom center" activator="parent">
              Signup
            </v-tooltip>
          </v-btn>

          <v-btn
            class="headerBtn"
            @click="fnMove('/settings')"
            v-show="loginFlag"
          >
            <v-icon icon="mdi-account-cog"></v-icon>
            <v-tooltip location="bottom center" activator="parent">
              Settings
            </v-tooltip>
          </v-btn>

          <v-btn
            class="headerBtn"
            @click.stop="fnOverlay(1)"
            v-show="!loginFlag"
          >
            <v-icon icon="mdi-key"></v-icon>
            <v-tooltip location="bottom center" activator="parent">
              login
            </v-tooltip>
          </v-btn>

          <v-btn class="headerBtn" @click.stop="fnLogout" v-show="loginFlag">
            <v-icon icon="mdi-logout"></v-icon>
            <v-tooltip location="bottom center" activator="parent">
              logout
            </v-tooltip>
          </v-btn>
        </div>
      </template>
    </v-app-bar>
  </v-layout>
</template>

<script>
export default {
  name: "headerView",
  data() {
    return {
      menuDrop: false,
      loginDisplay: false,
      loginFlag: false,
    };
  },
  created() {
    this.loginFlag = this.$loginInfo.login;
  },
  methods: {
    setLoginDisplay(obj) {
      this.menuDrop = false;
      this.loginDisplay = obj.loginDisplay;
    },
    setMenuDrop(obj) {
      this.menuDrop = obj.menuDrop;
      if (obj.path != "") this.$router.push(obj.path);
    },
    fnLogout() {
      // 로컬 스토리지 loginInfo 초기화
      this.$loginInfo.login = false;
      this.$loginInfo.token = null;
      this.$loginInfo.expired = null;
      location.href = "/logout"; // spring security logout URL로 이동
    },
    fnOverlay(type) {
      // type = 0 : menuDrop, 1 : loginDisplay
      if (type == 0) {
        this.menuDrop = !this.menuDrop;
        this.loginDisplay = false;
      } else {
        this.menuDrop = false;
        this.loginDisplay = !this.loginDisplay;
      }
    },
    fnMove(path) {
      this.menuDrop = false;
      this.$router.push(path);
    },
  },
};
</script>

<style>
.findInfo {
  color: darkblue;
  text-decoration: underline;
}

.findInfo:hover {
  color: purple;
  cursor: pointer;
}
#drop {
  color: #1d1d1d;
  margin-left: 0.3em;
  transition: transform 0.5s ease-in-out;
}
#drop:hover .v-btn__prepend {
  animation: rotation 2s linear infinite;
}

@keyframes rotation {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>
