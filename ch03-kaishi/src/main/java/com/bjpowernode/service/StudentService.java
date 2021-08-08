package com.bjpowernode.service;

import com.bjpowernode.domain.Student;
import com.bjpowernode.vo.StuAndClassVo;

import java.util.List;
import java.util.Map;

public interface StudentService {
    int insert(Student student);
    Student select(int id);

    List<Student> selectAll();
    List<Map<String,Object>> selectDuoBiao();
    List<StuAndClassVo> selectReturnRo();
}
