package com.firstmetcs.springbootsecurityoauth.controller.system;

import com.firstmetcs.springbootsecurityoauth.config.swagger.SwaggerTags;
import com.firstmetcs.springbootsecurityoauth.model.security.UserInfo;
import com.firstmetcs.springbootsecurityoauth.model.user.UserDomain;
import com.firstmetcs.springbootsecurityoauth.service.user.UserService;
import com.firstmetcs.springbootsecurityoauth.utils.Results;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

/**
 * @author Administrator
 * @date 2017/8/16
 */
@Controller
@RequestMapping(value = "/api/user")
@Api(value = "用户管理", tags = SwaggerTags.USER)
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @PostMapping("/add")
    @ApiOperation(value = "添加用户", notes = "添加用户备注", httpMethod = "POST")
    public ResponseEntity<Object> addUser(UserDomain user) {
        return Results.success(userService.addUser(user));
    }

    @ResponseBody
    @GetMapping("/all")
    @ApiOperation(value = "查看所有用户", notes = "查看所有用户备注", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pageNum", value = "页码", dataType = "int", defaultValue = "1", example = "0"),
            @ApiImplicitParam(paramType = "query", name = "pageSize", value = "页大小", dataType = "int", defaultValue = "10", example = "0")
    })
    public ResponseEntity<Object> findAllUser(
            @RequestParam(name = "pageNum", required = false, defaultValue = "1")
                    int pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "10")
                    int pageSize) {
        return Results.success(userService.findAllUser(pageNum, pageSize));
    }

    @ResponseBody
    @GetMapping("/current-user")
    @ApiOperation(value = "获取当前用户信息", notes = "获取当前用户信息备注", httpMethod = "GET")
    public ResponseEntity<Object> currentUser(@ApiIgnore Authentication authentication, @ApiIgnore Principal principal, @ApiIgnore @AuthenticationPrincipal UserInfo user) {
        UserInfo userInfo = (UserInfo) authentication.getPrincipal();
//        userInfo.setPassword(null);
//        userInfo.setAuthority(null);
//        userInfo.setPermission(null);
//        principal.setAuthorities(null);
        return Results.success(user);
    }

//    @ResponseBody
//    @GetMapping("/current-use2")
//    @ApiOperation(value = "获取当前用户信息", notes = "获取当前用户信息备注", httpMethod = "GET")
//    public Object currentUser2(Principal principal) {
//        return principal;
//    }
}
