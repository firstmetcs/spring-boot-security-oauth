package com.firstmetcs.springbootsecurityoauth.controller.system;

import com.firstmetcs.springbootsecurityoauth.config.security.security.service.UserDetailsServiceImpl;
import com.firstmetcs.springbootsecurityoauth.config.security.security.source.CustomInvocationSecurityMetadataSourceService;
import com.firstmetcs.springbootsecurityoauth.config.swagger.SwaggerTags;
import com.firstmetcs.springbootsecurityoauth.model.security.UserInfo;
import com.firstmetcs.springbootsecurityoauth.service.security.RouteService;
import com.firstmetcs.springbootsecurityoauth.utils.Results;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Administrator
 * @date 2017/8/16
 */
@RestController
@RequestMapping(value = "/api/authority")
@Api(value = "权限管理", tags = SwaggerTags.AUTHORITY)
public class AuthorityController {

    @Autowired
    private RouteService routeService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @GetMapping("/get-authority")
    @ApiOperation(value = "获取用户权限", notes = "获取用户权限备注", httpMethod = "GET")
    public ResponseEntity<Object> getAuthority(HttpServletRequest request, @ApiIgnore Authentication authentication) {
        UserInfo principal = (UserInfo) authentication.getPrincipal();
        return Results.success(Stream.of(principal.getAuthority(), principal.getPermission())
                .flatMap(List::stream)
                .collect(Collectors.toList()));
    }

    @GetMapping("/get-authority-by-code")
    @ApiOperation(value = "获取权限", notes = "根据代码获取权限", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "code", value = "权限编码", required = true, defaultValue = "TEST_PERMISSION_1", dataType = "String", dataTypeClass = String.class)
    })
    public ResponseEntity<Object> getAuthorityByCode(@RequestParam String code, HttpServletRequest request, @ApiIgnore Authentication authentication) {

        return Results.success(((UserInfo) authentication.getPrincipal()).getPermission().contains(code));
    }

    @GetMapping("/get-menu-with-authority")
    @ApiOperation(value = "获取带权限的菜单", notes = "获取带权限的菜单备注", httpMethod = "GET")
    public ResponseEntity<Object> getMenuWithAuthority(HttpServletRequest request, @ApiIgnore Principal principal) {

        return Results.success(routeService.selectWithRoleByUserIdAndPid(principal.getName(), 0));
    }

    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private RedisConnectionFactory connectionFactory;
    private final AuthenticationKeyGenerator authenticationKeyGenerator = new
            DefaultAuthenticationKeyGenerator();

    private final JdkSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

    @GetMapping("/update-access-token")
    @ApiOperation("刷新Token权限")
    public ResponseEntity<Object> updateAccessToken(@ApiIgnore Authentication authentication) {


        // 得到当前的认证信息
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //生成当前的所有授权
        List<GrantedAuthority> updateAuthorities = new ArrayList<>(auth.getAuthorities());
        UserInfo userInfo = (UserInfo) userDetailsService.loadUserByUsername(auth.getName());
        // 添加授权
        updateAuthorities.addAll(userInfo.getAuthorities());
        ArrayList<GrantedAuthority> collect = updateAuthorities.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(GrantedAuthority::getAuthority))), ArrayList::new));
        // 生成新的认证信息
        Authentication newAuth = new UsernamePasswordAuthenticationToken(userInfo, auth.getCredentials(), userInfo.getAuthorities());
        // 重置认证信息
//        SecurityContextHolder.getContext().setAuthentication(newAuth);


        //更新redis中的token相关信息
        OAuth2Authentication originalOAuth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(originalOAuth2Authentication.getOAuth2Request(), newAuth);
//        oAuth2Authentication.setDetails(userInfo);
        String key = authenticationKeyGenerator.extractKey(oAuth2Authentication);
        byte[] serializedKey = serializationStrategy.serialize("auth_to_access:" + key);
        byte[] bytes;
        try (RedisConnection conn = connectionFactory.getConnection()) {
            bytes = conn.get(serializedKey);
        }
        OAuth2AccessToken accessToken = serializationStrategy.deserialize(bytes,
                OAuth2AccessToken.class);
        tokenStore.storeAccessToken(accessToken, oAuth2Authentication);
        return Results.success();
    }


    @Autowired
    private CustomInvocationSecurityMetadataSourceService securityMetadataSource;

    @GetMapping("/refresh-api")
    @ApiOperation(value = "刷新系统API", notes = "刷新系统API备注", httpMethod = "GET")
    public ResponseEntity<Object> refreshApi(HttpServletRequest request, @ApiIgnore Principal principal) {

        securityMetadataSource.refreshResourceMap();
        return Results.success();
    }
}
