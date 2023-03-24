package com.minis.context;

/**
 * @author 曹振远
 * @date 2023/03/15
 **/
public interface ApplicationEventPublisher {

    /**
     * 发布事件
     *
     * @param event 监听应用的事件
     */
    void publishEvent(ApplicationEvent event);

    void addApplicationListener(ApplicationListener listener);
}
