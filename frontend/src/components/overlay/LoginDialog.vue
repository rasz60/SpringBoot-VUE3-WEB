<script setup>
import VerifyDialog from "@/components/overlay/EmailVerifyDialog.vue";
</script>
<template>
  <v-overlay v-model="overlay" id="overlay" scroll-strategy="block" persistent>
    <VerifyDialog
      ref="verifyDialog"
      @sendMessage="fnChildMessage"
      :memEmail="find.memEmail"
    />
  </v-overlay>
  <v-card
    class="py-8 px-6 text-center mx-auto ma-4"
    min-width="400"
    width="100%"
  >
    <div class="d-flex">
      <v-spacer></v-spacer>
      <v-icon icon="mdi-close" @click="fnLoginDisplayReset" />
    </div>

    <h3 class="text-h5 mb-4">{{ cardTitle }}</h3>

    <v-sheet color="surface">
      <v-text-field
        variant="underlined"
        label="ID"
        v-show="!findId"
        v-model="login.memId"
      ></v-text-field>
      <v-text-field
        variant="underlined"
        label="Password"
        type="password"
        v-show="!findId"
        v-model="login.memPw"
      ></v-text-field>
      <v-text-field
        variant="underlined"
        label="Email"
        type="email"
        v-show="findId || findPw"
        v-model="login.memEmail"
        :append-icon="flag ? `mdi-email` : `mdi-email-outline`"
        @click:append="fnValid"
      ></v-text-field>
    </v-sheet>

    <div class="text-caption">
      <span class="findInfo" @click.stop="findId = true" v-if="!findId"
        >아이디 찾기</span
      >
      &nbsp;&nbsp;
      <span class="findInfo" @click.stop="findPw = true" v-if="!findPw"
        >비밀번호 찾기</span
      >
      &nbsp;&nbsp;
      <span
        class="findInfo"
        @click.stop="
          findId = false;
          findPw = false;
        "
        v-if="findId || findPw"
        >로그인 하기</span
      >
    </div>

    <v-btn
      class="my-4"
      color="primary"
      height="40"
      text="Login"
      variant="flat"
      width="70%"
    ></v-btn>
  </v-card>
</template>

<script>
export default {
  data() {
    return {
      login: {
        memId: "",
        memPw: "",
      },
      find: {
        memId: "",
        memEmail: "",
        flag: false,
      },
      cardTitle: "Login",
      findId: false,
      findPw: false,
      overlay: false,
    };
  },
  methods: {
    fnLoginDisplayReset() {
      this.$emit("sendMessage", { loginDisplay: false });
    },
    fnSend() {
      console.log("send");
    },
  },
  watch: {
    findId(v) {
      if (v) {
        this.findPw = false;
        this.cardTitle = "Find ID";
      } else {
        if (!this.findPw) this.cardTitle = "Login";
      }
    },
    findPw(v) {
      if (v) {
        this.findId = false;
        this.cardTitle = "Find Password";
      } else {
        if (!this.findId) this.cardTitle = "Login";
      }
    },
    fnChildMessage(obj) {
      this.overlay = obj.overlay;
      this.find.flag = obj.chkd;
    },
  },
};
</script>
