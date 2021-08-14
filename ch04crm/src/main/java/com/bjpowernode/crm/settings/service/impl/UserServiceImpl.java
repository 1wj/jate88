package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import javax.security.auth.login.LoginException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    UserDao userDao= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    @Override
    public User login(String loginAct, String loginPwd,String ip) throws LoginException {
        System.out.println("进入到验证登录操作");
        Map<String,String> map=new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        User user=userDao.login(map);
        String expireTime= DateTimeUtil.getSysTime();
        if(expireTime.compareTo(user.getExpireTime())>0){
            throw new LoginException("账号以失效");
        }

        if("0".equals(user.getLockState())){
            throw new LoginException("账号以锁定");
        }
        if(!user.getAllowIps().contains(ip)){
            throw new LoginException("ip不在允许范围内");
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        return userDao.getUserList();
    }
}
