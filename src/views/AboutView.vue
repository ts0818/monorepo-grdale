<template>
  <div class="about">
    <h1>This is an about page</h1>
    <video-player :options="videoOptions" />
    <label
      >画像を選択
      <input type="file" @change="onFileChange" />
    </label>
    <button @click="removeFile">削除</button>
    <button @click="uploadFile">アップロード</button>
    <div>
      <video v-if="isMovie" :src="selectedFile" controls />
      <img v-else :src="selectedFile" />
    </div>

    <!--<img :src="require('@/assets/images/SVGアイコン.svg')" />-->
  </div>
</template>

<script lang="ts">
/* eslint-disable no-console */
import { Component, Vue, Watch } from "vue-property-decorator";
import UserService from "../api/userService";
import UserDto from "../dto/user/UserDto";
import VideoPlayer from "@/views/video/VideoPlayer.vue";

@Component({
  components: {
    VideoPlayer,
  },
})
export default class AboutView extends Vue {
  private temp = "";
  private selectedFile = "";
  private tempFile = {};
  private userDto?: any;
  private isMovie = false;

  private mimeType = "";
  private videoOptions = {};

  created() {
    //this.test();
  }

  private onFileChange(event: any) {
    const files = event.target.files || event.dataTransfer.files;
    this.tempFile = files[0];
    this.createDisplayImage(files[0], event);
  }

  private createDisplayImage(file: any, event: any) {
    const fileReader = new FileReader();
    fileReader.onload = (event) => {
      console.log("createDisplayImage");
      console.dir(event);
      if (event && event.target) {
        this.selectedFile = event.target.result as string;
        this.isMovie = this.isMimeTypeOfMovie(file.type);
      }
    };
    fileReader.readAsDataURL(file);
  }

  private removeFile() {
    this.selectedFile = "";
  }

  private uploadFile() {
    console.log("■created()");
    //console.dir(this.selectedFile);
    //console.dir(this.tempFile);
    let data = "テスト";
    this.userDto = Object.entries({
      id: data,
      file: this.tempFile as Blob,
    });
    let formData = new FormData();
    formData.append("id", data);
    formData.append("file", this.tempFile as File);

    //formData.append("userFormDto", JSON.stringify(userDto as any));
    formData.append("userFormDto", this.userDto);
    var postdata = new URLSearchParams();
    postdata.append("userFormDto", this.userDto);
    //postdata.append("id", data);
    //postdata.append("file", JSON.stringify(this.tempFile as File));

    // https://tech.chakapoko.com/nodejs/http/axios.html
    console.log("■送信前");
    UserService.save(formData)
      //UserService.save(postdata)
      .then((result) => {
        console.log("■レスポンス受信");
        console.log("■バックエンドからの返却の値");
        console.dir(result);
        let data = result.data.split("data:");
        let url = "";
        for (let temp of data) {
          if (temp.match("https")) {
            url = temp.replace(/\r?\n/g, "");
          }
        }
        console.log(url);
        this.videoOptions = Object.assign({}, this.videoOptions, {
          sourceOrder: true,
          autoplay: true,
          controls: true,
          liveui: true,
          muted: true,
          preload: "auto",
          html5: {
            vhs: {
              allowSeeksWithinUnsafeLiveWindow: true,
              enableLowInitialPlaylist: true,
              overrideNative: false,
              smoothQualityChange: true,
              withCredentials: false,
            },
          },
          sources: [
            {
              src: url,
              type: "application/x-mpegURL",
            },
          ],
        });
        //this.$forceUpdate();
      })
      .catch((error) => {
        console.log(error);
      });
    console.log("■処理中...");
    console.dir(UserService.chunkedArr);
    //const numArr = [...Array(100000).keys()].map((i: number) => ++i);
    const numArr = Array.from(Array(1000000).keys()).map((x: number) => x + 1);
    let response = "[";
    for (let val of numArr) {
      let tmp = val === numArr.length ? "" : ",";
      response = response.concat(val.toString(), tmp);
    }
    response += "]";
    console.log("■フロントエンド");
    console.log(response);
  }

  private test() {
    console.log("■created()");
    let data = "テスト";
    let formData = new FormData();
    formData.append("id", data);
    // https://tech.chakapoko.com/nodejs/http/axios.html
    console.log("■送信前");
    UserService.save(formData)
      .then((result) => {
        console.log("■レスポンス受信");
        console.log("■バックエンドからの返却の値");
        console.dir(result);
      })
      .catch((error) => {
        console.log(error);
      });
    console.log("■処理中...");
    console.dir(UserService.chunkedArr);
    //const numArr = [...Array(100000).keys()].map((i: number) => ++i);
    const numArr = Array.from(Array(1000000).keys()).map((x: number) => x + 1);
    let response = "[";
    for (let val of numArr) {
      let tmp = val === numArr.length ? "" : ",";
      response = response.concat(val.toString(), tmp);
    }
    response += "]";
    console.log("■フロントエンド");
    console.log(response);
  }

  @Watch("temp")
  private setInfo() {
    console.dir(this.temp);
  }

  private isMimeTypeOfMovie(mimeType: string): boolean {
    if (mimeType) {
      this.mimeType = mimeType;
      switch (mimeType) {
        case "video/mp4":
        case "video/mov":
          return true;
        default:
          return false;
      }
    }
    return false;
  }

  @Watch("videoOptions")
  private infoVideoOptions() {
    console.log(this.videoOptions);
  }
}
</script>
