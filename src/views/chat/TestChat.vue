<template>
  <div id="chat">
    <div id="chat-message">
      <ul v-if="messageList && messageList.length">
        <li v-for="item in messageList" :key="item.userId">
          <div>
            <span> {{ item.content }}</span>
            <span> {{ item.chatThreadId }} </span>
            <span> {{ item.chatMessageId }} </span>
          </div>
        </li>
      </ul>
    </div>
    <div id="chat-form">
      <el-form :model="formDataModel" ref="formDataModel">
        <el-form-item label="メッセージ">
          <el-input type="textarea" v-model="formDataModel.content"></el-input>
        </el-form-item>
        <button @click.prevent="sendMessage">送信</button>
      </el-form>
    </div>
  </div>
</template>

<script lang="ts">
/* eslint-disable no-console */
import { Component, Vue, Watch } from "vue-property-decorator";
import ChatUserModel from "@/modules/chat/chat.user.model";
import chatService from "@/api/chatService";

@Component
export default class TestChatView extends Vue {
  private formDataModel: any = {};
  private messageList: Array<typeof ChatUserModel> = [];
  private chatUserModel: any = {};

  created() {
    console.log("created");
    this.formDataModel = Object.assign({}, this.formDataModel, {
      userId: ChatUserModel.userId,
      userName: ChatUserModel.userName,
      messageType: ChatUserModel.messageType,
      content: ChatUserModel.content,
      chatMessageId: ChatUserModel.chatMessageId,
      chatThreadId: ChatUserModel.chatThreadId,
    });
    //this.messageList.push(this.formDataModel);
  }

  private async sendMessage() {
    //this.messageList.push(this.formDataModel);
    //let formData = new FormData();
    console.dir(this.formDataModel);
    //formData.append("chatUserDto", this.formDataModel);
    this.chatUserModel = Object.assign({}, this.formDataModel, {
      userId: this.formDataModel.userId,
      userName: this.formDataModel.userName,
      messageType: this.formDataModel.messageType,
      content: this.formDataModel.content,
      chatMessageId: this.formDataModel.chatMessageId,
      chatThreadId: this.formDataModel.chatThreadId,
    });
    console.dir(this.chatUserModel);
    this.$set(this.formDataModel, "content", "");
    try {
      await chatService.sendMessage(this.chatUserModel).then((result) => {
        console.log("await chatService.sendMessage");
        console.dir(result.data);
        this.messageList.push({
          userId: result.data.userId,
          userName: result.data.userName,
          messageType: result.data.messageType,
          content: result.data.content,
          chatMessageId: result.data.chatMessageId,
          chatThreadId: result.data.chatThreadId,
        });
      });
    } catch (error) {
      console.log(error);
    }
  }

  @Watch("formDataModel.content")
  private watchContent() {
    console.dir(this.formDataModel);
  }
}
</script>
