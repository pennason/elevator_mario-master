package com.shmashine.api.controller.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shmashine.api.service.permission.PermissionService;
import com.shmashine.api.service.system.TblSysMenuServiceI;
import com.shmashine.common.entity.TblSysMenu;

@RestController
@Controller
@RequestMapping("/menu")
public class SysMenuController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private TblSysMenuServiceI tblSysMenuService;

    //查询菜单
    @GetMapping("/queryMenu/{userName}")
    public ResponseEntity queryMenu(@PathVariable String userName) {
        return ResponseEntity.ok(permissionService.getPermissionV2(userName));
    }

    //添加菜单
    @PostMapping("/addMenu")
    public ResponseEntity addMenu(@RequestBody TblSysMenu menu) {
        return ResponseEntity.ok(tblSysMenuService.insert(menu));
    }

    //删除菜单
    @DeleteMapping("/delMenuByMenuId/{menuId}")
    public ResponseEntity delMenuByMenuId(@PathVariable String menuId) {
        return ResponseEntity.ok(tblSysMenuService.deleteById(menuId));
    }

    //修改菜单
    @PostMapping("/updateMenu")
    public ResponseEntity updateMenu(@RequestBody TblSysMenu menu) {
        return ResponseEntity.ok(tblSysMenuService.update(menu));
    }

}
