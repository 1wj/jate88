package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.ClueDao;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;

public class ClueServiceImpl implements ClueService {
    ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);

    @Override
    public Boolean save(Clue c) {
        int i=clueDao.save(c);
        Boolean flag=false;
        if(i==1)
            flag=true;
        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue c=clueDao.detail(id);
        return c;

    }
}
