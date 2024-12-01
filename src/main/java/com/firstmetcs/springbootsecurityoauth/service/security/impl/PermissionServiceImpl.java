package com.firstmetcs.springbootsecurityoauth.service.security.impl;

import com.firstmetcs.springbootsecurityoauth.dao.security.SysPermissionMapper;
import com.firstmetcs.springbootsecurityoauth.dao.security.SysRolePermissionMapper;
import com.firstmetcs.springbootsecurityoauth.model.security.*;
import com.firstmetcs.springbootsecurityoauth.service.security.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fancunshuo
 * @date 2024/11/24
 */
@Service(value = "permissionService")
public class PermissionServiceImpl implements PermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;
    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;


    @Override
    public List<SysPermission> selectAll() {
        return sysPermissionMapper.selectAll();
    }

    @Override
    public int modifyPermissionOfRole(SysRole record) {
        List<SysRolePermission> rolePermissionList = new ArrayList<>();
        List<SysPermission> permissionList = new ArrayList<>(record.getPermissionList());

        permissionList.forEach(item -> {
            rolePermissionList.add(new SysRolePermission() {{
                setRoleId(record.getId());
                setPermissionId(item.getId());
            }});
        });

        sysRolePermissionMapper.deleteByRoleId(record.getId());

        if (!rolePermissionList.isEmpty()) {
            sysRolePermissionMapper.batchInsert(rolePermissionList);
        }

        return 0;
    }
}
