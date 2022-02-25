<template>
  <div>
    <!--  查询功能-->
    <div style="margin:10px">
      <el-input placeholder="请输入关键字" style="width: 50%" v-model="search" clearable></el-input>
      <el-button type="primary" @click="load">查询</el-button>
    </div>

    <!--  功能按钮-->
    <div style="margin:10px">
      <el-button type="priamry" @click="add">新增</el-button>
      <el-popconfirm title="确定删除吗？" @confirm="deleteBatch">
        <template #reference>
          <el-button type="danger" style="margin-left: 10px">批量删除</el-button>
        </template>
      </el-popconfirm>
    </div>

    <!--  表格界面-->
    <div>
      <el-table :data="tableData" stripe style="width: 100%;font-size:12px" @selection-change="handleSelectionChange">
        border>
        <el-table-column type="selection"></el-table-column>
        <el-table-column prop="id" label="编码" width="180"></el-table-column>
        <el-table-column prop="name" label="名称" width="180"></el-table-column>
        <el-table-column prop="type" label="类型"></el-table-column>
        <el-table-column prop="origin" label="应用来源" align="center"></el-table-column>
        <el-table-column prop="opercenter" label="运营中心" width="140px" align="center"></el-table-column>
        <el-table-column label="物理验证状态" width="140px" align="center">
          <el-button type="text">关闭</el-button>
        </el-table-column>
        <el-table-column prop="text" label="描述" width="150px"></el-table-column>
        <el-table-column label="操作">
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
      <el-dialog title="角色添加"
                 :visible.sync="dialogVisible"
                 width="50%"
      >
        <el-form :model="form" label-width="120px">

          <el-form-item label="名称">
            <el-input v-model="form.name" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="类型">
            <template>
              <el-select v-model="form.type" placeholder="请选择">
                <el-option
                    v-for="item in types"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
            </template>
          </el-form-item>
          <el-form-item label="应用来源">
            <template>
              <el-select v-model="form.origin" placeholder="请选择">
                <el-option
                    v-for="item in origins"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
            </template>
          </el-form-item>
          <el-form-item label="运营中心">
            <template>
              <el-select v-model="form.opercenter" placeholder="请选择">
                <el-option
                    v-for="item in centers"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
            </template>
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="form.text" style="width: 80%"></el-input>
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
  name: "Role",
  created() {
    this.load()
    request.get("/api/type", '',).then(res => {
      res.data.records.forEach(rec => {
        this.types.push({value: rec.name_Zh, label: rec.name_Zh})
      })
    })
  },
  data() {
    return {
      search: '',
      tableData: [],
      currentPage: 1,
      total: 2,
      form: {},
      types: [],
      dialogVisible: false,
      origins: [{
        value: "帮邦行",
        label: "帮邦行"
      }],
      centers: [
        {
          value: "漳州运营中心",
          label: "漳州运营中心",
        },
        {
          value: "厦门运营中心",
          label: "厦门运营中心",
        },
        {
          value: "泉州运营中心",
          label: "泉州运营中心",
        }
      ],
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
        request.put("/api/role", this.form).then(res => {
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
        request.post("/api/role", this.form).then(res => {
              console.log(res)
              if (res.code === '0') {
                this.$message({
                  type: "success",
                  message: "新增成功"
                })
              } else {
                this.$message({
                  type: "error",
                  message: res.msg
                })
              }
            }
        )
      }
      this.load()
      this.dialogVisible = false

    },
    load() {
      request.get("/api/role", {
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
      request.delete('/api/role/' + id).then(res => {
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
      request.post('/api/role/deleteBatch', this.ids).then(res => {
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