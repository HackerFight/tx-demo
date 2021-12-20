package com.qiuguan.tx.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.UUID;

/**
 * @author created by qiuguan on 2021/12/9 15:42
 */
@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert() {

        String sql = "INSERT INTO `user_info`(name,age,phone) VALUES(?,?,?)";
        String username = UUID.randomUUID().toString().substring(0, 5);
        jdbcTemplate.update(sql, username,19, "18368116334");
    }

    public void insertWithCheckedException() throws IOException {

        String sql = "INSERT INTO `user_info`(name,age,phone) VALUES(?,?,?)";
        String username = UUID.randomUUID().toString().substring(0, 5);
        jdbcTemplate.update(sql, username,22, "15033806009");

        throw new IOException("IO Exception");
    }
}
