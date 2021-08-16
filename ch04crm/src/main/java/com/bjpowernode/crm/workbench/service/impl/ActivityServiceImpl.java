package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    ActivityDao activityDao= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    ActivityRemarkDao activityRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    //保存
    @Override
    public Boolean save(Activity activity) {
        Boolean flag=true;
        int i=activityDao.save(activity);
        if(i!=1){
            flag=false;
        }
        return flag;

    }

    //分页查询
    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
        System.out.println("进入service层");
        //取list
        List<Activity> dataList=activityDao.getActivityListByCondition(map);
        //取total
        int total=activityDao.getTotalByCondition(map);
        //将total和dataList封装到vo中
        PaginationVO<Activity> vo=new PaginationVO();
        vo.setTotal(total);
        vo.setDataList(dataList);
       return vo;
    }

    //删除
    @Override
    public Boolean delete(String[] ids) {
        System.out.println("进入到service层的delete方法");
        Boolean flag=true;
        //查询出需要删除的备注的数量
        int count1=activityRemarkDao.getCountByAids(ids);
        //删除备注，返回收到影响的条数(实际删除的数量)
        int count2=activityRemarkDao.deleteByAids(ids);
        if(count1 != count2){
            flag=false;
        }

        //删除市场活动
        int count3=activityDao.delete(ids);
        if(count3!=ids.length){
            flag=false;
        }

      return flag;
    }

    //查询
    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
        //取uList
        List<User> uList=userDao.getUserList();
        //取a
        Activity a=activityDao.getById(id);
        //将UList和a打包到map中
        Map<String,Object> map=new HashMap<>();
        map.put("uList",uList);
        map.put("a",a);
        //返回map就可以了
        return map;
    }

    //修改

    @Override
    public Boolean update(Activity activity) {
        Boolean flag=true;
        int i=activityDao.update(activity);
        if(i!=1){
            flag=false;
        }
        return flag;
    }
}
