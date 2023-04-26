package com.minis.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author czy
 * @date 2023/04/26
 * @Deprecated JDBC 里传参数的代码进行包装的组件
 **/
public class ArgumentPreparedStatementSetter {

    /**
     * 参数数组
     */
    private final Object[] args;

    public ArgumentPreparedStatementSetter(Object[] args) {
        this.args = args;
    }

    /**
     * 参数传进 PreparedStatement
     */
    public void setValues(PreparedStatement preparedStatement) throws SQLException {
        if (null != this.args) {
            for (int i = 0; i < this.args.length; i++) {
                Object arg = this.args[i];
                doSetValue(preparedStatement, i + 1, arg);
            }
        }
    }

    /**
     * 对某个参数，设置对应的参数值
     *
     * @param preparedStatement preparedStatement
     * @param i  参数下标
     * @param arg 参数
     */
    protected void doSetValue(PreparedStatement preparedStatement, int i, Object arg) throws SQLException {
        //判断参数类型，调用响应的JDBC set方法
        if (arg instanceof String) {
            preparedStatement.setString(i + 1, (String) arg);
        } else if (arg instanceof Integer) {
            preparedStatement.setInt(i + 1, (int) arg);
        } else if (arg instanceof java.util.Date) {
            preparedStatement.setDate(i + 1, new java.sql.Date(((java.util.Date) arg).getTime()));
        }
    }
}
