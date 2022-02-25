import Vue from 'vue'

Vue.mixin({
    methods: {
        hasAuth(perm) {
            var authority = JSON.parse(localStorage.getItem("perms"))
            return authority.indexOf(perm) > -1
        }
    }
})