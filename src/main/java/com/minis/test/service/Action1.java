package com.minis.test.service;

/**
 * @author czy
 * @date 2023/05/05
 **/
public class Action1 implements IAction{
    @Override
    public void doAction() {
        System.out.println("really do action1");

    }

    @Override
    public void doSomething() {
        System.out.println("really do something action1");
    }
}
