package com.firstmetcs.springbootsecurityoauth.dao.security;

import com.firstmetcs.springbootsecurityoauth.model.security.SysRoleRoute;

import java.util.List;

public interface SysRoleRouteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleRoute record);

    int insertSelective(SysRoleRoute record);

    SysRoleRoute selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleRoute record);

    int updateByPrimaryKey(SysRoleRoute record);

    int deleteByRoleId(Integer roleId);

    int batchInsert(List<SysRoleRoute> list);
}