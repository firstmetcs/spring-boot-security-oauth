package com.firstmetcs.springbootsecurityoauth.service.security.impl;

import com.firstmetcs.springbootsecurityoauth.dao.security.SysRoleRouteMapper;
import com.firstmetcs.springbootsecurityoauth.dao.security.SysRouteMapper;
import com.firstmetcs.springbootsecurityoauth.model.security.SysRole;
import com.firstmetcs.springbootsecurityoauth.model.security.SysRoleRoute;
import com.firstmetcs.springbootsecurityoauth.model.security.SysRoute;
import com.firstmetcs.springbootsecurityoauth.service.security.RouteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @date 2020/12/16
 * @apiNote Created by Administrator on 2017/8/16.
 */
@Service(value = "routeService")
public class RouteServiceImpl implements RouteService {

    @Resource
    private SysRouteMapper sysRouteMapper;
    @Resource
    private SysRoleRouteMapper sysRoleRouteMapper;


    @Override
    public List<SysRoute> selectWithRoleByUserIdAndPid(String userId, Integer pid) {
        return sysRouteMapper.selectWithRoleByUserIdAndPid(userId, pid);
    }

    @Override
    public List<SysRoute> selectByPid(Integer pid) {
        return sysRouteMapper.selectByPid(pid);
    }

    @Override
    public int insert(SysRoute record) {
        return sysRouteMapper.insert(record);
    }

    @Override
    public int insertSelective(SysRoute record) {
        return sysRouteMapper.insertSelective(record);
    }

    @Override
    public List<SysRoute> getAllParentRouteById(Integer id) {
        List<SysRoute> routeList = new ArrayList<>();
        getParentRouteById(id, routeList);
        return routeList;
    }

    @Override
    public List<SysRoute> selectByRoleId(Integer roleId) {
        return sysRouteMapper.selectByRoleId(roleId);
    }

    @Override
    public int modifyRouteOfRole(SysRole record) {
        List<SysRoleRoute> roleRouteList = new ArrayList<>();
        List<SysRoute> routeList = new ArrayList<>(record.getRouteList());
        record.getRouteList().forEach(item -> {
            getParentRouteById(item.getId(), routeList);
        });

        ArrayList<SysRoute> collect = routeList.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SysRoute::getId))), ArrayList::new)
        );

        collect.forEach(item -> {
            roleRouteList.add(new SysRoleRoute() {{
                setRoleId(record.getId());
                setRouteId(item.getId());
            }});
        });

        sysRoleRouteMapper.deleteByRoleId(record.getId());

        if (!roleRouteList.isEmpty()) {
            sysRoleRouteMapper.batchInsert(roleRouteList);
        }

        return 0;
    }

    @Override
    public int updateByPrimaryKey(SysRoute record) {
        return sysRouteMapper.updateByPrimaryKey(record);
    }

    @Override
    public int insertOrUpdateByPrimaryKey(SysRoute record) {
        if (record.getId() == null) {
            return sysRouteMapper.insert(record);
        }
        return sysRouteMapper.updateByPrimaryKey(record);
    }

    private SysRoute getParentRouteById(Integer id, List<SysRoute> routeList) {
        SysRoute sysRoute = sysRouteMapper.selectByPrimaryKey(id);
        if (sysRoute.getPid() != 0) {
            routeList.add(getParentRouteById(sysRoute.getPid(), routeList));
        }
        return sysRoute;
    }
}
