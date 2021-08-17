package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.*;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到市场活动控制器");
        String path = req.getServletPath();
        System.out.println(path);
        if("/workbench/activity/getUserList.do".equals(path)){
            getUserList(req,resp);
        }else if("/workbench/activity/save.do".equals(path)){
           save(req,resp);
        }else if("/workbench/activity/pageList.do".equals(path)){
            pageList(req,resp);
        }else if("/workbench/activity/delete.do".equals(path)){
            delete(req,resp);
        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(req,resp);
        }else if("/workbench/activity/update.do".equals(path)){
            update(req,resp);
        }else if("/workbench/activity/detail.do".equals(path)){
            detail(req,resp);
        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)){
            getRemarkListByAid(req,resp);
        }else if("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(req,resp);
        }else if("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(req,resp);
        }

    }
    //添加一个备注对象
    private void saveRemark(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行添加备注操作");
        String noteContent = req.getParameter("noteContent");
        String activityId = req.getParameter("activityId");
        String id=UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy=((User)req.getSession().getAttribute("user")).getName();
        String editFlag="0";
        ActivityRemark ar=new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setActivityId(activityId);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=as.saveRemark(ar);

        Map<String,Object> map=new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(resp,map);


    }

    //删除备注操作
    private void deleteRemark(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("删除备注操作");
        String id = req.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=as.deleteRemark(id);
        PrintJson.printJsonFlag(resp,flag);
    }

    //根据市场活动id,取得备注信息列表（一个集合）
    private void getRemarkListByAid(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("根据市场活动id,取得备注信息列表");
        String activityId = req.getParameter("activityId");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> arList = as.getRemarkListByAid(activityId);
        PrintJson.printJsonObj(resp,arList);
    }

    //获取备注信息的一个对象
    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到跳转到详细信息页的操作");
        String id = req.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity a=as.detail(id);
        req.setAttribute("a",a);
        req.getRequestDispatcher("/workbench/activity/detail.jsp").forward(req,resp);
    }

    //修改
    private void update(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行市场活动修改操作");

        String id = req.getParameter("id");
        String owner=req.getParameter("owner");
        String name=req.getParameter("name");
        String startDate=req.getParameter("startDate");
        String endDate=req.getParameter("endDate");
        String cost=req.getParameter("cost");
        String description=req.getParameter("description");
        //修改时间：当前系统时间
        String editTime= DateTimeUtil.getSysTime();
        //修改人：当前登录用户
        String editBy=((User)req.getSession().getAttribute("user")).getName();
        Activity activity=new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditBy(editBy);
        activity.setEditTime(editTime);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Boolean flag=as.update(activity);
        PrintJson.printJsonFlag(resp,flag);
    }

    //查询
    private void getUserListAndActivity(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到查询用户信息列表和根据市场活动id查询单条记录的操作");
        String id = req.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object>map=as.getUserListAndActivity(id);
        PrintJson.printJsonObj(resp,map);
    }

    //删除市场活动
    private void delete(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行删除市场活动操作");
        String[] ids = req.getParameterValues("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
       // ActivityService as=new ActivityServiceImpl();
        Boolean flag=as.delete(ids);
        PrintJson.printJsonFlag(resp,flag);
    }

    //分页查询
    private void pageList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("进入到市场活动信息列表的操作（结合条件查询+分页）");
        String name = req.getParameter("name");
        String owner = req.getParameter("owner");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String pageNoStr = req.getParameter("pageNo");
        int pageNo=Integer.valueOf(pageNoStr);
        String pageSizeStr = req.getParameter("pageSize");
        int pageSize=Integer.valueOf(pageSizeStr);
        //计算略过的记录数 (页数-1)*条数
        int skipCount=(pageNo-1)*pageSize;

        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        //不再使用动态代理
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
       // ActivityService as= new ActivityServiceImpl();
        /*
                使用vo来装返回值，将来分页查询每个模块都有
                vo
                PaginationVO<T>
                    private List<T> dataList
                    private int total
         */
        PaginationVO<Activity> vo=as.pageList(map);
        PrintJson.printJsonObj(resp,vo);
    }

    //市场活动的添加
    private void save(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("执行市场活动的添加操作");

        String id = UUIDUtil.getUUID();
        String owner=req.getParameter("owner");
        String name=req.getParameter("name");
        String startDate=req.getParameter("startDate");
        String endDate=req.getParameter("endDate");
        String cost=req.getParameter("cost");
        String description=req.getParameter("description");
        //创建时间：当前系统时间
        String createTime= DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy=((User)req.getSession().getAttribute("user")).getName();
        Activity activity=new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
         Boolean flag=as.save(activity);
          PrintJson.printJsonFlag(resp,flag);


    }

    //活动用户信息列表
    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("取得用户信息列表");
        UserService service= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList= service.getUserList();
        PrintJson.printJsonObj(resp,uList);
    }

}
