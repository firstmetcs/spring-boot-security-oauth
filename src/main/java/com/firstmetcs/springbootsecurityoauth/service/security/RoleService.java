package com.firstmetcs.springbootsecurityoauth.service.security;

import com.firstmetcs.springbootsecurityoauth.model.security.SysRole;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface RoleService {

    List<SysRole> selectAllRole();
}
