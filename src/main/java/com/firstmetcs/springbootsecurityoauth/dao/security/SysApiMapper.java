package com.firstmetcs.springbootsecurityoauth.dao.security;

import com.firstmetcs.springbootsecurityoauth.model.security.SysApi;

import java.util.List;

public interface SysApiMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysApi record);

    int insertSelective(SysApi record);

    SysApi selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysApi record);

    int updateByPrimaryKey(SysApi record);

    List<SysApi> findAll();

    List<SysApi> selectByRouteId(Integer routeId);
}