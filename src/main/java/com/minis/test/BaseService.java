package com.minis.test;

import com.minis.beans.factory.annotation.Autowired;

/**
 * @author 曹振远
 * @date 2023/03/17
 **/
public class BaseService {

    @Autowired
    private BaseBaseService baseBaseService;

    public BaseService(){

    }

    public BaseBaseService getBbs() {
        return baseBaseService;
    }

    public void setBbs(BaseBaseService bbs) {
        this.baseBaseService = bbs;
    }

    public void sayHello(){
        System.out.println("Base Service says hello");
        baseBaseService.sayHello();
    }
}
