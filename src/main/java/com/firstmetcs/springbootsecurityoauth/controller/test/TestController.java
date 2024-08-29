package com.firstmetcs.springbootsecurityoauth.controller.test;

import com.firstmetcs.springbootsecurityoauth.config.swagger.SwaggerTags;
import com.firstmetcs.springbootsecurityoauth.dao.security.SysRouteMapper;
import com.firstmetcs.springbootsecurityoauth.dao.security.SysUserMapper;
import com.firstmetcs.springbootsecurityoauth.utils.Results;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author Administrator
 * @date 2017/8/16
 */
@RestController
@RequestMapping(value = "/api/test")
@Api(value = "测试", tags = SwaggerTags.TEST)
public class TestController {

    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRouteMapper sysRouteMapper;

    @ResponseBody
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN_33')")
    @ApiOperation(value = "测试所有方法", notes = "测试所有方法备注", httpMethod = "GET")
    public ResponseEntity<Object> getAll() {
        return Results.success(sysUserMapper.selectWithRoleAndPermission());
    }

    @GetMapping("/test")
    @PreAuthorize("#oauth2.hasScope('read')")
    @ApiOperation(value = "测试方法", notes = "测试方法备注", httpMethod = "GET")
    public ResponseEntity<Object> test(@ApiParam(name = "api", value = "测试id", required = true, defaultValue = "0") @RequestParam String api, HttpSession httpSession) {
        return Results.success("test");
    }

    @GetMapping("/test1")
    @ApiOperation(value = "测试方法", notes = "测试方法备注", httpMethod = "GET")
    public ResponseEntity<Object> test1(@ApiParam(name = "api", value = "测试id", defaultValue = "0") @RequestParam String api, HttpSession httpSession) throws Exception {
        throw new Exception("Wrong");
    }

    @GetMapping("/test-user")
    @ApiOperation(value = "测试方法", notes = "测试方法备注", httpMethod = "GET")
    public ResponseEntity<Object> testUser(@ApiParam(name = "userId", value = "用户id", defaultValue = "0") @RequestParam String userId, HttpSession httpSession) throws Exception {
        return Results.success(sysUserMapper.findByUserName(userId));
    }

    @GetMapping("/test-route")
    @PreAuthorize("hasRole('GUEST')")
    @ApiOperation(value = "测试方法", notes = "测试方法备注", httpMethod = "GET")
    public ResponseEntity<Object> testRoute(@ApiParam(name = "userId", value = "用户id", defaultValue = "0") @RequestParam String userId, HttpSession httpSession) throws Exception {
        return Results.success(sysRouteMapper.selectWithRoleByUserIdAndPid(userId, 0));
    }
}
