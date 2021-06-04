package com.firstmetcs.springbootsecurityoauth.model.security;

import java.util.ArrayList;
import java.util.List;

public class SysRole {
    private Integer id;

    private String code;

    private String name;

    private Integer status;

    private String remark;

    List<SysApi> apiList = new ArrayList<>();

    List<SysPermission> permissionList = new ArrayList<>();

    List<SysRoute> routeList = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public List<SysApi> getApiList() {
        return apiList;
    }

    public void setApiList(List<SysApi> apiList) {
        this.apiList = apiList;
    }

    public List<SysPermission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<SysPermission> permissionList) {
        this.permissionList = permissionList;
    }

    public List<SysRoute> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<SysRoute> routeList) {
        this.routeList = routeList;
    }
}