package com.firstmetcs.springbootsecurityoauth.service.security.impl;

import com.firstmetcs.springbootsecurityoauth.dao.security.SysApiMapper;
import com.firstmetcs.springbootsecurityoauth.model.security.SysApi;
import com.firstmetcs.springbootsecurityoauth.service.security.ApiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
 * @date 2020/12/16
 * @apiNote Created by Administrator on 2017/8/16.
 */
@Service(value = "apiService")
public class ApiServiceImpl implements ApiService {

    @Resource
    private SysApiMapper sysApiMapper;


    @Override
    public List<SysApi> findAll() {
        return sysApiMapper.findAll();
    }

    @Override
    public List<SysApi> selectByRouteId(Integer routeId) {
        return sysApiMapper.selectByRouteId(routeId);
    }

    @Override
    public int insertOrUpdateByPrimaryKey(SysApi record) {
        if (record.getId() == null) {
            return sysApiMapper.insert(record);
        }
        return sysApiMapper.updateByPrimaryKey(record);
    }
}
