package com.firstmetcs.springbootsecurityoauth.dao.security;

import com.firstmetcs.springbootsecurityoauth.model.security.SysRoute;
import java.util.List;

public interface SysRouteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoute record);

    int insertSelective(SysRoute record);

    SysRoute selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoute record);

    int updateByPrimaryKey(SysRoute record);

    List<SysRoute> selectWithRoleByUserIdAndPid(String userId, Integer pid);

    List<SysRoute> selectByPid(Integer pid);

    List<SysRoute> selectByRoleId(Integer roleId);
}