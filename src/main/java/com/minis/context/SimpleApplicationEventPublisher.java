package com.minis.context;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 曹振远
 * @date 2023/03/23
 **/
public class SimpleApplicationEventPublisher implements ApplicationEventPublisher {

    List<ApplicationListener> listeners = new ArrayList<>();

    @Override
    public void publishEvent(ApplicationEvent event) {
        for (ApplicationListener listener : listeners) {
            listener.onApplicationEvent(event);
        }
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.listeners.add(listener);
    }
}
