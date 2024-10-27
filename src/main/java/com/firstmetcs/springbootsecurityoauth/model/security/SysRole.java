package com.firstmetcs.springbootsecurityoauth.model.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SysRole {
    private Integer id;

    private String code;

    private String name;

    private Integer status;

    private String remark;

    private String insUserId;

    private Date insDttm;

    private String updUserId;

    private Date updDttm;

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

    public String getInsUserId() {
        return insUserId;
    }

    public void setInsUserId(String insUserId) {
        this.insUserId = insUserId;
    }

    public Date getInsDttm() {
        return insDttm;
    }

    public void setInsDttm(Date insDttm) {
        this.insDttm = insDttm;
    }

    public String getUpdUserId() {
        return updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    public Date getUpdDttm() {
        return updDttm;
    }

    public void setUpdDttm(Date updDttm) {
        this.updDttm = updDttm;
    }
}