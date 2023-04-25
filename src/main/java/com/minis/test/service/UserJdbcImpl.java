package com.minis.test.service;

import com.minis.jdbc.core.JdbcTemplate;
import com.minis.test.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author czy
 * @date 2023/04/25
 * @Deprecated 对User表做数据访问
 **/
public class UserJdbcImpl extends JdbcTemplate {
//    @Override
//    protected Object doInStatement(ResultSet rs) {
//        //从JDBC数据集读取数据，并生成对象返回
//        User rtnUser = null;
//        try {
//            if (rs.next()) {
//                rtnUser = new User();
//                rtnUser.setId(rs.getInt("id"));
//                rtnUser.setName(rs.getString("name"));
//                rtnUser.setBirthday(rs.getDate("birthday"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return rtnUser;
//    }
}
