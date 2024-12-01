package com.firstmetcs.springbootsecurityoauth.dao.security;

import com.firstmetcs.springbootsecurityoauth.model.security.SysRolePermission;

import java.util.List;

public interface SysRolePermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRolePermission record);

    int insertSelective(SysRolePermission record);

    SysRolePermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRolePermission record);

    int updateByPrimaryKey(SysRolePermission record);

    int deleteByRoleId(Integer roleId);

    int batchInsert(List<SysRolePermission> list);
}