package com.bjpowernode.service;

import com.bjpowernode.dao.StudentDao;
import com.bjpowernode.domain.Student;
import com.bjpowernode.util.SqlSessionUtil;
import com.bjpowernode.vo.StuAndClassVo;

import java.util.List;
import java.util.Map;

public class StudentServiceImpl implements StudentService{
    private StudentDao dao= SqlSessionUtil.getSession().getMapper(StudentDao.class);
    @Override
    public int insert(Student student) {
        int insert = dao.insert(student);
        return insert;
    }

    @Override
    public Student select(int id) {
        Student select = dao.select(id);
        return select;
    }

    @Override
    public List<Student> selectAll() {
        List<Student> list = dao.selectAll();
        return list;
    }

    @Override
    public List<Map<String,Object>> selectDuoBiao() {
        List<Map<String,Object>> map = dao.selectDuoBiao();
        return map;
    }

    @Override
    public List<StuAndClassVo> selectReturnRo() {
        List<StuAndClassVo> stuAndClassVos = dao.selectReturnRo();

        return stuAndClassVos;
    }
}
