package com.qiuguan.tx;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author created by qiuguan on 2021/12/14 18:27
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionFailureTest {


    //事务的本质是代理对象去工作，如果一个@Transacitonal 注解标注的方法a,内部调用了@Transactional 注解标注的方法b,那么此时
    //方法b就是一个普通的方法（本质上就是没有经过代理对象去调用）

}
