<template>
  <div class="main">
    <div class="login">

      <div class="window">
        <div class="title">欢迎登陆</div>
        <el-form ref="form" :model="form">
          <el-form-item prop="username">
            <el-input class="account" prefix-icon="el-icon-user-solid" v-model="form.username"
                      size="large"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input class="password" prefix-icon="el-icon-lock" v-model="form.password" show-password
                      size="large"></el-input>
          </el-form-item>
          <div style="display:flex">
            <el-input type="text" v-model="inputCode" class="inputyzm" placeholder="请输入验证码" clearable
                      size="large"></el-input>
            <span @click="createCode"><SIdentify :identifyCode="code" class="yzm"></SIdentify></span>
          </div>

          <el-form-item>
            <el-button class="button" type="primary" @click="login(form)">登录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>

</template>

<script>
import request from "@/utils/request";
import SIdentify from '@/code/SIdentify'
//import {activeRouter} from "@/utils/permission"


export default {
  name: "Login",
  data() {
    return {
      form: {
        username: '',
        password: '',

        token: ''
      },
      code: '',
      captchaImg: '',
      inputCode: '',
    }

  },
  components: {
    SIdentify
  },
  methods: {
    createCode() {
      var text = "";
      var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      // 设置验证码长度，
      for (var i = 0; i < 4; i++) {
        text += possible.charAt(Math.floor(Math.random() * possible.length));
      }
      this.code = text
    },
    login() {
      if (this.inputCode == '') {
        this.$message.error('请输入验证码')
        return
      }
      if (this.inputCode.toLowerCase() != this.code.toLowerCase()) {
        this.$message.error('验证码错误')
        this.inputCode = ''
        this.createCode()
        return
      }

      request.post("/api/login?" + this.$qs.stringify(this.form)).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "登陆成功"
          })

          sessionStorage.setItem("user", JSON.stringify(res.data.user))// 缓存用户信息
          const jwt = res.data.jwt
          console.log("jwt", jwt)
          localStorage.setItem("token", jwt)
          //const menus=res.data.menus;
          //activeRouter(menus)
          this.$router.push("/main")
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    getToken() {
      request.get('/api/token').then(res => {
        this.form.token = res.data.token
      })
    }
  },

  mounted() {
    this.createCode()
  },


}
</script>

<style scoped>
.main {
  width: 100%;
  height: 100vh;
  background-color: darkturquoise;
  overflow: hidden;
}

.login {
  width: 400px;
  margin: 150px auto;
}

.title {
  color: black;
  font-size: 30px;
  padding-top: 20px;
  margin-bottom: 30px;
  text-align: center;
}

.button {
  margin-top: 30px;
  margin-left: 50px;
  width: 400px;
}

.window {
  background-color: whitesmoke;
  width: 500px;
  height: 500px;
}

.account {
  margin-top: 50px;
  margin-left: 50px;
  width: 400px;
}

.password {
  margin-top: 10px;
  margin-left: 50px;
  width: 400px;
}

.inputyzm {
  width: 200px;
  margin-left: 50px;
}

.yzm {
  margin-left: 20px;
}
</style>