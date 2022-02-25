<template>
  <div class="header">
    <div class="logo">后台管理系统</div>
    <div class="mid"></div>

    <div class="tool">
      <el-dropdown>
          <span class="el-dropdown-link">
          <el-avatar :size="30" style="position: relative; top: 10px"></el-avatar>
           {{ userInfo }}
          <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <el-button @click="logout" style="width: 100px;height: 30px;font-size: 16px">退出系统</el-button>
        </el-dropdown-menu>
      </el-dropdown>

    </div>
  </div>

</template>

<script>
import request from "@/utils/request";

export default {
  name: 'Header',
  data() {
    return {
      userInfo: {}
    }
  },
  mounted() {
    this.getUserInfo()
  },
  methods: {
    getUserInfo() {
      this.userInfo = sessionStorage.getItem("user");
    },
    logout() {
      request.post("/api/logout").then(res => {
        if (res.code === "0") {
          localStorage.clear()
          sessionStorage.clear()
          this.$router.push("/login")
        }

      })
    }
  }
}
</script>

<style scoped>
.header {
  background-color: whitesmoke;
  height: 80px;
  line-height: 80px;
  border-bottom: 1px solid #ccc;
  display: flex;
}

.logo {
  padding-left: 40px;
  font-size: 20px;
  font-weight: bold;
  background-color: #545c64;
  color: deepskyblue;
  width: 160px;

}

.mid {
  flex: 1;
}

.user {
  width: 100px;

}

.tool {
}
</style>