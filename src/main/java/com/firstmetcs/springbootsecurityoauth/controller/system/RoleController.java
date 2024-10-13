package com.firstmetcs.springbootsecurityoauth.controller.system;

import com.firstmetcs.springbootsecurityoauth.config.swagger.SwaggerTags;
import com.firstmetcs.springbootsecurityoauth.model.security.SysRole;
import com.firstmetcs.springbootsecurityoauth.service.security.RoleService;
import com.firstmetcs.springbootsecurityoauth.service.security.RouteService;
import com.firstmetcs.springbootsecurityoauth.utils.Results;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 * @date 2017/8/16
 */
@RestController
@RequestMapping(value = "/api/role")
@Api(value = "角色管理", tags = SwaggerTags.ROLE)
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RouteService routeService;

    @ResponseBody
    @GetMapping("/all")
    @ApiOperation(value = "获取所有角色", notes = "获取所有角色备注", httpMethod = "GET")
    public ResponseEntity<Object> getAllRole() {
        return Results.success(roleService.selectAllRole());
    }

    @ResponseBody
    @PostMapping("/modify-route-of-role")
    @ApiOperation(value = "新增路由", notes = "新增路由备注", httpMethod = "POST")
    public ResponseEntity<Object> modifyRouteOfRole(@ApiParam(name = "sysRole", value = "路由信息") @RequestBody SysRole sysRole) {
        return Results.success(routeService.modifyRouteOfRole(sysRole));
    }

    @ResponseBody
    @PostMapping("/add-or-update")
    @ApiOperation(value = "新增或修改角色", notes = "新增或修改角色备注", httpMethod = "POST")
    public ResponseEntity<Object> addOrUpdateRole(@ApiParam(name = "sysRole", value = "角色信息") @RequestBody SysRole sysRole) {
        return Results.success(roleService.insertOrUpdateByPrimaryKey(sysRole));
    }
}
