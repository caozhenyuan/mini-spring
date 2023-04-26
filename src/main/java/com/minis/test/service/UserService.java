package com.minis.test.service;

import com.minis.beans.factory.annotation.Autowired;
import com.minis.jdbc.core.JdbcTemplate;
import com.minis.test.entity.User;

import java.sql.ResultSet;
import java.util.List;

/**
 * @author czy
 * @date 2023/04/25
 **/
public class UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

//    public User getUserInfo(int userId) {
//        String sql = "select id,name,birthday from user where id =" + userId;
//        JdbcTemplate jdbcTemplate = new UserJdbcImpl();
//        return (User) jdbcTemplate.query(sql);
//    }


//    public User getUserInfo(int userId) {
//        String sql = "select id,name,birthday from user where id =" + userId;
//        return (User) jdbcTemplate.query(stmt -> {
//            ResultSet rs = stmt.executeQuery(sql);
//            User rtnUser = null;
//            if (rs.next()) {
//                rtnUser = new User();
//                rtnUser.setId(userId);
//                rtnUser.setName(rs.getString("name"));
//                rtnUser.setBirthday(new Date(rs.getDate("birthday").getTime()));
//            }
//            return rtnUser;
//        });
//    }

    public User getUserInfo(int userId) {
        String sql = "select id,name,birthday from user where id =" + userId;
        return (User) jdbcTemplate.query(sql, new Object[]{userId},
                (stmt -> {
                    ResultSet rs = stmt.executeQuery();
                    User rtnUser = null;
                    if (rs.next()) {
                        rtnUser = new User();
                        rtnUser.setId(userId);
                        rtnUser.setName(rs.getString("name"));
                        rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
                    }
                    return rtnUser;
                }));
    }

    public List<User> getUsers(int userId) {
        final String sql = "select id, name,birthday from users where id> ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            User rtnUser = new User();
            rtnUser.setId(rs.getInt("id"));
            rtnUser.setName(rs.getString("name"));
            rtnUser.setBirthday(new java.util.Date(rs.getDate("birthday").getTime()));
            return rtnUser;
        });
    }
}
