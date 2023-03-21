package com.minis.test;

import com.minis.beans.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;

/**
 * @author 曹振远
 * @date 2023/03/14
 **/
public class Test1 {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        AService aservice;
        try {
            aservice = (AService) context.getBean("aService");
        } catch (BeansException e) {
            throw new RuntimeException(e);
        }
        aservice.sayHello();
    }
}
