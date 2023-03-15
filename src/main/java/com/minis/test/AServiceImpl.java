package com.minis.test;

import com.minis.test.AService;

/**
 * @author 曹振远
 * @date 2023/03/14
 **/
public class AServiceImpl implements AService {

    private String name;
    private int level;

    public AServiceImpl(String name, int level) {
        this.name = name;
        this.level = level;
    }

    @Override
    public void sayHello() {
        System.out.println("a service 1 say hello");
    }
}
