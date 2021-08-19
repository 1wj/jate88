package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SysInitListener implements ServletContextListener {
    /*

     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("上下文域对象创建了");
        System.out.println("服务器缓存处理数据字典开始---------------------------------------------");

        ServletContext application = event.getServletContext();
        //取数据字典
        //application.setAttribute("key",数据字典);
        DicService ds= (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map= ds.getAll();
        //先找从map中所有的key
        Set<String> set=map.keySet();
        for (String key:set ) {
            //依次把每个
            application.setAttribute(key,map.get(key));
        }
        System.out.println("服务器缓存处理数据字典结束---------------------------------------------");

    }
}
