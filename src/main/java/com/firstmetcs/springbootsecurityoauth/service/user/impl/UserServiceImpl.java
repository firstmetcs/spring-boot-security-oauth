package com.firstmetcs.springbootsecurityoauth.service.user.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.firstmetcs.springbootsecurityoauth.dao.user.UserDao;
import com.firstmetcs.springbootsecurityoauth.model.user.UserDomain;
import com.firstmetcs.springbootsecurityoauth.service.user.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * @author Administrator
 * @date 2020/12/16
 * @apiNote Created by Administrator on 2017/8/16.
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public int addUser(UserDomain user) {

        return userDao.insert(user);
    }

    /**
     * 这个方法中用到了我们开头配置依赖的分页插件pagehelper
     * 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
     *
     * @param pageNum  开始页数
     * @param pageSize 每页显示的数据条数
     * @return PageInfo<UserDomain>
     */
    @Override
    public PageInfo<UserDomain> findAllUser(int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        List<UserDomain> userDomains = userDao.selectUsers();
        PageInfo<UserDomain> result = new PageInfo<>(userDomains);
        return result;
    }
}
