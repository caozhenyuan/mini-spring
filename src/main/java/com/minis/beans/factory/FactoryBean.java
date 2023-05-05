package com.minis.beans.factory;

/**
 * @author czy
 * @date 2023/04/28
 **/
public interface FactoryBean<T> {

    /**
     * 创建对象加载到容器中
     *
     * @return
     * @throws Exception
     */
    T getObejct() throws Exception;

    /**
     * 返回类型
     *
     * @return
     */
    Class<?> getObjectType();


    /**
     * 是否是单实例 true:单实例，false:多实例
     *
     * @return
     */
    default boolean isSingleton() {
        return true;
    }

}
