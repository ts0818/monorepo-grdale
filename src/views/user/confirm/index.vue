<template>
  <div>
    <el-form ref="form" :model="formDataModel" label-width="120px" size="mini">
      <div>{{ formDataModel.lastName }}</div>
      <div>{{ formDataModel.firstName }}</div>
      <div>{{ formDataModel.age }}</div>
      <div>{{ formDataModel.birthday }}</div>
      <el-form-item size="large">
        <el-button type="primary" @click="goSave">登録</el-button>
        <el-button @click="goBackEdit">戻る</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
/* eslint-disable */
/* eslint-disable no-console */
import { Component, Vue, Watch } from "vue-property-decorator";
import UserModel from "@/modules/user/user.model";

@Component
export default class UserFormEdit extends Vue {
  private formDataModel: any = {};

  // ライフサイクルのメソッド
  created() {
      console.log("■created() confirm")
      if (this.$route.params.formDataModel) {
        console.dir(this.$route.params.formDataModel)
        this.formDataModel = this.$route.params.formDataModel;
      }
  }

  private goSave() {
    console.log("■TODO");
  }

  private goBackEdit() {
    this.$router.go(-1);
  }
}
</script>
