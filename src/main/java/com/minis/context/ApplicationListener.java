package com.minis.context;

import java.util.EventListener;

/**
 * @author 曹振远
 * @date 2023/03/23
 **/
public class ApplicationListener implements EventListener {

    void onApplicationEvent(ApplicationEvent event){
        System.out.println(event.toString());
    }
}
