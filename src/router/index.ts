import Vue from "vue";
import VueRouter, { RouteConfig } from "vue-router";
import HomeView from "../views/HomeView.vue";

Vue.use(VueRouter);

const routes: Array<RouteConfig> = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/about",
    name: "about",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },
  // {
  //   path: "/test",
  //   name: "test",
  //   component: () => import("../views/NavigationGuard.vue"),
  // },
  // {
  //   path: "/testdom",
  //   name: "testdom",
  //   component: () => import("../views/TestDom.vue"),
  // },
  {
    path: "/testui",
    name: "testui",
    component: () => import("../views/TestElementUi.vue"),
  },
  {
    path: "/user/list",
    component: () => import("../views/user/list.vue"),
  },
  {
    path: "/user/edit/:id",
    name: "/user/edit",
    component: () => import("../views/user/edit/index.vue"),
    //props: (route) => ({ id: Number(route.params.id) }),
  },
  {
    path: "/user/confirm",
    name: "/user/confirm",
    component: () => import("../views/user/confirm/index.vue"),
    //props: (route) => ({ id: Number(route.params.id) }),
  },
  {
    path: "/chat",
    name: "chat",
    component: () => import("../views/chat/TestChat.vue"),
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
