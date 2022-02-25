package com.example.demo.Controller;


import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.bean.*;
import com.example.demo.common.Result;
import com.example.demo.mapper.MenuMapper;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.TypeMapper;
import com.example.demo.mapper.UserMapper;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.demo.common.vo.MenuVo;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {
    @Resource
    MenuMapper menuMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    RoleMapper roleMapper;
    @Resource
    TypeMapper typeMapper;

    @GetMapping("/nav")
    public Result nav(Principal principal) {
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getNumber, principal.getName()));
        //获取权限信息
        String authorityInfo = getUserAuthorityInfo(user.getId());
        String[] authorityInfoArray = StringUtils.tokenizeToStringArray(authorityInfo, ",");
        System.out.println("权限列表" + authorityInfo);
        List<MenuVo> menuVos = getCurrentUserNav();
        return Result.success(MapUtil.builder()
                .put("authoritys", JSONUtil.toJsonStr(authorityInfoArray))
                .put("nav", JSONUtil.toJsonStr(menuVos))
                .map());
        //获取导航信息
    }

    @GetMapping("/list")
    public Result<?> list() {
        List<Menu> menus = getMenuTree();

        return Result.success(JSONUtil.toJsonStr(menus));
    }

    @GetMapping("/getmenu/{id}")
    public Result<?> getMenu(@PathVariable Integer id) {
        List<Integer> menuids = typeMenuMapper.getMenuIdsByTypeId(id);

//        List<Menu> menus=menuMapper.selectBatchIds(menuids);

        return Result.success(JSONUtil.toJsonStr(menuids));
    }

    private List<Menu> getMenuTree() {
        List<Menu> menus = menuMapper.getAllMenu();
        return buildTreeMenu(menus);
    }

    @PostMapping("/save")
    //@PreAuthorize("hasAuthority('menu:save')")
    public Result<?> save(@Validated @RequestBody Menu menu) {
        menuMapper.insert(menu);
        return Result.success();
    }


    @PutMapping
    public Result<?> update(@RequestBody Menu menu) {
        menuMapper.updateById(menu);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        Long count = menuMapper.selectCount(new QueryWrapper<Menu>().eq("parentid", id));
        menuMapper.deleteById(id);
        return Result.success();
    }


    //根据获取的权限id和角色类型设置权限
    @PostMapping("/setAuthority")
    public Result<?> setAuthority(@RequestBody List<Integer> ids) {
        //先清空数据库中对应权限
        Integer tid = ids.get(0);
        typeMenuMapper.delete(Wrappers.<TypeMenu>lambdaQuery().eq(TypeMenu::getTid, tid));
        ids.remove(0);
        Integer mid;
        for (int i = 0; i < ids.size() + 1; i++) {
            mid = ids.get(i);
            TypeMenu typeMenu = new TypeMenu();
            typeMenu.setMid(mid);
            typeMenu.setTid(tid);
            typeMenuMapper.insert(typeMenu);

        }

        return Result.success();
    }


    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.success(menuMapper.selectById(id));
    }


    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {

        LambdaQueryWrapper<Menu> wrapper = Wrappers.<Menu>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            wrapper.like(Menu::getName, search);
        }
        Page<Menu> menuPage = menuMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Result.success(menuPage);
    }


    public List<MenuVo> getCurrentUserNav() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getNumber, username));
        List<Integer> roleids = roleMapper.getRoleIdByUserId(user.getId());
        List<Integer> typeids = new ArrayList<Integer>();
        HashSet<Integer> menuIds = new HashSet<Integer>();
        for (Integer roleid : roleids) {
            Integer typeid = typeMapper.getTypeIdByRoleId(roleid);
            Type type = typeMapper.selectOne(Wrappers.<Type>lambdaQuery().eq(Type::getId, typeid));
            typeids.add(typeid);
        }
        for (Integer typeid : typeids) {
            List<Integer> nowMenuIds = menuMapper.getMenuIdByTypeId(typeid);
            menuIds.addAll(nowMenuIds);
        }
        List<Menu> menus = new ArrayList<Menu>();
        if (menuIds.size() > 0) {

            for (Integer menuid : menuIds) {
                menus.add(menuMapper.getByMenuId(menuid));
            }
        }
        //转换成树形结构
        List<Menu> menuTree = buildTreeMenu(menus);
        //实体转Vo
        System.out.println(menuTree);
        return convert(menuTree);
    }

    public List<Menu> buildTreeMenu(List<Menu> menus) {
        List<Menu> finalMenus = new ArrayList<>();
        //先各自寻找到自己的孩子
        for (Menu menu : menus) {
            for (Menu e : menus) {
                if (menu.getId() == e.getParentid()) {
                    menu.getChildren().add(e);
                }
            }
            if (menu.getParentid() == 0L) {
                finalMenus.add(menu);
            }
        }

        //
        return finalMenus;
    }

    public List<MenuVo> convert(List<Menu> menuTree) {
        List<MenuVo> menuVos = new ArrayList<>();
        menuTree.forEach(m -> {
            MenuVo menuVo = new MenuVo();
            menuVo.setId(m.getId());
            menuVo.setName(m.getComment());
            menuVo.setComponent(m.getName());
            menuVo.setPath(m.getPath());
            menuVo.setPerms(m.getPerms());
            if (m.getChildren().size() > 0) {
                menuVo.setChildren(convert(m.getChildren()));
            }
            menuVos.add(menuVo);
        });

        return menuVos;
    }


}
