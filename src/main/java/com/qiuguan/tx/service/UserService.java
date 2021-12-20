package com.qiuguan.tx.service;

import com.qiuguan.tx.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * @author created by qiuguan on 2021/12/9 15:41
 *
 * 声明式事务演示
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;


    //用来演示事务回滚和提交
    //int i = 1/ 0;
    /**
     * @see ArithmeticException
     */
    @Transactional(rollbackFor = Exception.class, noRollbackFor = ArithmeticException.class)
    public void insert() {

       // userDao.insert();

        int i = 1/ 0;

        System.out.println("插入成功");

    }

    @Transactional(rollbackFor = Exception.class)
    public void insertWithCheckedException() throws IOException {

        userDao.insertWithCheckedException();

        System.out.println("插入成功");

    }

}
