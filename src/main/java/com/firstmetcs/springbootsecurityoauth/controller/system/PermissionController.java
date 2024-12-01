package com.firstmetcs.springbootsecurityoauth.controller.system;

import com.firstmetcs.springbootsecurityoauth.config.swagger.SwaggerTags;
import com.firstmetcs.springbootsecurityoauth.service.security.PermissionService;
import com.firstmetcs.springbootsecurityoauth.utils.Results;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author fancunshuo
 * @date 2024/11/24
 */
@RestController
@RequestMapping(value = "/api/permission")
@Api(value = "权限管理", tags = SwaggerTags.PERMISSION)
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @ResponseBody
    @GetMapping("/all")
    @ApiOperation(value = "获取所有权限", notes = "获取所有权限备注", httpMethod = "GET")
    public ResponseEntity<Object> getAllPermission() {
        return Results.success(permissionService.selectAll());
    }
}
