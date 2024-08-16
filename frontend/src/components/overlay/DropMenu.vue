<template>
  <v-sheet id="menu">
    <v-row>
      <v-spacer></v-spacer>
      <v-col cols="3" v-for="i in 3" :key="i">
        <v-list>
          <v-list-item
            v-for="menu in menus.filter((m) => {
              if (m.menuSection == i) return m;
            })"
            :key="menu"
            :class="menu.menuClass"
            :prepend-icon="menu.menuType == 1 ? 'mdi-plus' : ''"
            :link="menu.menuType == 1"
            @click="fnMove(menu.menuLink)"
          >
            {{ menu.menuName }}
          </v-list-item>
        </v-list>
      </v-col>
      <v-spacer></v-spacer>
    </v-row>
  </v-sheet>
</template>

<script>
export default {
  data() {
    return {
      menus: [],
    };
  },
  async created() {
    await this.getMenus();
  },
  computed: {},
  methods: {
    async getMenus() {
      await this.axios
        .get("/rest/menuList")
        .then((res) => {
          this.menus = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    fnMove(path) {
      if (path) {
        var msg = {
          menuDrop: false,
          path: path,
        };

        this.$emit("sendMessage", msg);
      }
    },
  },
  watch: {},
};
</script>

<style lang="scss">
.v-overlay__scrim {
  top: 6em !important;
}

#menu {
  margin-top: 5em;
  .v-row {
    margin: 0;
    width: 100vw;
  }

  .titles {
    background-color: RGBA(239, 239, 255, 0.4);
    font-weight: 600;
  }

  .subMenus {
    padding-left: 1.5em;
    padding-bottom: 0;
    padding-top: 0;
    min-height: 2.5em;

    .v-list-item__prepend {
      width: 2em;
      font-size: 10px;
    }
  }

  .subMenus:hover .v-list-item__prepend > i {
    animation: rotation 1s linear infinite;
  }

  @keyframes rotation {
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(360deg);
    }
  }
}
</style>
