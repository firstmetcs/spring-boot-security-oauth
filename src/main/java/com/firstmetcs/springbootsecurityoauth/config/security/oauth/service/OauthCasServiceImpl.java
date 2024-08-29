package com.firstmetcs.springbootsecurityoauth.config.security.oauth.service;

import com.firstmetcs.springbootsecurityoauth.config.security.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("oauthCasService")
public class OauthCasServiceImpl implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
        String name = token.getName();
        System.out.println("获得的用户名：" + name);
        UserDetails userinfo = userDetailsService.loadUserByUsername(name);
        if (userinfo == null) {
            throw new UsernameNotFoundException(name + "不存在");
        }
        return userinfo;
    }
}
