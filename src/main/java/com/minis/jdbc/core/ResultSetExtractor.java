package com.minis.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author czy
 * @date 2023/04/26
 * @Deprecated 处理JDBC返回结果的组件
 **/
public interface ResultSetExtractor<T> {

    /**
     * 把JDBC返回的数据集映射为一个集合对象
     *
     * @param rs 结果集
     * @return 集合对象
     * @throws SQLException sql异常
     */
    T extractData(ResultSet rs) throws SQLException;
}
