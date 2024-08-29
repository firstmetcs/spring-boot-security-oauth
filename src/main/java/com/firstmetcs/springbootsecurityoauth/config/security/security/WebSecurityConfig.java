package com.firstmetcs.springbootsecurityoauth.config.security.security;

import com.firstmetcs.springbootsecurityoauth.config.security.security.auth.captcha.CaptchaAuthenticationProvider;
import com.firstmetcs.springbootsecurityoauth.config.security.security.service.UserDetailsServiceImpl;
import com.firstmetcs.springbootsecurityoauth.config.security.security.filter.CustomFilterSecurityInterceptor;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * Spring security 核心配置文件
 *
 * @author fancunshuo
 * @date 20/12/19
 * @apiNote Created by yangyibo on 17/1/19.
 */

//@Order(1)
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Autowired
    private CasAuthenticationProvider casAuthenticationProvider;
    @Autowired
    private CasAuthenticationFilter casAuthenticationFilter;

    @Autowired
    private SingleSignOutFilter singleSignOutFilter;

    @Autowired
    private LogoutFilter requestSingleLogoutFilter;

    @Autowired
    private CasAuthenticationEntryPoint entryPoint;

    @Autowired
    private CaptchaAuthenticationProvider captchaAuthenticationProvider;

    @Autowired
    private CustomFilterSecurityInterceptor customFilterSecurityInterceptor;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> captchaAuthenticationDetailsSource;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //user Details Service验证
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        // 设置 userDetailsService 和  authenticationProvider 都会创建一个 Provider。 如果仅需要一个，请只设置一个
        auth.authenticationProvider(captchaAuthenticationProvider).authenticationProvider(casAuthenticationProvider).authenticationEventPublisher(authenticationEventPublisher());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 使用BCryptPasswordEncoder加密
        return new BCryptPasswordEncoder();
//        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DefaultAuthenticationEventPublisher authenticationEventPublisher() {
        return new DefaultAuthenticationEventPublisher();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                // 允许基于使用HttpServletRequest限制访问
                .authorizeRequests()
                .antMatchers("/oauth/**", "/login-page").permitAll()
                // 除了前面定义的url,后面的都得认证后访问（登陆后访问）
                .anyRequest().authenticated().and()
                .exceptionHandling()
                //设置认证入口
                .authenticationEntryPoint(entryPoint)
                // 配置 Http Basic 验证
//                .and().httpBasic()
                // 指定支持基于表单的身份验证。如果未指定FormLoginConfigurer#loginPage(String)，则将生成默认登录页面
                // 默认登陆页面位于 org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter
                .and().formLogin()
//                .loginPage("/login-page").loginProcessingUrl("/authentication/form")
                // 注入自定义authenticationDetailsSource
//                .authenticationDetailsSource(captchaAuthenticationDetailsSource)
                // 关闭跨站请求防护 csrf (Cross-site request forgery)
                .and().csrf().disable();
        http.addFilterBefore(customFilterSecurityInterceptor, FilterSecurityInterceptor.class);
        http//设置认证入口
                //添加过滤器,执行单点登录处理逻辑
                .addFilter(casAuthenticationFilter)
                //处理认证逻辑
                .addFilterBefore(singleSignOutFilter, CasAuthenticationFilter.class)
                //处理退出登录
                .addFilterBefore(requestSingleLogoutFilter, LogoutFilter.class);
    }

    /*** 设置不拦截规则 */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**", "/druid/**", "/public/**", "/error", "/login-captcha-code");
        web.ignoring().antMatchers("/swagger-ui.html")
                .antMatchers("/webjars/**")
                .antMatchers("/v2/**")
                .antMatchers("/swagger-resources/**");
    }
}