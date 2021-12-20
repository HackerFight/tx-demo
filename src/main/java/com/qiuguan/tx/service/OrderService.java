package com.qiuguan.tx.service;

import com.qiuguan.tx.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author created by qiuguan on 2021/12/10 11:25
 *
 * 测试编程式事务
 */
@Service
public class OrderService {

    /**
     * 通过事务模板，这个相对简单
     */
    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 通过事务管理器来控制事务的提交和回滚，它是最本质的
     */
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private UserDao userDao;

    /**
     * 通过事务模板进行事务控制
     */
    public void saveOrderWithTransactionTemplate() {

        this.transactionTemplate.execute(status -> {

            try {
                this.userDao.insert();

                int i = 1/0;
            } catch (Exception e) {
                e.printStackTrace();
                /**
                 * 如果捕获了异常，则一定要在这里手动回滚，如果不进行异常捕获，那么如果抛出异常，则自动回滚，
                 * 请看 execute 方法, 本质上是通过事务管理器来进行事务提交和回滚的
                 */
                status.setRollbackOnly();
            }

            return null;
        });
    }

    /**
     * 通过事务管理器来控制事务
     */
    public void saveOrderWithTransactionManager(){
        //可以参考 DataSourceTransactionManagerAutoConfiguration

        DefaultTransactionDefinition df = new DefaultTransactionDefinition();
        TransactionStatus status = this.transactionManager.getTransaction(df);

        //**************** 业务逻辑 ****************
        try {
            userDao.insert();

            int i = 1 / 0;

            this.transactionManager.commit(status);
        } catch (Exception e) {
            e.printStackTrace();
            this.transactionManager.rollback(status);
        }

    }
}
