package com.firstmetcs.springbootsecurityoauth.service.user;

import com.github.pagehelper.PageInfo;
import com.firstmetcs.springbootsecurityoauth.model.user.UserDomain;

/**
 * Created by Administrator on 2018/4/19.
 */
public interface UserService {

    int addUser(UserDomain user);

    PageInfo<UserDomain> findAllUser(int pageNum, int pageSize);
}
