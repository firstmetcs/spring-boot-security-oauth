package com.firstmetcs.springbootsecurityoauth.model.security;

import java.util.ArrayList;
import java.util.List;

public class SysRoute {
    private Integer id;

    private String code;

    private String name;

    private String path;

    private String icon;

    private Integer hideInMenu;

    private Integer pid;

    List<SysRoute> children = new ArrayList<>();

    List<String> authority = new ArrayList<>();

    List<SysApi> apiList = new ArrayList<>();

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon == null ? null : icon.trim();
    }

    public Integer getHideInMenu() {
        return hideInMenu;
    }

    public void setHideInMenu(Integer hideInMenu) {
        this.hideInMenu = hideInMenu;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public List<SysRoute> getChildren() {
        return children;
    }

    public void setChildren(List<SysRoute> children) {
        this.children = children;
    }

    public List<String> getAuthority() {
        return authority;
    }

    public void setAuthority(List<String> authority) {
        this.authority = authority;
    }

    public List<SysApi> getApiList() {
        return apiList;
    }

    public void setApiList(List<SysApi> apiList) {
        this.apiList = apiList;
    }
}