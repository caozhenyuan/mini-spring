package com.minis.web.bind.support;

import com.minis.web.WebDataBinder;

/**
 * @author czy
 * @date 2023/04/04
 **/
public interface WebBindingInitializer {

    void initBinder(WebDataBinder binder);
}
