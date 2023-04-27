package com.minis.jdbc.core;

import com.minis.jdbc.pool.PooledConnection;
import com.minis.jdbc.pool.PooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @author czy
 * @date 2023/04/25
 * @Deprecated 实现基本的 JDBC 访问框架
 **/
public class JdbcTemplate {

    private PooledDataSource dataSource;

    public JdbcTemplate() {

    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

//    public Object query(StatementCallback statementCallback) {
//        Connection con = null;
//        Statement stmt = null;
//
//        try {
//            con = dataSource.getConnection();
//            stmt = con.createStatement();
//            return statementCallback.doInStatement(stmt);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (null != stmt) {
//                    stmt.close();
//                }
//                if (null != con) {
//                    con.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        return null;
//    }
//
//    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
//        RowMapperResultSetExtractor<T> rowMapperResultSetExtractor = new RowMapperResultSetExtractor<>(rowMapper);
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//        try {
//            connection = dataSource.getConnection();
//            preparedStatement = connection.prepareStatement(sql);
//
//            ArgumentPreparedStatementSetter statementSetter = new ArgumentPreparedStatementSetter(args);
//            statementSetter.setValues(preparedStatement);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            return rowMapperResultSetExtractor.extractData(resultSet);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (null != preparedStatement) {
//                    preparedStatement.close();
//                }
//                if (null != connection) {
//                    connection.close();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }


    public Object query(String sql, Object[] args, PreparedStatementCallback preparedStatementCallback) {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            //通过data source拿数据库连接
            con = dataSource.getConnection();
            stmt = con.prepareStatement(sql);
            //
            //通过argumentSetter统一设置参数值
            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(stmt);
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
                dataSource.releaseConnection((PooledConnection)con);
            }
        }
        return null;
    }

    public Integer update(String sql, Object[] args, PreparedStatementCallback preparedStatementCallback) {
        Connection con = null;
        PreparedStatement stmt = null;

        try {
            //通过data source拿数据库连接
            con = dataSource.getConnection();
            stmt = con.prepareStatement(sql);
            //
            //通过argumentSetter统一设置参数值
            ArgumentPreparedStatementSetter argumentSetter = new ArgumentPreparedStatementSetter(args);
            argumentSetter.setValues(stmt);
            return (Integer) preparedStatementCallback.doInPreparedStatement(stmt);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != stmt) {
                    stmt.close();
                }
                if (null != con) {
                    dataSource.releaseConnection((PooledConnection)con);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
