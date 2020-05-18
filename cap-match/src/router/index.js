import Vue from "vue";
import VueRouter from "vue-router";
// import Home from "../views/Home.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    meta: {layout: "onboarding"},
    component: () => import("../views/Login.vue")
  },
  // {
  //   path: "/about",
  //   name: "About",
  //   component: () => import(/* webpackChunkName: "about" */ "../views/About.vue")
  // },
  {
    path: "/user",
    name: "User",
    meta: {layout: "onboarding"},
    component: () => import("../views/User.vue")
  },
  {
    path: "/signup",
    name: "Signup",
    meta: {layout: "onboarding"},
    component: () => import("../views/Signup.vue")
  },
  {
    path: "/login",
    name: "Login",
    meta: {layout: "onboarding"},
    component: () => import("../views/Login.vue")
  },
  {
    path: "/major",
    name: "Major",
    meta: {layout: "onboarding"},
    component: () => import("../views/Major.vue")
  },
  {
    path: "/department",
    name: "Department",
    meta: {layout: "onboarding"},
    component: () => import("../views/Department.vue")
  },
  {
    path: "/interests",
    name: "Interest",
    meta: {layout: "onboarding"},
    component: () => import("../views/Interest.vue")
  },
  {
    path: "/sdgs",
    name: "Sdgs",
    meta: {layout: "onboarding"},
    component: () => import("../views/Sdgs.vue")
  },
  {
    path: "/verification",
    name: "Verification",
    meta: {layout: "onboarding"},
    component: () => import("../views/Verification.vue")
  },
  {
    path: "/verification_resend",
    name: "Resnd",
    meta: {layout: "onboarding"},
    component: () => import("../views/Resend.vue")
  },
  {
    path: "/password_reset",
    name: "PasswordReset",
    meta: {layout: "onboarding"},
    component: () => import("../views/PsswdReset.vue")
  },
  {
    path: "/password_reset/1234",
    name: "ChangePasswd",
    meta: {layout: "onboarding"},
    component: () => import("../views/ResetChangePasswd.vue")
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: () => import("../views/Dashboard.vue")
  },
  {
    path: "/profile",
    name: "Profile",
    component: () => import("../views/Profile.vue")
  },
  {
    path: "/choices",
    name: "Choices",
    component: () => import("../views/Choices.vue")
  },
  {
    path: "/match",
    name: "Match",
    component: () => import("../views/Match.vue")
  },
  {
    path: "*",
    name: "notfound",
    meta: {layout: "blank"},
    component: () => import("../views/404.vue")
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
});

export default router;
