<template>
  <div>

    <!--  功能按钮-->
    <div style="margin:10px">
      <el-button type="priamry" @click="add">新增</el-button>
      <el-popconfirm title="确定删除吗？" @confirm="deleteBatch">
        <template #reference>
          <el-button type="danger" style="margin-left: 10px">批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>
    <!--    表格界面-->
    <div>
      <el-table
          :data="tableData"
          style="width: 100%;margin-bottom: 20px;"
          row-key="id"
          border
          stripe
          default-expand-all
          :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
        <el-table-column
            prop="comment"
            label="名称"
            sortable
            width="180">
        </el-table-column>
        <el-table-column
            prop="perms"
            label="权限编码"
            sortable
            align="center"
            width="180">
        </el-table-column>
        <el-table-column
            prop="icon"
            label="图标"
            align="center"
            width="170px">
        </el-table-column>
        <el-table-column
            prop="type"
            label="类型">
          <template slot-scope="scope">
            <el-tag size="samll" v-if="scope.row.type===0">目录</el-tag>
            <el-tag size="samll" v-else-if="scope.row.type===1">菜单</el-tag>
            <el-tag size="samll" v-else-if="scope.row.type===2">按钮</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="path"
            label="菜单URL">
        </el-table-column>
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
    <!--    表单提交-->
    <div>
      <!--表单提交界面-->
      <div>
        <el-dialog title="菜单信息添加"
                   :visible.sync="dialogVisible"
                   width="50%"
        >

          <el-form :model="form" label-width="120px">
            <el-form-item label="名称">
              <el-input v-model="form.name" style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item label="上级菜单">
              <el-select v-model="form.parentid" placeholder="请选择上级菜单">
                <template v-for="item in tableData">
                  <el-option :label="item.comment" :value="item.id" :key="item.id"></el-option>
                  <template v-for="child in item.children">
                    <el-option :label="child.comment" :value="child.id" :key="child.id">
                      <span>{{ "-" + child.comment }}</span>
                    </el-option>
                  </template>
                </template>
              </el-select>
            </el-form-item>
            <el-form-item label="权限编码" prop="perms">
              <el-input v-model="form.perms" style="width: 80%"></el-input>
            </el-form-item>

            <el-form-item label="图标" prop="icon">
              <el-input v-model="form.icon" style="width: 80%"></el-input>
            </el-form-item>
            <el-form-item label="菜单URL" prop="path">
              <el-input v-model="form.path" style="width: 80%"></el-input>
            </el-form-item>

            <el-form-item label="菜单组件" prop="component">
              <el-input v-model="form.component" style="width: 80%"></el-input>
            </el-form-item>

            <el-form-item label="类型" prop="type">
              <el-radio-group v-model="form.type">
                <el-radio :label=0>目录</el-radio>
                <el-radio :label=1>菜单</el-radio>
                <el-radio :label=2>按钮</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
        </span>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
import request from "@/utils/request";

export default {
  name: "Menu",
  data() {
    return {
      form: {},
      dialogVisible: false,
      tableData: []
    }
  },
  methods: {
    save() {
      if (this.form.id) {
        request.put("/api/menu", this.form).then(res => {
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
        request.post("/api/menu/save", this.form).then(res => {
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
      this.dialogVisible = false
      this.load()
    },
    load() {
      request.get("/api/menu/list").then(res => {
        this.tableData = JSON.parse(res.data)
      })
    },
    add() {

      this.dialogVisible = true
      this.form = {}
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
      request.delete('/api/menu/' + id).then(res => {
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
  },
  created() {
    this.load()
  }
}

</script>

<style scoped>

</style>