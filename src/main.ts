import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import Element from "element-ui";
// ElementUIでの言語設定、datePickerとかで適用される
import locale from "element-ui/lib/locale/lang/ja";
import "element-ui/lib/theme-chalk/index.css";

import "video.js/dist/video-js.css";

Vue.config.productionTip = false;
Vue.use(Element, { locale });

new Vue({
  router,
  render: (h) => h(App),
}).$mount("#app");
