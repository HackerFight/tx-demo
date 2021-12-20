package com.qiuguan.tx;

import com.qiuguan.tx.service.OrderService;
import com.qiuguan.tx.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author created by qiuguan on 2021/12/9 15:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTest {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;


    /**
     * 声明式事务
     */
    @Test
    public void testTransaction() {
        userService.insert();
    }

    /**
     * 声明式事务-受检异常
     */
    @Test
    public void testTransactionWithCheckedException() throws Exception {
        userService.insertWithCheckedException();
    }

    /**
     * 编程式事务-TransactionTemplate
     */
    @Test
    public void testTransactionWithTemplate(){
        orderService.saveOrderWithTransactionTemplate();
    }

    /**
     * 通过事务管理器来控制事务
     */
    @Test
    public void testTransactionWithTransactionManager(){
        orderService.saveOrderWithTransactionManager();
    }
}
