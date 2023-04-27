package com.minis.batis;

import com.minis.jdbc.core.JdbcTemplate;
import com.minis.jdbc.core.PreparedStatementCallback;

/**
 * @author czy
 * @date 2023/04/26
 **/
public interface SqlSession {

    void setJdbcTemplate(JdbcTemplate jdbcTemplate);

    void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory);

    /**
     * 查询单条记录
     *
     * @param sqlId xml配置文件的sqlId,例namespace.id
     * @param args 参数
     * @param preparedStatementCallback 回调方法
     * @return 结果
     */
    Object selectOne(String sqlId, Object[] args, PreparedStatementCallback preparedStatementCallback);

    Integer update(String sqlId,Object [] args,PreparedStatementCallback preparedStatementCallback);
}
