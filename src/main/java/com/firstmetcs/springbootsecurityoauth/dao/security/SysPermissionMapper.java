package com.firstmetcs.springbootsecurityoauth.dao.security;

import com.firstmetcs.springbootsecurityoauth.model.security.SysPermission;

import java.util.List;

public interface SysPermissionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysPermission record);

    int insertSelective(SysPermission record);

    SysPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysPermission record);

    int updateByPrimaryKey(SysPermission record);

    List<SysPermission> selectAll();

    List<SysPermission> selectByRoleId();
}