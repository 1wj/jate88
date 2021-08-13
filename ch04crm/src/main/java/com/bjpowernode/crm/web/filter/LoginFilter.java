package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入到验证有没有登陆过得过滤器");
        //用来做登陆验证的
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;

        //默认不拦截的 一个登陆页面，一个ajax查找登陆的servl
        String path=request.getServletPath();
        System.out.println(path);
        if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            filterChain.doFilter(servletRequest,servletResponse);
        }else {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            //如果user不为空，就说明登陆过
            // 与之前的request.getSession(false)类似,之前的比的是是否有柜子  这个比的是柜子里面是否有东西 ，没有就不行
            if(user!=null){
                filterChain.doFilter(servletRequest,servletResponse);
            }else {
                //没有登陆过
                //重定向到登陆页
             /*
                转发：使用的是一种特殊的绝对路径的使用方式，前面不加/项目名，也被称为内部路径
                重定向：使用的是传统的绝对路径的写法，前面必须要有/项目名，后面根具体的资源路径
              */
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }



    }
}
