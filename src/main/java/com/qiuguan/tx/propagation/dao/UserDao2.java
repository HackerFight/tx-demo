package com.qiuguan.tx.propagation.dao;

import com.qiuguan.tx.bean.User2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author created by qiuguan on 2021/12/14 15:14
 */
@Repository
public class UserDao2 {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser2(User2 user2) {
        String sql = "INSERT INTO `user2`(name) VALUES(?)";
        jdbcTemplate.update(sql, user2.getName());
    }
}
