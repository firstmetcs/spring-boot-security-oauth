package com.firstmetcs.springbootsecurityoauth.service.security.impl;

import com.firstmetcs.springbootsecurityoauth.dao.security.SysRoleMapper;
import com.firstmetcs.springbootsecurityoauth.model.security.SysRole;
import com.firstmetcs.springbootsecurityoauth.service.security.RoleService;
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

    @Override
    public List<SysRole> selectAllRole() {
        return sysRoleMapper.selectAllRole();
    }
}
