package com.minis.batis;

import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.PreparedStatementCallback;

/**
 * @author czy
 * @date 2023/04/26
 **/
public class DefaultSqlSession implements SqlSession {

    private JdbcTemplate jdbcTemplate;

    private SqlSessionFactory sqlSessionFactory;

    @Override
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public Object selectOne(String sqlId, Object[] args, PreparedStatementCallback preparedStatementCallback) {
        String sql = this.sqlSessionFactory.getMapperNode(sqlId).getSql();
        return jdbcTemplate.query(sql, args, preparedStatementCallback);
    }

    @Override
    public Integer update(String sqlId, Object[] args, PreparedStatementCallback preparedStatementCallback) {
        String sql = this.sqlSessionFactory.getMapperNode(sqlId).getSql();
        return jdbcTemplate.update(sql, args, preparedStatementCallback);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
