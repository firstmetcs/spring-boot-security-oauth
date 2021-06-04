package com.firstmetcs.springbootsecurityoauth.config.security.security.service;

import com.firstmetcs.springbootsecurityoauth.dao.security.SysUserMapper;
import com.firstmetcs.springbootsecurityoauth.model.security.*;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义UserDetailsService 接口
 * 获取用户等相关信息
 *
 * @author fancunshuo
 * @date 20/12/19
 * @apiNote Created by yangyibo on 17/1/19.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        SysUser user = sysUserMapper.findByUserName(username);
        if (user != null) {
            // 添加URL权限
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            for (SysRole role :
                    user.getRoleList()) {

                GrantedAuthority roleGrantedAuthority = new SimpleGrantedAuthority(role.getCode());
                //1：此处将角色信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
                grantedAuthorities.add(roleGrantedAuthority);

                for (SysApi api : role.getApiList()) {
                    if (api != null && api.getCode() != null) {

                        GrantedAuthority apiGrantedAuthority = new SimpleGrantedAuthority(api.getCode());
                        //1：此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
                        grantedAuthorities.add(apiGrantedAuthority);
                    }
                }
            }
            // 添加功能权限
            List<String> permissions = new ArrayList<>();
            for (SysRole role :
                    user.getRoleList()) {

                for (SysPermission permission : role.getPermissionList()) {
                    if (permission != null && permission.getCode() != null) {

                        permissions.add(permission.getCode());
                    }
                }
            }
            UserInfo userInfo = new UserInfo(user.getUserId(), user.getPassword(), grantedAuthorities);
            BeanUtils.copyProperties(user, userInfo);
            userInfo.setAuthority(user.getRoleList().stream().map(SysRole::getCode).collect(Collectors.toList()));
            userInfo.setPermission(permissions);
            return userInfo;
        } else {
            throw new UsernameNotFoundException("admin: " + username + " do not exist!");
        }
    }

}