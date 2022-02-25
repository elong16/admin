import Vue from "vue";
import  Vuex from 'vuex'
Vue.use(Vuex)
let store=new Vuex.Store({
    state:{
        token: ''
    },
    mutations:{
        SET_TOKEN:(state,token)=>{
            state.token=token
            localStorage.setItem("token",token)
        }
    },
    actions:{},

});

export default store;