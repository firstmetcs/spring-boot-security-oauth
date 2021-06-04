package com.firstmetcs.springbootsecurityoauth.controller.system;

import com.firstmetcs.springbootsecurityoauth.config.swagger.SwaggerTags;
import com.firstmetcs.springbootsecurityoauth.model.security.SysApi;
import com.firstmetcs.springbootsecurityoauth.service.security.ApiService;
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
@RequestMapping(value = "/api/api")
@Api(value = "接口管理", tags = SwaggerTags.API)
public class ApiController {

    @Autowired
    private ApiService apiService;

    @ResponseBody
    @GetMapping("/all")
    @ApiOperation(value = "获取所有权限", notes = "获取所有权限备注", httpMethod = "GET")
    public ResponseEntity<Object> getAllPermission() {
        return Results.success(apiService.findAll());
    }

    @ResponseBody
    @PostMapping("/add-or-update")
    @ApiOperation(value = "新增或修改权限", notes = "新增或修改权限备注", httpMethod = "POST")
    public ResponseEntity<Object> addOrUpdatePermission(@ApiParam(name = "sysPermission", value = "路由信息") @RequestBody SysApi sysApi) {
        return Results.success(apiService.insertOrUpdateByPrimaryKey(sysApi));
    }

    @ResponseBody
    @GetMapping("/get-api-by-route-id")
    @ApiOperation(value = "根据路由获取权限", notes = "根据路由获取权限备注", httpMethod = "GET")
    public ResponseEntity<Object> getPermissionByRouteId(@ApiParam(name = "routeId", value = "路由id", defaultValue = "0", example = "0") @RequestParam Integer routeId) {
        return Results.success(apiService.selectByRouteId(routeId));
    }
}
