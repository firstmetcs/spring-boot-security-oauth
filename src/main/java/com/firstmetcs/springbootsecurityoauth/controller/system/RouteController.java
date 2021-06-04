package com.firstmetcs.springbootsecurityoauth.controller.system;

import com.firstmetcs.springbootsecurityoauth.config.swagger.SwaggerTags;
import com.firstmetcs.springbootsecurityoauth.model.security.SysRoute;
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
@RequestMapping(value = "/api/route")
@Api(value = "路由管理", tags = SwaggerTags.ROUTE)
public class RouteController {

    @Autowired
    private RouteService routeService;

    @ResponseBody
    @GetMapping("/all")
    @ApiOperation(value = "获取所有路由", notes = "获取所有路由备注", httpMethod = "GET")
    public ResponseEntity<Object> getAllRoute() {
        return Results.success(routeService.selectByPid(0));
    }

    @ResponseBody
    @PostMapping("/add-or-update")
    @ApiOperation(value = "新增或修改路由", notes = "新增或修改路由备注", httpMethod = "POST")
    public ResponseEntity<Object> addOrUpdateRoute(@ApiParam(name = "sysRoute", value = "路由信息") @RequestBody SysRoute sysRoute) {
        return Results.success(routeService.insertOrUpdateByPrimaryKey(sysRoute));
    }

    @ResponseBody
    @GetMapping("/get-parent-by-id")
    @ApiOperation(value = "获取父路由", notes = "获取父路由备注", httpMethod = "GET")
    public ResponseEntity<Object> getParentRouteById(@ApiParam(name = "id", value = "路由id", defaultValue = "0", example = "0") @RequestParam Integer id) {
        return Results.success(routeService.getAllParentRouteById(id));
    }

    @ResponseBody
    @GetMapping("/get-route-by-role-id")
    @ApiOperation(value = "根据角色获取路由", notes = "根据角色获取路由备注", httpMethod = "GET")
    public ResponseEntity<Object> getRouteByRoleId(@ApiParam(name = "roleId", value = "角色id", defaultValue = "0", example = "0") @RequestParam Integer roleId) {
        return Results.success(routeService.selectByRoleId(roleId));
    }
}
