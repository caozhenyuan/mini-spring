package com.minis.test;

import com.minis.context.ClassPathXmlApplicationContext;

/**
 * @author 曹振远
 * @date 2023/03/14
 **/
public class Test1 {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        AService aservice = (AService) context.getBean("aservice");
        aservice.sayHello();
    }
}
