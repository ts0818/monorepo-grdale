<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1"
        ><div class="grid-content bg-purple">勇者</div></el-col
      >
      <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11"
        ><div class="grid-content bg-purple-light">戦士</div></el-col
      >
      <el-col :xs="4" :sm="6" :md="8" :lg="9" :xl="11"
        ><div class="grid-content bg-purple">魔法使い</div></el-col
      >
      <el-col :xs="8" :sm="6" :md="4" :lg="3" :xl="1"
        ><div class="grid-content bg-purple-light">賢者</div></el-col
      >
    </el-row>
    <div>
      <el-form ref="form" :model="sizeForm" label-width="120px" size="mini">
        <el-form-item label="Activity name">
          <el-input v-model="sizeForm.name"></el-input>
        </el-form-item>
        <el-form-item label="Activity zone">
          <el-select
            v-model="sizeForm.region"
            placeholder="please select your zone"
          >
            <el-option label="Zone one" value="shanghai"></el-option>
            <el-option label="Zone two" value="beijing"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Activity time">
          <el-col :span="11">
            <el-date-picker
              type="date"
              placeholder="Pick a date"
              v-model="sizeForm.date1"
              style="width: 100%"
            ></el-date-picker>
          </el-col>
          <el-col class="line" :span="2">-</el-col>
          <el-col :span="11">
            <el-time-picker
              placeholder="Pick a time"
              v-model="sizeForm.date2"
              style="width: 100%"
            ></el-time-picker>
          </el-col>
        </el-form-item>
        <div class="checkbox">
          <el-row :gutter="10">
            <el-col>
              <el-form-item label="Activity type">
                <el-checkbox-button name="type"></el-checkbox-button>
              </el-form-item>
            </el-col>
            <el-col>
              <el-form-item label="Activity type">
                <el-checkbox-button name="type"></el-checkbox-button>
              </el-form-item>
            </el-col>
          </el-row>
        </div>
        <el-form-item label="Resources">
          <el-radio-group v-model="sizeForm.resource" size="medium">
            <el-radio border label="Sponsor"></el-radio>
            <el-radio border label="Venue"></el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item size="large">
          <el-button type="primary" @click="onSubmit">Create</el-button>
          <el-button>Cancel</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from "vue-property-decorator";

@Component
export default class TestElementUi extends Vue {
  private sizeForm = {
    name: "",
    region: "",
    date1: "",
    date2: "",
    delivery: false,
    type: [],
    resource: "",
    desc: "",
  };

  created() {
    console.log("■");
  }

  private onSubmit() {
    console.log("done");
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  max-width: 960px;
  margin: auto;
}
::v-deep {
  .checkbox {
    display: inline-flex;
    .el-row {
      display: inline-flex;
    }
    .el-form-item {
      display: flex;
      margin-bottom: 1rem !important;
    }
    label.el-form-item__label {
      order: 2 !important;
      width: max-content !important;
      padding-left: 1rem;
    }

    .el-form-item__content {
      margin-left: 0 !important;
    }

    .el-checkbox-button__original {
      opacity: 1;
    }

    .el-checkbox-button {
      padding-bottom: 0.6rem;
    }
  }
}
</style>
