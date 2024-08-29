## Spring boot 整合 Spring security、OAuth2

- 支持原生登陆
- 支持完美SSO
- 支持第三方登陆
- 支持Redis MD5 token和JWT


整合第三方登陆代码位于`CustomTokenGranter.java`，需注意`getOAuth2Authentication`方法生成的token不进行Spring security标准的用户名密码验证。

集成完美SSO思路如下：
1. 使用`IntegrationAuthenticationFilter.java`拦截登陆信息获取OpenAM生成的header信息，然后使用继承自`IntegrationAuthenticator.java`SSO登陆验证器进行prepare做MD5验证，若不符合规则则直接中断token生成过程。
2. 使用`IntegrationUserDetailsServiceImpl.java`通用第三方验证器的loadUserByUsername方法调用实际用户查询和生成token的准备。

>1. OAuth核心配置类：`AuthorizationServiceConfig.java`
>2. Spring security核心配置类：`WebSecurityConfig.java`


重要URL：
1. [获取token](http://localhost:8080/oauth/token?username=admin&password=123&grant_type=password&client_id=login&client_secret=Norma1-login&scope=all)
2. [获取当前用户信息](http://localhost:8080/api/user/current-user)
3. [刷新用户权限信息](http://localhost:8080/api/authority/update-access-token)
4. [刷新系统接口权限](http://localhost:8080/api/authority/refresh-api)