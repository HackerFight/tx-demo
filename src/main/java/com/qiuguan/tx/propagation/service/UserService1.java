package com.qiuguan.tx.propagation.service;

import com.qiuguan.tx.bean.User1;
import com.qiuguan.tx.propagation.dao.UserDao1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author created by qiuguan on 2021/12/14 14:07
 */
@SuppressWarnings("all")
@Service
public class UserService1 {

    @Autowired
    private UserDao1 userDao1;

    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired(User1 user) {
        userDao1.addUser1(user);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNew(User1 user){
        userDao1.addUser1(user);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void addNested(User1 user){
        userDao1.addUser1(user);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void addMandaory(User1 user){
        userDao1.addUser1(user);
    }

    @Transactional(propagation = Propagation.NEVER)
    public void addNever(User1 user){
        userDao1.addUser1(user);
    }
}

