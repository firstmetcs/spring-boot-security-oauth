package com.firstmetcs.springbootsecurityoauth.config.security.security.source;

import com.firstmetcs.springbootsecurityoauth.dao.security.SysApiMapper;
import com.firstmetcs.springbootsecurityoauth.model.security.SysApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 加载权限资源
 *
 * @author fancunshuo
 * @date 20/12/19
 * @apiNote Created by yangyibo on 17/1/19.
 */
@Service
public class CustomInvocationSecurityMetadataSourceService implements
        FilterInvocationSecurityMetadataSource, InitializingBean {

    @Resource
    private SysApiMapper sysApiMapper;

    private final static List<ConfigAttribute> NULL_CONFIG_ATTRIBUTE = Collections.emptyList();
    /*** 权限集合 */
    private Map<RequestMatcher, Collection<ConfigAttribute>> sourceRequestMatcherCollectionMap;

    /**
     * 加载权限表中所有权限
     *
     * @return Map<String, String> url, code
     */
    public Map<RequestMatcher, String> loadResourceDefine() {
        Map<RequestMatcher, String> map = new LinkedHashMap<RequestMatcher, String>();
        List<SysApi> apis = sysApiMapper.findAll();
        for (SysApi api :
                apis) {
            if (StringUtils.isEmpty(api.getMethod())) {
                map.put(new AntPathRequestMatcher(api.getUrl()), api.getCode());
            } else {
                map.put(new AntPathRequestMatcher(api.getUrl(), api.getMethod().toUpperCase()), api.getCode());
            }
        }
        return map;
    }

    protected Map<RequestMatcher, Collection<ConfigAttribute>> bindRequestMap() {
        Map<RequestMatcher, Collection<ConfigAttribute>> map =
                new LinkedHashMap<RequestMatcher, Collection<ConfigAttribute>>();

        Map<RequestMatcher, String> resMap = this.loadResourceDefine();
        for (Map.Entry<RequestMatcher, String> entry : resMap.entrySet()) {
            RequestMatcher key = entry.getKey();
            Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
            //此处只添加了用户的名字，其实还可以添加更多权限的信息，例如请求方法到ConfigAttribute的集合中去。此处添加的信息将会作为MyAccessDecisionManager类的decide的第三个参数。
//            ConfigAttribute configAttribute = new SecurityConfig(permission.getCode());
            configAttributes = SecurityConfig.createListFromCommaDelimitedString(entry.getValue());
            //用权限的getUrl() 作为map的key，用ConfigAttribute的集合作为 value，
            map.put(key, configAttributes);
        }

        return map;
    }

    /**
     * 此方法是为了判定用户请求的url 是否在权限表中，
     * 如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
     *
     * @param object FilterInvocation
     * @return Collection<ConfigAttribute>
     * @throws IllegalArgumentException IllegalArgumentException
     * @see org.springframework.security.access.SecurityMetadataSource#getAttributes(Object)
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        Collection<ConfigAttribute> configAttributes = new ArrayList<>();
        //object 中包含用户请求的request 信息
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : sourceRequestMatcherCollectionMap.entrySet()) {
            if (entry.getKey().matches(request)) {
                configAttributes.addAll(entry.getValue());
            }
        }
        return configAttributes;
    }

    /**
     * 返回所有定义的权限资源，
     * Spring Security会在启动时校验每个ConfigAttribute是否配置正确，
     * 不需要校验直接返回null
     *
     * @return Collection<ConfigAttribute>
     * @see org.springframework.security.access.SecurityMetadataSource#getAllConfigAttributes()
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();

        for (Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry : sourceRequestMatcherCollectionMap.entrySet()) {
            allAttributes.addAll(entry.getValue());
        }

        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.sourceRequestMatcherCollectionMap = this.bindRequestMap();
    }

    public void refreshResourceMap() {
        this.sourceRequestMatcherCollectionMap = this.bindRequestMap();
    }
}