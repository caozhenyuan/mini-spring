package com.minis.core.env;

/**
 * @author 曹振远
 * @date 2023/03/22
 **/
public interface PropertyResolver {

    boolean containsProperty(String key);

    String getProperty(String key);

    String getProperty(String key, String defaultValue);

    <T> T getProperty(String key, Class<T> targetType);

    <T> T getProperty(String key, Class<T> targetType, String defaultValue);

    <T> Class<T> getPropertyAsClass(String key, Class<T> targetType);

    String getRequiredProperty(String key) throws IllegalStateException;

    <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException;

    String resolvePlaceholders(String text);

    String resolveRequiredPlaceholders(String text) throws IllegalStateException;
}
