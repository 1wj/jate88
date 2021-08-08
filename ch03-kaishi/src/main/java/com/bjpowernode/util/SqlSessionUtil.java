package com.bjpowernode.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class SqlSessionUtil {
    private static SqlSessionFactory factory=null;
    static {
        String config="mybatis-config.xml";
        InputStream in = null;
        try {
            in = Resources.getResourceAsStream(config);
            factory=new SqlSessionFactoryBuilder().build(in);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    //类似于池
    private static ThreadLocal<SqlSession> t=new ThreadLocal<>();
    public static SqlSession getSession(){
        SqlSession sqlSession =t.get();
        if(sqlSession==null){
            sqlSession=factory.openSession();
            t.set(sqlSession);
        }
        return sqlSession;
    }
    public static void closeSqlSession(SqlSession sqlSession){
        sqlSession.close();
        //这句必须加，特别容易忘
        t.remove();
    }
}
