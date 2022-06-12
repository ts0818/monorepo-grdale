<template>
  <div class="video-content">
    <div class="container row">
      <div class="column">
        <video
          ref="videoPlayer"
          class="video-js vjs-default-skin vjs-big-play-centered vjs-16-9"
          muted
          playsinline
        ></video>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import videojs, { VideoJsPlayer, VideoJsPlayerOptions } from "video.js";
/* eslint-disable no-console */
import { Component, Prop, Vue, Watch } from "vue-property-decorator";

// type PlayerPropsType = {
//   id: string;
//   mediaType?: string;
//   src: string;
//   title: string;
//   createdAt: string;
// };

@Component
export default class VideoPlayerView extends Vue {
  private name = "videojsPlayer";
  @Prop({
    type: Object,
    default: () => {
      /**/
    },
  })
  private options?: VideoJsPlayerOptions;

  private videoJsPlayerInstance = {} as VideoJsPlayer;

  mounted() {
    console.log("■child component mounted()");
  }

  beforeDestroy() {
    if (this.videoJsPlayerInstance) {
      this.videoJsPlayerInstance.dispose();
    }
  }

  private createVideoJsInstance() {
    this.videoJsPlayerInstance = videojs(
      this.$refs.videoPlayer as any,
      this.options as any,
      () => {
        console.log("onPlayerReady");
      }
    );
  }

  @Watch("videoJSPlayerInstance")
  private infoVideoJSPlayerInstance() {
    console.log(this.videoJsPlayerInstance);
  }

  @Watch("options")
  private infoPropOptions() {
    console.log(this.options);
    this.createVideoJsInstance();
  }
}
</script>

<style lang="scss" scoped>
.video-content {
  max-width: 640px;
  margin: 0 auto;
}
::v-deep {
  .vjs-big-play-button {
    display: block;
    background: orange;
  }
}
</style>
