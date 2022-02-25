<template>
  <div>
    <el-container>
      <!--    角色列表-->
      <el-aside style="padding: 15px" width="300px">
        <div class="role">
          <h1>角色类型</h1>
          <table class="roletable">
            <tr v-for="data in tableData" style="align-content: center" :key="data.id" :id="data.id">
              <div @click="selecttype(data.id)" class="b">
                <td style="width: 40px;border-bottom: 1px solid #ccc;border-right: 1px solid #ccc">{{ data.id }}</td>
                <td style="width: 200px;border-bottom: 1px solid #ccc ">{{ data.name_Zh }}</td>
              </div>
            </tr>
          </table>
        </div>
      </el-aside>


      <el-main>
        <div class="tool">
          <el-button type="primary" @click="submittypemenu">保存</el-button>
        </div>
        <div class="menu">
          <h1>菜单按钮权限</h1>
          <el-tree
              :data="data"
              show-checkbox
              check-strictly=true
              node-key="id"
              :default-expanded-keys="[2, 3]"
              :props="defaultProps"
              ref="tree"
          >
          </el-tree>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import request from "@/utils/request";


export default {
  name: "Authority",
  data() {
    return {
      tableData: [],
      data: [],
      defaultProps: {
        children: 'children',
        label: 'comment'
      },
      nowtypeid: 0,
      menuids: [],
      ids: []
    };
  },
  methods: {
    loadtype() {
      request.get("/api/type", '',).then(res => {
        this.tableData = res.data.records
      })
      request.get("/api/menu/list").then(res => {
        this.data = JSON.parse(res.data)
      })
    },

    selecttype(id) {
      var i;
      for (i = 1; i < 9; i++) {
        document.getElementById(i).style.backgroundColor = "white"
      }

      var idstyle = document.getElementById(id)
      idstyle.style.backgroundColor = "skyblue"
      request.get("/api/menu/getmenu/" + id).then(res => {
        this.menuids = JSON.parse(res.data)
        console.log("sad", this.menuids)
      })
      this.$refs.tree.setCheckedKeys([])
      this.$refs.tree.setCheckedKeys(this.menuids)
      console.log("yoxi", this.$refs.tree.getCheckedKeys())
      this.nowtypeid = id

    },

    submittypemenu() {

      var val = this.$refs.tree.getCheckedNodes()
      this.ids = val.map(v => v.id)
      this.ids.unshift(this.nowtypeid)
      request.post("/api/menu/setAuthority", this.ids).then(res => {
        console.log(res)
        this.$message({
          type: "success",
          message: "保存成功"
        })
      })
      console.log(this.nowtypeid)
    }

  },
  created() {
    this.loadtype()
    this.selecttype(1)
  }
}
</script>

<style scoped>
.role {
  border: solid 1px #ccc;
  border-radius: 4px;
  padding: 5px;
}

.roletable {
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 25px;
}

.menu {
  width: 800px;
  border: 1px solid #ccc;
  border-radius: 7px;
  padding: 20px;
}

.tool {
  margin-bottom: 20px;
  width: 840px;
  height: 80px;
  border: 1px solid #ccc;
  border-radius: 7px;
}

</style>