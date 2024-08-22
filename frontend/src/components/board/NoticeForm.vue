<template>
  <!-- form -->
  <v-form id="form" ref="boardFrm">
    <v-row>
      <v-col cols="2">
        <v-select
          label="말머리"
          v-model="item.itemHeader"
          :items="headers"
          variant="underlined"
        ></v-select>
      </v-col>
      <v-col cols="10">
        <v-text-field
          type="text"
          label="제목"
          variant="underlined"
          v-model="item.title"
          :rules="titleRules"
        ></v-text-field>
      </v-col>
    </v-row>
    <v-row>
      <v-col cols="12">
        <v-textarea
          label="내용"
          rows="10"
          variant="outlined"
          v-model="item.contents"
          :rules="contentRules"
        ></v-textarea>
      </v-col>
    </v-row>

    <v-row>
      <v-col cols="12">
        <v-text-field
          variant="underlined"
          type="text"
          label="#tags"
          @keyup="fnAddTags"
          v-model="item.keywords"
        ></v-text-field>
      </v-col>
    </v-row>

    <v-row id="keywords">
      <v-col cols="12">
        <v-chip
          v-for="tag in item.hashtags"
          :key="tag"
          @click="fnTagDel(tag)"
          color="success"
          variant="tonal"
        >
          <v-icon icon="mdi-pound" start></v-icon>
          {{ tag }}
          <v-icon icon="mdi-close-circle" end></v-icon>
        </v-chip>
      </v-col>
    </v-row>
    <v-row>
      <v-spacer></v-spacer>
      <v-col cols="3" class="text-right">
        <v-btn
          prepend-icon="mdi-content-save-all-outline"
          color="secondary"
          v-if="item.itemStatus == 0"
          variant="tonal"
          @click="fnValidate(0)"
          >임시저장</v-btn
        >
        <v-btn
          prepend-icon="mdi-content-save-all"
          color="primary"
          variant="tonal"
          @click="fnValidate(1)"
          >저장하기</v-btn
        >
        <v-btn
          prepend-icon="mdi-format-list-bulleted-square"
          color="default"
          variant="tonal"
          @click="fnMove()"
          >목록으로</v-btn
        >
      </v-col>
    </v-row>
  </v-form>
</template>

<script>
import NoticeBoardFormDatas from "@/assets/js/board/form/NoticeBoardFormDatas";
import NoticeBoardFormComputed from "@/assets/js/board/form/NoticeBoardFormComputed";
import NoticeBoardFormMethods from "@/assets/js/board/form/NoticeBoardFormMethods";

export default {
  props: {
    flag: {
      type: String,
      required: true,
    },
    seq: {
      type: String,
      required: true,
    },
    items: {
      type: Map,
      required: true,
    },
  },
  name: "NoticeForm",
  data() {
    return NoticeBoardFormDatas;
  },
  async created() {
    await this.getHeaders();
    this.item = this.items;
  },
  computed: NoticeBoardFormComputed,
  methods: NoticeBoardFormMethods,
  watch: {
    "item.itemKeywords"(v) {
      if (v) this.item.hashtags = v.substring(1).split("#");
      else this.item.hashtags = [];
    },
  },
};
</script>

<style lang="scss" scoped>
#form {
  width: 100%;

  .v-row {
    width: 95%;
    margin-left: 2em;
    margin-right: 2em;

    .v-col {
      text-align: center;

      .v-btn {
        margin: 0.2em;
      }
    }
  }

  #keywords {
    margin-top: 0;
    overflow-x: auto;
    .v-col {
      text-align: left;
    }
  }

  .v-chip {
    margin: 0.2em;
  }
}
</style>
