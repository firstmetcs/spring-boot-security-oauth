package com.firstmetcs.springbootsecurityoauth.service.security.impl;

import com.firstmetcs.springbootsecurityoauth.dao.security.SysRoleMapper;
import com.firstmetcs.springbootsecurityoauth.model.security.SysRole;
import com.firstmetcs.springbootsecurityoauth.service.security.PermissionService;
import com.firstmetcs.springbootsecurityoauth.service.security.RoleService;
import com.firstmetcs.springbootsecurityoauth.service.security.RouteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
 * @date 2020/12/16
 * @apiNote Created by Administrator on 2017/8/16.
 */
@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private RouteService routeService;
    @Resource
    private PermissionService permissionService;

    @Override
    public List<SysRole> selectAllRole() {
        return sysRoleMapper.selectAllRole();
    }

    @Override
    public int insertOrUpdateByPrimaryKey(SysRole record) {
        int res = 0;
        if (record.getId() == null) {
            res = sysRoleMapper.insert(record);
        } else {
            res = sysRoleMapper.updateByPrimaryKey(record);
        }
        routeService.modifyRouteOfRole(record);
        permissionService.modifyPermissionOfRole(record);
        return res;
    }
}
