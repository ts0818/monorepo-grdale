<template>
  <div>テスト</div>
</template>

<script lang="ts">
/* eslint-disable no-console */
/* eslint-disable */
import { Component, Vue } from "vue-property-decorator";
import Route from "vue-router";

Component.registerHooks(["beforeRouteLeave"]);

@Component
export default class Navi extends Vue {
  private beforeRouterLeave(to: Route, from: Route, next: any) {
    console.log("■");
    console.dir(to);
    console.dir(from);
    console.dir(next);
    next()
  }

  created() {
    console.log("■created()");
  }
}
</script>
