package com.firstmetcs.springbootsecurityoauth.service.security;

import com.firstmetcs.springbootsecurityoauth.model.security.SysRole;
import com.firstmetcs.springbootsecurityoauth.model.security.SysRoute;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface RouteService {

    List<SysRoute> selectWithRoleByUserIdAndPid(String userId, Integer pid);

    List<SysRoute> selectByPid(Integer pid);

    int insert(SysRoute record);

    int insertSelective(SysRoute record);

    List<SysRoute> getAllParentRouteById(Integer id);

    List<SysRoute> selectByRoleId(Integer roleId);

    int modifyRouteOfRole(SysRole record);

    int updateByPrimaryKey(SysRoute record);

    int insertOrUpdateByPrimaryKey(SysRoute record);
}
