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

    <v-sheet color="surface" class="mb-4">
      <v-text-field
        variant="underlined"
        label="ID"
        v-show="!findId"
        v-model="login.memId"
        :rules="loginChk"
      ></v-text-field>
      <v-text-field
        variant="underlined"
        label="Password"
        type="password"
        v-show="!findId"
        v-model="login.memPw"
        :rules="loginChk"
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
      @click="fnLogin"
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
  computed: {
    loginChk() {
      const rules = [];

      const nullChk = (v) => {
        if (v) return true;
        return "필수 입력사항입니다.";
      };

      rules.push(nullChk);
      return rules;
    },
  },
  methods: {
    fnLoginDisplayReset() {
      this.$emit("sendMessage", { loginDisplay: false });
    },
    async fnLogin() {
      if (
        this.loginChk[0](this.login.memId) == true &&
        this.loginChk[0](this.login.memPw) == true
      ) {
        let data = {
          memId: this.login.memId,
          memPw: this.login.memPw,
        };

        await this.axios
          .post("/rest/login", data)
          .then((res) => {
            if (res.data) {
              localStorage.setItem("login", res.data);
              this.$router.go(0);
            }
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        alert("필수 입력사항을 입력해주세요.");
        return false;
      }
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
  },
};
</script>
