import Vue from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import router from "./router";

import Default from "./layouts/Default.vue"
import Onboarding from "./layouts/Onboarding.vue"

Vue.component('default-layout', Default)
Vue.component('onboarding-layout', Onboarding)

Vue.config.productionTip = false;

new Vue({
  router,
  render: h => h(App)
}).$mount("#app");
