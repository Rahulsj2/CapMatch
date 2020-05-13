import Vue from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import router from "./router";
import jQuery from "jquery";
import "bootstrap";
import "./assets/css/app.scss";
import "@fortawesome/fontawesome-free/css/all.css";
import "@fortawesome/fontawesome-free/js/all.js";
import Default from "./layouts/Default.vue";
import Blank from "./layouts/Blank.vue";
import Onboarding from "./layouts/Onboarding.vue";

window.$ = window.jQuery = jQuery;
Vue.component("default-layout", Default);
Vue.component("blank-layout", Blank);
Vue.component("onboarding-layout", Onboarding);
Vue.config.productionTip = false;

new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
