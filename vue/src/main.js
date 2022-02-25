import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
//import store from "./store/index";
import global from './globalFun'
import qs from 'qs'

Vue.prototype.$qs = qs
import Axios from 'axios'
import VueAxios from 'vue-axios'

Vue.use(VueAxios, Axios)
//引入VueRouter
import VueRouter from 'vue-router'
//引入路由器
import router from './router'

Vue.use(VueRouter)
Vue.config.productionTip = false
Vue.use(ElementUI, {size: 'small'})

console.log(global)

new Vue({
    render: h => h(App),
    router,
    //store,
}).$mount('#app')


