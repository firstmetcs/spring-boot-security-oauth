package com.firstmetcs.springbootsecurityoauth.dao.user;


import com.firstmetcs.springbootsecurityoauth.model.user.UserDomain;

import java.util.List;

public interface UserDao {


    int insert(UserDomain record);


    List<UserDomain> selectUsers();
}