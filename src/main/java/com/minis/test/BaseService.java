package com.minis.test;

/**
 * @author 曹振远
 * @date 2023/03/17
 **/
public class BaseService {

    private BaseBaseService bbs;

    public BaseService(){

    }

    public BaseBaseService getBbs() {
        return bbs;
    }

    public void setBbs(BaseBaseService bbs) {
        this.bbs = bbs;
    }

    public void sayHello(){
        System.out.println("Base Service says hello");
        bbs.sayHello();
    }
}
