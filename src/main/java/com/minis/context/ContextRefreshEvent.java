package com.minis.context;

import java.io.Serializable;

/**
 * @author 曹振远
 * @date 2023/03/23
 **/
public class ContextRefreshEvent extends ApplicationEvent implements Serializable {


    private static final long serialVersionUID = 1164860562223622218L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ContextRefreshEvent(Object source) {
        super(source);
    }

    public String toString(){
        return this.msg;
    }
}
