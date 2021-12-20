package com.qiuguan.tx.propagation.dao;

import com.qiuguan.tx.bean.User1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


/**
 * @author created by qiuguan on 2021/12/14 15:12
 */
@Repository
public class UserDao1 {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser1(User1 user1) {
        String sql = "INSERT INTO `user1`(name) VALUES(?)";
        jdbcTemplate.update(sql, user1.getName());
    }
}
