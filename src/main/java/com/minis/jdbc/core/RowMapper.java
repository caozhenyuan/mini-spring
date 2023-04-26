package com.minis.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author czy
 * @date 2023/04/26
 * @Deprecated 处理JDBC返回结果的组件
 **/
public interface RowMapper<T> {

    /**
     * 把 JDBC 返回的 ResultSet 里的某一行数据映射成一个对象
     *
     * @param rs     结果集
     * @param rowNum 行数
     * @return 结果对象
     * @throws SQLException sql异常
     */
    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
