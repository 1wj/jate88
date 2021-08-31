package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到线索控制器");
        String path = req.getServletPath();
        System.out.println(path);
        if("/workbench/clue/getUserList.do".equals(path)){
            getUserList(req,resp);
        }else if ("/workbench/clue/save.do".equals(path)){
            save(req,resp);
        }else if ("/workbench/clue/detail.do".equals(path)){
            detail(req,resp);
        }

    }

    private void detail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("跳转到线索详细信息页");
        String id=req.getParameter("id");
       ClueService cs= new ClueServiceImpl();
       Clue c=cs.detail(id);
      PrintJson.printJsonObj(resp,c);
      /*  session.setAttribute("cc",c);
    req.getRequestDispatcher("/workbench/clue/detail.jsp").forward(req,resp);
   */

    }

    private void save(HttpServletRequest req, HttpServletResponse resp) {
            System.out.println("执行线索的添加");
            String id=UUIDUtil.getUUID();
            String fullname=req.getParameter("fullname");
            String appellation=req.getParameter("appellation");
            String owner=req.getParameter("owner");
            String company=req.getParameter("company");
            String job=req.getParameter("job");
            String email=req.getParameter("email");
            String phone=req.getParameter("phone");
            String website=req.getParameter("website");
            String mphone=req.getParameter("mphone");
            String state=req.getParameter("state");
            String source=req.getParameter("source");
            String createBy= ((User)req.getSession().getAttribute("user")).getName();
            String createTime=DateTimeUtil.getSysTime();
            String description=req.getParameter("description");
            String contactSummary=req.getParameter("contactSummary");
            String nextContactTime=req.getParameter("nextContactTime");
            String address = req.getParameter("address");

        Clue c=new Clue();
            c.setId(id);
            c.setFullname(fullname);
            c.setAppellation(appellation);
            c.setOwner(owner);
            c.setCompany(company);
            c.setJob(job);
            c.setEmail(email);
            c.setPhone(phone);
            c.setWebsite(website);
            c.setMphone(mphone);
            c.setState(state);
            c.setSource(source);
            c.setCreateBy(createBy);
            c.setCreateTime(createTime);
            c.setDescription(description);
            c.setContactSummary(contactSummary);
            c.setNextContactTime(nextContactTime);
            c.setAddress(address);

            ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
            Boolean flag=cs.save(c);
            PrintJson.printJsonFlag(resp,flag);


    }

    private void getUserList(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("取的用户信息列表");
        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        PrintJson.printJsonObj(resp,uList);

    }


}
