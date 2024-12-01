package com.firstmetcs.springbootsecurityoauth.service.security;

import com.firstmetcs.springbootsecurityoauth.model.security.SysPermission;
import com.firstmetcs.springbootsecurityoauth.model.security.SysRole;

import java.util.List;

/**
 * Created by fancunshuo on 2024/11/24.
 */
public interface PermissionService {

    List<SysPermission> selectAll();

    int modifyPermissionOfRole(SysRole record);
}
