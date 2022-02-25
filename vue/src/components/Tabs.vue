<template>
  <el-tabs v-model="editableTabsValue" type="card" closable>
    <el-tab-pane
        v-for="(item) in editableTabs"
        :key="item.name"
        :label="item.title"
        :name="item.name"
    >
    </el-tab-pane>
  </el-tabs>
</template>

<script>
export default {
  name: "Tabs",
  data() {
    return {
      menus: JSON.parse(localStorage.getItem("menus")),
      editableTabsValue: this.menus.editableTabsValue,
      editableTabs: this.menus.editableTabs,
    }
  },
  methods: {},


}
</script>

<style scoped>

</style>