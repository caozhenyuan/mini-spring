package com.minis.batis;

/**
 * @author czy
 * @date 2023/04/26
 **/
public interface SqlSessionFactory {

    SqlSession openSession();

    MapperNode getMapperNode(String name);
}
