package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入到用户控制器");
        String path = req.getServletPath();
        if("/settings/user/login.do".equals(path)){
            login(req,resp);
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) {

        String loginAct = req.getParameter("loginAct");
        String loginPwd = req.getParameter("loginPwd");
        //将密码转换成md5的密文形式
        loginPwd=MD5Util.getMD5(loginPwd);
        //接收浏览器端的ip地址
        String ip =req.getRemoteAddr();
        System.out.println("ip---------------------------"+ip);
        //未来业务层开发，统一使用代理类形态的接口对象
        UserService service= (UserService) ServiceFactory.getService(new UserServiceImpl());

        try {
            User user=service.login(loginAct,loginPwd,ip);
            req.getSession().setAttribute("user",user);
            //如果程序执行到此处，说明业务层没有为controller抛出任何异常
            PrintJson.printJsonFlag(resp,true);
        } catch (Exception e) {
            /**
             * 可以有两种手段来处理：
             *  1.将多项信息打包成map，将map解析为json串
             *  2.创建一个Vo
             *      private boolean success;
             *      private String msg;
             *  如果对于展现的信息将来还会大量使用，我们创建一个Vo类，使用方便
             *  如果对于展现的信息只在这个需求中使用，我们使用map就可以了
             */
            e.printStackTrace();
            String msg=e.getMessage();
            Map<String,Object> map=new HashMap<>();
            map.put("success",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(resp,map);
        }


    }
}
