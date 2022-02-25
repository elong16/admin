<template>
  <div>
    <Header :user="user"/>
    <div class="main">
      <Aside class="aside"/>
      <div>
        <Tabs></Tabs>
        <router-view class="home" @userInfo="refreshUser"></router-view>
      </div>

    </div>
  </div>
</template>

<script>
import Header from '../components/Header'
import Aside from '../components/Aside'
import request from "@/utils/request";
import Tabs from "@/components/Tabs";


export default {
  name: "Layout",
  mounted() {

  },
  components: {
    Header,
    Aside,
    Tabs
  },
  data() {
    return {
      user: {},

    }
  },
  created() {
    this.refreshUser()
  },
  methods: {
    refreshUser() {
      let userJson = sessionStorage.getItem("user");
      if (!userJson) {
        return
      }
      let userId = JSON.parse(userJson).id
      // 从后台取出更新后的最新用户信息
      request.get("/api/user/" + userId).then(res => {
        this.user = res.data
        console.log(this.user)
      })
    }
  }

}
</script>

<style scoped>
.main {
  display: flex;
  height: 1200px;
}


.home {
  flex: 1;

}
</style>
