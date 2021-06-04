package com.firstmetcs.springbootsecurityoauth.service.security;

import com.firstmetcs.springbootsecurityoauth.model.security.SysApi;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface ApiService {

    int insertOrUpdateByPrimaryKey(SysApi record);

    List<SysApi> findAll();

    List<SysApi> selectByRouteId(Integer routeId);
}
