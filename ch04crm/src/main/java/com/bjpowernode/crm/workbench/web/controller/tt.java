package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

public class tt {
    public static void main(String[] args) {

        String id="082e3909314f47979dce678d8f6121ab";
        ClueService cs= new ClueServiceImpl();
        Clue c=cs.detail(id);
        System.out.println(c);



    }
}
