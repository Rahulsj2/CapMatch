import Vue from "vue";
import App from "./App.vue";
import Vuelidate from "vuelidate";
import "./registerServiceWorker";
import router from "./router";
import jQuery from "jquery";
import "bootstrap";
import "./assets/css/app.scss";
import "@fortawesome/fontawesome-free/css/all.css";
import "@fortawesome/fontawesome-free/js/all.js";
import axios from 'axios';
// element
import {Loading, Message} from "element-ui";
import "./assets/css/index.css";
// layouts
import Default from "./layouts/Default.vue";
import Blank from "./layouts/Blank.vue";
import Onboarding from "./layouts/Onboarding.vue";
import store from "./stores/store";

window.$ = window.jQuery = jQuery;

Vue.component("default-layout", Default);
Vue.component("blank-layout", Blank);
Vue.component("onboarding-layout", Onboarding);


Vue.use(Vuelidate);
Vue.use(Loading)

Vue.prototype.$message = Message
// Vue.prototype.$loading = Loading.service

const httpClient = axios.create({
  baseURL: process.env.VUE_APP_API
});  

// Request Interceptor
httpClient.interceptors.request.use(
  config => {
    /*
    const token = Vue.auth.getToken();
    if (token) config.headers.common.Authorization = `Bearer ${token}`;
    return config;
*/
    // const token = store.getters.getToken;
    // if (token){
    //   config.headers.common['x_auth_token'] = token;
    // }
    return config;
  },
  error=> {
     Promise.reject(error.request);
  }
);

// Response Interceptor
httpClient.interceptors.response.use(
  response => {
    const stat = Number(response.status)
    if ( stat === 200 || stat === 201){
      if(stat === 201){
        Vue.prototype.$message({
          showClose: true,
          message: "Your account has been suucesfully created. We're almost there",
          type: 'success',
        });
      }
      return Promise.resolve(response);
    }
    else if (stat === 226){
      Vue.prototype.$message({
      showClose: true,
      message: "Email already taken",
      type: 'error',
    });
      
      return Promise.reject(response)
    }
     else {
      return Promise.reject(response);
    }
  },
  error=>{
    if (error.response.status) {
      switch (error.response.status){
        case 400:
          Vue.prototype.$message({
            showClose: true,
            message: "Something seems to be missing..",
            type: 'error',
          });
          break;
        case 401:
          Vue.prototype.$message({
            showClose: true,
            message: "Your cannot cannot access the requested page.",
            type: 'error',
          });
          break;
        case 404:
          Vue.prototype.$router.push("/404")
          break;
        case 406:
          Vue.prototype.$message({
            showClose: true,
            message: "Your email or password is incorrect",
            type: 'error',
          });
          break;
        case 423:
          Vue.prototype.$message({
            showClose: true,
            message: "Your account has been locked contact your administrator to reolve this issue.",
            type: 'error',
          });
          break;
        case 500:
          Vue.prototype.$message({
            showClose: true,
            message: "We are experiencing some server issues, try again later.",
            type: 'error',
          });
          break;
      }
    }
    return Promise.reject(error.response)
  }
);

Vue.prototype.$http = httpClient;

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
