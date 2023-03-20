package com.minis.test;

/**
 * @author 曹振远
 * @date 2023/03/14
 **/
public class AServiceImpl implements AService {

    private String name;
    private int level;

    private String property1;

    private String property2;

    private BaseService ref1;

    public AServiceImpl() {
    }

    public AServiceImpl(String name, int level) {
        this.name = name;
        this.level = level;
        System.out.println(this.name + "," + this.level);
    }

    public void sayHello() {
        System.out.println(this.property1 + "," + this.property2);
    }

    public String getProperty1() {
        return property1;
    }

    public void setProperty1(String property1) {
        this.property1 = property1;
    }

    public String getProperty2() {
        return property2;
    }

    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    public BaseService getRef1() {
        return ref1;
    }

    public void setRef1(BaseService bs) {
        this.ref1 = bs;
    }
}
