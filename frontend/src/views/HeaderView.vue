<template>
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
        </div>
      </template>
    </v-app-bar>
  </v-layout>
</template>

<script>
export default {};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#header {
  width: 98.3%;
  height: 5em;
  border-bottom: 1px solid #ececec;
  overflow: visible !important;
  position: fixed !important;

  #headerMenu {
    height: 100%;
    box-shadow: none;
  }
  .v-toolbar__content {
    margin: auto 0;
  }
  a {
    text-decoration: none;
    cursor: pointer;
  }

  .toggleBtn {
    margin-right: 10px;
  }

  #logo {
    padding: 5px;
    height: 80%;
    background-color: red;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 20px 20px 20px 0;

    .logo-icons {
      color: white;
      padding: 13px;
      font-size: 1em;
    }
    .logo-icons.alpha {
      font-size: 1.7em;
    }
  }

  #buttonBox {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: end;
    align-items: center;

    .headerBtn {
      height: 100%;
      border-radius: 2em;
      font-size: 1.3em;
    }
  }
}
</style>
