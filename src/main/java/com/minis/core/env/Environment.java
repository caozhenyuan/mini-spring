package com.minis.core.env;

/**
 * @author 曹振远
 * @date 2023/03/22
 * @Deprecated 用于获取属性
 **/
public interface Environment extends PropertyResolver {

    String[] getActiveProfiles();

    String[] getDefaultProfiles();

    boolean acceptsProfiles(String... profiles);
}
