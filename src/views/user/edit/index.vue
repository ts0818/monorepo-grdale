<template>
  <div class="app-container">
    <el-form :model="formDataModel" ref="formDataModel">
      <el-form-item label="氏">
        <el-input v-model="formDataModel.lastName"></el-input>
      </el-form-item>
      <el-form-item label="名">
        <el-input v-model="formDataModel.firstName"></el-input>
      </el-form-item>
      <el-form-item label="年齢">
        <el-input v-model="formDataModel.age"></el-input>
      </el-form-item>
      <div id="datetime">
        <div class="block">
          <el-date-picker
            v-model="formDataModel.birthday"
            type="datetime"
            placeholder="日付の選択"
          >
          </el-date-picker>
        </div>
      </div>
      <el-form-item size="large">
        <el-button type="primary" @click="goConfirm">確認</el-button>
        <el-button @click="goBackList">戻る</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
/* eslint-disable */
/* eslint-disable no-console */
import { Component, Vue, Watch } from "vue-property-decorator";
import UserModel from "@/modules/user/user.model";
import UserServiceImpl from '@/service/user/userServiceImpl';

@Component
export default class UserFormEdit extends Vue {

  private formDataModel: any = {};

  // ライフサイクルのメソッド
  created() {
    console.log("■created()")
    if (this.$route.params.id) {
      let id :any = this.$route.params.id;
      console.log("■this.$route.params.id");
      console.log(id);
      //this.$router.push
    }
    console.log("■UserModel")
    console.dir(UserModel)
    // 確認画面からVue Router経由で戻ってきた場合
    if (!this.isEmpty(UserModel)) {
      console.log("■再設定");
      this.formDataModel = Object.assign({}, this.formDataModel, {
        id: UserModel.id,
        lastName: UserModel.lastName,
        firstName: UserModel.firstName,
        age: UserModel.age,
        birthday: UserModel.birthday
      });
    }
    console.dir(this.formDataModel);
  }

  // 確認画面に遷移
  private goConfirm() {
    console.log("■goConfirm()");
    this.formDataModel;
    console.dir(this.formDataModel);
    UserServiceImpl.setUserModel(UserModel, this.formDataModel);
    this.$router.push({ name: "/user/confirm", params: { formDataModel: this.formDataModel } });
  }

  // 一覧画面に遷移
  private goBackList() {
    // ユーザー情報初期化
    if(!this.isEmpty(UserModel)) {
      UserServiceImpl.clearUserModel(UserModel);
    }
    this.$router.go(-1);
  }

  /**
   * Objectの各プロパティの値がすべて空か確認
   * @param model プロパティの値がすべて空か調べたいオブジェクト
   * @returns true: プロパティの値がすべて空、false:値が空でないプロパティの値が存在する
   */
  private isEmpty(model: any) {

    const isNullUndefEmptyStr = Object.values(model).every(value => {
      // check for multiple conditions
      if (value === null || value === undefined || value === '') {
        return true;
      }
      return false;
    });
    return isNullUndefEmptyStr;
  }

  @Watch("formDataModel.lastName")
  private watchLastName() {
    console.log("■@Watch formDataModel.lastName")
    console.dir(this.formDataModel.lastName)
  }

  @Watch("formDataModel.firstName")
  private watchFirstName() {
    console.log("■@Watch formDataModel.firstName")
    console.dir(this.formDataModel.firstName)
  }

  @Watch("formDataModel.age")
  private watchFirstAge() {
    console.log("■@Watch formDataModel.age")
    console.dir(this.formDataModel.age)
  }

  @Watch("formDataModel.birthday")
  private watchFirstBirthday() {
    console.log("■@Watch formDataModel.birthday")
    console.dir(this.formDataModel.birthday)
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  max-width: 640px;
  margin: auto;
}
</style>
