<script setup>
import NoticeDetails from "./NoticeDetails.vue";
import NoticeForm from "./NoticeForm.vue";
</script>

<template>
  <v-layout id="noticeLayout">
    <div id="title">
      <h1>NoTiCe</h1>
    </div>
    <NoticeDetails
      v-if="flag == 'details' && items.itemUuid != ``"
      @sendMessage="fnSendMessage"
      :flag="flag"
      :seq="seq"
      :items="items"
    />
    <NoticeForm
      v-if="flag != 'details'"
      @sendMessage="fnSendMessage"
      :flag="flag"
      :seq="seq"
      :items="items"
    />
  </v-layout>
</template>

<script>
import NoticeItemLayoutDatas from "@/assets/js/board/layout/NoticeItemLayoutDatas";
import NoticeItemLayoutMethods from "@/assets/js/board/layout/NoticeItemLayoutMethods";

export default {
  name: "NoticeItemLayout",
  components: {
    NoticeDetails,
    NoticeForm,
  },
  data: () => NoticeItemLayoutDatas,
  async created() {
    this.seq = this.$route.params.seq;
    this.flag = this.$route.params.flag;

    if (this.flag == "details") {
      await this.getDetails();
    }
  },
  methods: NoticeItemLayoutMethods,
  watch: {
    async flag(v) {
      if (v == "edit") {
        await this.getDetails();
      }
    },
  },
};
</script>

<style lang="scss">
#noticeLayout {
  display: block;
  width: 100%;

  div#title {
    border-left: 6px solid red;
    padding-left: 2em;
    margin-left: 1em;
    margin-top: 1em;
    margin-bottom: 1.5em;
  }
}
</style>
