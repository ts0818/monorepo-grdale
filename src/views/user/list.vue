<template>
  <div>
    <el-table :data="userList" stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="180"> </el-table-column>
      <el-table-column prop="lastName" label="氏" width="180">
      </el-table-column>
      <el-table-column prop="firstName" label="名"> </el-table-column>
      <el-table-column prop="age" label="年齢"> </el-table-column>
      <el-table-column prop="birthday" label="生年月日"> </el-table-column>
      <el-table-column>
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)"
            >Edit</el-button
          >
          <el-button
            size="mini"
            type="danger"
            @click="handleDelete(scope.$index, scope.row)"
            >Delete</el-button
          >
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script lang="ts">
/* eslint-disable no-console */
import { Component, Vue, Watch } from "vue-property-decorator";
import UserModel from "@/modules/user/user.model";
import UserServiceImpl from "@/service/user/userServiceImpl";

@Component
export default class UserList extends Vue {
  private userList = [
    {
      id: 1,
      lastName: "鈴木",
      firstName: "一郎",
      age: "38",
      birthday: "1983-07-20 15:00:00",
    },
    {
      id: 2,
      lastName: "佐藤",
      firstName: "二郎",
      age: "39",
      birthday: "1982-07-20 15:00:00",
    },
    {
      id: 3,
      lastName: "高橋",
      firstName: "三郎",
      age: "40",
      birthday: "1981-07-20 15:00:00",
    },
  ];

  created() {
    console.log("■list");
  }

  private handleEdit(index: number, row: any) {
    console.log(row);
    if (row.id) {
      UserServiceImpl.setUserModel(UserModel, row);
      this.$router.push({ name: "/user/edit", params: { id: String(row.id) } });
    }
  }

  private handleDelete(index: number, row: any) {
    console.log(row);
  }
}
</script>
