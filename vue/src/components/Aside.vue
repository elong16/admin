<template>
  <div>
    <el-menu
        style="width: 200px"
        active-text-color="#ffd04b"
        background-color="#545c64"
        class="el-menu-vertical-demo"
        :default-active="$route.path"
        text-color="#fff"
        router
    >
      <el-submenu :index="m.path" v-for="m in menus" :key="m.name">
        <template #title>
          <span> <i :class="m.icon"></i>{{ m.name }}</span>
        </template>
        <el-menu-item-group>
          <el-menu-item :index="c.path" v-for="c in m.children" :key="c.name">
            <i :class="c.icon"></i>{{ c.name }}
          </el-menu-item>
        </el-menu-item-group>
      </el-submenu>
    </el-menu>
  </div>
</template>

<script>
export default {
  name: "Aside",
  data() {
    return {
      menus: {},
      i: 1,
    }
  },
  created() {
    this.menus = JSON.parse(localStorage.getItem("menus"))
  },
};
</script>

<style>
a {
  text-decoration: none;
}

.el-menu-vertical-demo {
  height: 100%;
}
</style>