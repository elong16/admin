<template>
  <div>
    <!--  查询功能-->
    <div style="margin:10px">
      <el-input placeholder="请输入关键字" style="width: 50%" v-model="search" clearable></el-input>
      <el-button type="primary" @click="load">查询</el-button>
    </div>

    <!--  功能按钮-->
    <div style="margin:10px">
      <el-button type="priamry" @click="add" v-if="hasAuth('user:add')">新增</el-button>
      <el-popconfirm title="确定删除吗？" @confirm="deleteBatch">
        <template #reference>
          <el-button type="danger" style="margin-left: 10px" v-if="hasAuth('user:delete')">批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>

    <!--  表格界面-->
    <div>
      <el-table :data="tableData" stripe style="width: 100%;font-size:12px" border
                @selection-change="handleSelectionChange">
        <el-table-column type="selection"></el-table-column>
        <el-table-column prop="id" label="用户编码" width="140px"></el-table-column>
        <el-table-column prop="username" label="用户姓名" width="80px"></el-table-column>
        <el-table-column prop="phone" label="手机号码" width="200px" align="center"></el-table-column>
        <el-table-column prop="number" label="登录账号" width="100px"></el-table-column>
        <el-table-column prop="role" label="角色" width="100px"></el-table-column>
        <el-table-column prop="state" label="状态"></el-table-column>
        <el-table-column prop="mobile" label="是否移动登录" width="100px" align="center"></el-table-column>
        <el-table-column prop="supper" label="是否超级客服" width="100px" align="center"></el-table-column>
        <el-table-column prop="hidden" label="乘客手机号" width="100px" align="center"></el-table-column>
        <el-table-column prop="hide" label="司机手机号" width="100px" align="center"></el-table-column>
        <el-table-column label="操作" width="140px">
          <template #default="scope">
            <el-button type="text" @click="handleEdit(scope.row)" size="mini">编辑</el-button>
            <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
              <template #reference>
                <el-button type="text" size="mini">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--  页码条-->
    <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5,10,20]"
          :page-size="10"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <!--表单提交界面-->
    <div>
      <el-dialog title="用户添加"
                 :visible.sync="dialogVisible"
                 width="50%"
      >
        <el-form :model="form" label-width="120px">

          <el-form-item label="用户姓名">
            <el-input v-model="form.username" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="手机号码">
            <el-input v-model="form.phone" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="登录账号">
            <el-input v-model="form.number" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="初始密码">
            <el-input v-model="form.password" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="状态">
            <template>
              <el-select v-model="form.state" placeholder="请选择">
                <el-option
                    v-for="item in states"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
            </template>
          </el-form-item>
          <div style="display: flex">
            <el-form-item label="乘客手机号">
              <template>
                <el-select v-model="form.hidden" placeholder="请选择" style="width: 100px" default-first-option>
                  <el-option
                      v-for="item in hides"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </template>
            </el-form-item>
            <el-form-item label="司机手机号" style="flex: 1">
              <template>
                <el-select v-model="form.hide" placeholder="请选择" default-first-option>
                  <el-option
                      v-for="item in hides"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </template>
            </el-form-item>
          </div>
          <div style="display: flex">
            <el-checkbox v-model="form.mobile" label="是否为移动登录" style="padding-left: 40px"></el-checkbox>
            <el-checkbox v-model="form.supper" label="标记为超级客服" style="padding-left: 40px"></el-checkbox>
          </div>
          <el-form-item label="角色" style="flex: 1">
            <template>
              <el-select v-model="form.role" placeholder="请选择">
                <el-option
                    v-for="item in roletypes"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

        </el-form>
        <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";


export default {
  name: "User",
  created() {
    this.load()
    this.loadrole()
  },
  data() {
    return {
      dialogVisible: false,
      search: '',
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      form: {
        mobile: false,
        supper: false
      },
      states: [
        {
          label: "可用",
          value: "可用",
        },
        {
          label: "不可用",
          value: "不可用",
        }
      ],
      hides: [
        {
          label: "隐藏",
          value: "隐藏",
        },
        {
          label: "显示",
          value: "显示",
        }
      ],
      roletypes: [],
      ids: [],
    }
  },
  methods: {
    add() {
      this.dialogVisible = true
      this.form = {}
    },
    save() {
      if (this.form.id) {
        request.put("/api/user", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
        })
      } else {
        request.post("/api/user", this.form).then(res => {
              console.log(res)
              if (res.code === '0') {
                this.$message({
                  type: "success",
                  message: "新增成功"
                })
              } else {
                this.types = res.data.records
              }
            }
        )
      }
      this.load()
      this.dialogVisible = false

    },
    load() {
      request.get("/api/user", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total

      })
    },
    loadrole() {
      request.get("/api/role", {
        params: {
          pageNum: 1,
          pageSize: 10,
          search: '',
        }
      }).then(res => {
        res.data.records.forEach(rec => {
          this.roletypes.push({value: rec.name, label: rec.name})
        })

      })


    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.currentPage = pageNum
      this.load()
    },
    handleDelete(id) {
      request.delete('/api/user/' + id).then(res => {
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "删除成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
      this.load()
    },
    deleteBatch() {
      if (!this.ids.length) {
        this.$message.warning("请选择数据")
        return
      }
      request.post('/api/user/deleteBatch', this.ids).then(res => {
        if (res.code === '0') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error(res.msg)
        }
      })
      this.load()
    },
    handleSelectionChange(val) {
      this.ids = val.map(v => v.id)
    }
  }
}
</script>

<style scoped>

</style>