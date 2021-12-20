package com.qiuguan.tx.propagation.service;

import com.qiuguan.tx.bean.User2;
import com.qiuguan.tx.propagation.dao.UserDao2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author created by qiuguan on 2021/12/14 14:07
 */
@SuppressWarnings("all")
@Service
public class UserService2 {

    @Autowired
    private UserDao2 userDao2;

    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired(User2 user){
        userDao2.addUser2(user);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequiredWithException(User2 user){
        userDao2.addUser2(user);

        throw new RuntimeException("插入失败");
    }

    //******************************************************************



    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNew(User2 user){
        userDao2.addUser2(user);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNewWithException(User2 user){
        userDao2.addUser2(user);

        throw new RuntimeException("插入失败");
    }

    //*********************************************************************

    @Transactional(propagation = Propagation.NESTED)
    public void addNested(User2 user){
        userDao2.addUser2(user);
    }


    @Transactional(propagation = Propagation.NESTED)
    public void addNestedWithException(User2 user){
        userDao2.addUser2(user);

        throw new RuntimeException("插入失败");
    }
}
