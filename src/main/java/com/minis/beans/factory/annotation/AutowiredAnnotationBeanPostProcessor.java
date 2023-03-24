package com.minis.beans.factory.annotation;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * @author 曹振远
 * @date 2023/03/21
 * @Deprecated 处理@Autowired的注解的后置处理器
 **/
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {

    private BeanFactory beanFactory;

    /**
     * 初始化之前对所有的Bean检测是否携带@Autowired注解的属性并处理
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;

        Class<?> clazz = bean.getClass();
        //通过反射获取所有的声明字段
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length > 0) {
            for (Field field : fields) {
                //对每个属性进行判断，如果带有@Autowired注解则进行处理
                boolean isAutowired = field.isAnnotationPresent(Autowired.class);
                if (isAutowired) {
                    String fieldName = field.getName();
                    Object autowiredObj = this.getBeanFactory().getBean(fieldName);
                    try {
                        //设置属性值,完成注入
                        field.setAccessible(true);
                        field.set(bean, autowiredObj);
                        System.out.println("autowire " + fieldName + " for bean " + beanName);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // TODO Auto-generated method stub
        return null;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

}
