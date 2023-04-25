package com.minis.jdbc.core;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * @author czy
 * @date 2023/04/25
 * @Deprecated 实现基本的 JDBC 访问框架
 **/
public class JdbcTemplate {

    private DataSource dataSource;

    public JdbcTemplate() {

    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Object query(StatementCallback statementCallback) {
        Connection con = null;
        Statement stmt = null;

        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            return statementCallback.doInStatement(stmt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != stmt) {
                    stmt.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public Object query(String sql, Object[] args, PreparedStatementCallback preparedStatementCallback) {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            //初始化连接
            con = dataSource.getConnection();
            stmt = con.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                //按照不同的数据类型调用JDBC的不同设置方法
                if (arg instanceof String) {
                    stmt.setString(i + 1, (String) arg);
                } else if (arg instanceof Integer) {
                    stmt.setInt(i + 1, (int) arg);
                } else if (arg instanceof java.util.Date) {
                    stmt.setDate(i + 1, new java.sql.Date(((java.util.Date) arg).getTime()));
                }
            }
            return preparedStatementCallback.doInPreparedStatement(stmt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != stmt) {
                    stmt.close();
                }
                if (null != con) {
                    con.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
