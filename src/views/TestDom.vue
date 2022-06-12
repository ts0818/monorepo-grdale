<template>
  <div>
    <div class="dom">
      <span v-if="value">{{ value }}</span>
    </div>
    <div class="dom"></div>
  </div>
</template>

<script lang="ts">
/* eslint-disable no-console */
import { Component, Vue, Watch } from "vue-property-decorator";
//import React from 'react';

@Component
export default class TestDom extends Vue {
  private value = "test";

  created() {
    console.log("■created()");
    //
  }

  mounted() {
    console.log("■mounted()");
    this.test();
    this.createHtmlDivElement();
  }

  private createHtmlDivElement() {
    console.log("■private createHtmlDivElement() ");
    //const element = <HTMLDivElement>document.createElement("test");
    const addElement01 = document.createElement("test01") as HTMLDivElement;
    const addElement02 = document.createElement("test02") as HTMLDivElement;
    const elements = document.getElementsByClassName(
      "dom"
    ) as HTMLCollectionOf<HTMLDivElement>;
    addElement01.textContent = "divの中test01";
    addElement02.textContent = "divの中test02";
    document.body.appendChild(addElement01);
    Array.from(elements).forEach((element) => {
      console.table(elements);
      console.dir(element);
      element.appendChild(addElement02);
    });
  }

  private test() {
    console.log("■test");
  }

  @Watch("test")
  private setInfo() {
    console.dir(this.value);
  }
}
</script>
