package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {
    ActivityDao activityDao= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);

    @Override
    public Boolean save(Activity activity) {
        Boolean flag=true;
        int i=activityDao.save(activity);
        if(i!=1){
            flag=false;
        }
        return flag;

    }

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
}
