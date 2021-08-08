package com.bjpowernode;

import com.bjpowernode.domain.Student;
import com.bjpowernode.service.StudentService;
import com.bjpowernode.service.StudentServiceImpl;
import com.bjpowernode.util.ServiceFactory;
import com.bjpowernode.vo.StuAndClassVo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class t1 {
    @Test
    public void tt() throws IOException {
        StudentService service = (StudentService) ServiceFactory.getService(new StudentServiceImpl());
       /* Student student=new Student(6,"李四","e3@qq",25);
        int insert = service.insert(student);
        System.out.println(insert);*/
      //  Student select = service.select(6);
       // System.out.println(select);
        //List<Student> list= service.selectAll();
       // List<Map<String,Object>> listmap = service.selectDuoBiao();
        //打印方式一
      /*  for (Map<String,Object> map:listmap) {
            Set<String> set=   map.keySet();
            for (String key:set) {
               // System.out.println(key);
                System.out.println( "key::"+key+",value::"+map.get(key));
            }
            System.out.println("----------");
        }*/
        //打印方式二
      /*  for (Map<String,Object> map:listmap) {
                System.out.println( map);
            System.out.println("----------");
        }*/

        //vo  如果前端重复率不高就用map
        //map 如果前端重复率高就用vo
        List<StuAndClassVo> stuAndClassVos = service.selectReturnRo();
        for (StuAndClassVo vo:stuAndClassVos) {
            System.out.println(vo);
        }

    }
}
