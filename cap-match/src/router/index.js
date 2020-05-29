import Vue from "vue";
import VueRouter from "vue-router";
// import Home from "../views/Home.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Dashboard",
    meta: {layout: "default"},
    component: () => import("../views/Dashboard.vue"),
  },
  {
    path: "/admin",
    name: "AdminDashboard",
    meta: {layout: "default"},
    component: () => import("../views/AdminDashboard.vue"),
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
    component: () => import("../views/Auth/Signup.vue")
  },
  {
    path: "/login",
    name: "Login",
    meta: {layout: "onboarding"},
    component: () => import("../views/Auth/Login.vue")
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
    path: "/profile",
    name: "Profile",
    component: () => import("../views/Individual.vue")
  },
  {
    path: "/studentProfiles",
    name: "StudentProfile",
    component: () => import("../views/AdminBrowseStudent.vue")
  },
  {
    path: "/facultyProfile",
    name: "FacutlyProfile",
    component: () => import("../views/AdminBrowseFaculty.vue")
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
    path: "/confirmAccount",
    name: "confirmAccount",
    meta: {layout: "onboarding"},
    component: () => import("../views/ConfirmAccount.vue")
  },
  {
    path: "/profiles",
    name: "Profiles",
    component: () => import("../views/Profiles.vue")
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