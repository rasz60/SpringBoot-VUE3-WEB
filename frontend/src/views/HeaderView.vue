<template>
  <v-overlay
    v-model="loginDisplay"
    id="overlay"
    scroll-strategy="block"
    persistent
  >
    <v-card
      class="py-8 px-6 text-center mx-auto ma-4"
      min-width="400"
      width="100%"
    >
      <div class="d-flex">
        <v-spacer></v-spacer>
        <v-icon icon="mdi-close" @click="fnLoginDisplayReset" />
      </div>

      <h3 class="text-h5 mb-4">LOGIN</h3>

      <v-sheet color="surface">
        <v-text-field variant="underlined" label="ID"></v-text-field>
        <v-text-field
          variant="underlined"
          label="Password"
          type="password"
        ></v-text-field>
      </v-sheet>

      <div class="text-caption">
        <span class="findInfo" @click.stop="findId = true">아이디 찾기</span>
        &nbsp;|&nbsp;
        <span class="findInfo" @click.stop="findPw = true">비밀번호 찾기</span>
      </div>

      <v-btn
        class="my-4"
        color="primary"
        height="40"
        text="Login"
        variant="flat"
        width="70%"
        @click="fnValidCode"
      ></v-btn>
    </v-card>
  </v-overlay>

  <v-layout id="header">
    <v-app-bar id="headerMenu">
      <template v-slot:prepend>
        <v-app-bar-title id="logo">
          <router-link to="/">
            <v-icon icon="mdi-alpha-r" class="logo-icons alpha" />
            <v-icon icon="mdi-alpha-m" class="logo-icons alpha" />
            <v-icon icon="mdi-alpha-f" class="logo-icons alpha" />
            <v-icon icon="mdi-alpha-r" class="logo-icons alpha" />
            <v-icon icon="mdi-help" class="logo-icons" />
          </router-link>
        </v-app-bar-title>
      </template>

      <template v-slot:append>
        <div id="buttonBox">
          <v-btn class="headerBtn" @click="$router.push('/signup')">
            <v-icon icon="mdi-account-plus"></v-icon>
            <v-tooltip location="bottom center" activator="parent">
              Signup
            </v-tooltip>
          </v-btn>

          <v-btn class="headerBtn" @click.stop="loginDisplay = !loginDisplay">
            <v-icon icon="mdi-key"></v-icon>
            <v-tooltip location="bottom center" activator="parent">
              login
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
      loginDisplay: false,
      findId: false,
      findPw: false,
    };
  },
  methods: {
    fnLoginDisplayReset() {
      this.loginDisplay = false;
      this.findId = false;
      this.findPw = false;
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
</style>
