package com.qiuguan.tx.propagation.service;

import com.qiuguan.tx.bean.User1;
import com.qiuguan.tx.bean.User2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author created by qiuguan on 2021/12/14 15:21
 */
@SuppressWarnings("all")
@Service
public class CallService {

    @Autowired
    private UserService1 userService1;

    @Autowired
    private UserService2 userService2;


    //************************************  场景一 *************************************//

    /**
     *
     * 外围方法未开启事务, 就是 noTransactionAndNoExceptionByRequired() 方法没有事务
     * 插入“张三”、“李四”方法在’各自‘的事务中'独立'运行，外围方法异常不影响内部插入“张三”、“李四”方法独立的事务。
     *
     * 张三，李四 分别可以插入到各自的表中
     */
    public void noTransactionAndNoExceptionByRequired(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.REQUIRED)
        userService1.addRequired(user1);

        User2 user2=new User2();
        user2.setName("李四");
        //@Transactional(propagation = Propagation.REQUIRED)
        userService2.addRequired(user2);

        //因为外层方法没有事务，所以发生异常也不影响内部
        throw new RuntimeException("外层方法发生了异常");
    }


    /**
     *
     * 外层没有事务,就是  noTransactionAndHasExceptionByRequired() 方法没有事务
     * 插入“张三”、“李四”方法都在自己的事务中独立运行,所以插入“李四”方法抛出异常只会回滚插入“李四”方法，插入“张三”方法不受影响。
     *
     * 张三可以插入
     * 李四不能插入
     */
    public void noTransactionAndHasExceptionByRequired(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.REQUIRED)
        userService1.addRequired(user1);

        User2 user2=new User2();
        user2.setName("李四");
        //@Transactional(propagation = Propagation.REQUIRED)
        userService2.addRequiredWithException(user2);
    }

    //************************************ 场景二 ****************************************//

    /**
     *外围方法开启事务（就是 hasTransactionAndNoExceptionByRequired()方法标注了事务）
     * 外层方法开启了事务，当执行到内部的 userService1.addUser1(user1); 时，它会判断出当前已经
     * 存在事务，而且 userService1.addUser1(user1) 的事务类型是 Propagation.REQUIRED，
     * 则直接将 userService1.addUser1(user1) 的事务加入到外层已经存在的事务中
     *
     * 外围方法开启事务，内部方法加入外围方法事务，外围方法回滚，内部方法也要回滚。
     *
     * 通过debug 去看，确实在  userService1.addUser1(user1) 和 userService2.addUser2(user2) 中没有新建事务
     * 而是直接使用的外层方法的事务
     *
     * 张三，李四 都没有插入
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void hasTransactionAndNoExceptionByRequired(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.REQUIRED)
        userService1.addRequired(user1);

        User2 user2=new User2();
        user2.setName("李四");
        //@Transactional(propagation = Propagation.REQUIRED)
        userService2.addRequired(user2);

        //外层方法有事务，所以如果外层方法发生异常，内部也要回滚
        throw new RuntimeException("外层方法发生了异常");
    }


    /**
     *外围方法开启事务，内部方法加入外围方法事务，内部方法抛出异常回滚，外围方法感知异常致使整体事务回滚。
     *
     * 张三，李四都没有插入
     *
     * 通过debug 发现确实如此，当回滚后，继续往外抛出异常，最后还是通过外层事务方法进行整体回滚
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void hasTransactionAndHasExceptionByRequired(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.REQUIRED)
        userService1.addRequired(user1);

        User2 user2=new User2();
        user2.setName("李四");
        /**
         * 内层事务方法发生了异常
         */
        //@Transactional(propagation = Propagation.REQUIRED)
        userService2.addRequiredWithException(user2);
    }


    /**
     * 外围方法开启事务，内部方法加入外围方法事务，内部方法抛出异常回滚，即使方法被catch不被外围方法感知，整个事务依然回滚。
     * 张三，李四都没有插入
     *
     * 刚开始我以为肯定都可以插入，但是我debug 发现，当 userService2.addUser2WithException(user2); 执行的时候
     * 实际上它是抛出了异常（只是这里有一个假象，我在外层进行了捕获，以为他是成功的）
     * 因为内部方法的事务都是 REQUIRED， 所以将会使用外层事务，从而导致整体回滚
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void hasTransactionAndHasExceptionButHasTryByRequired(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.REQUIRED)
        userService1.addRequired(user1);

        User2 user2=new User2();
        user2.setName("李四");

        try {
            /**
             * 内层事务方法发生了异常,但是进行了catch
             */
            //@Transactional(propagation = Propagation.REQUIRED)
            userService2.addRequiredWithException(user2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以上结论：
     * 结论：以上试验结果我们证明在外围方法开启事务的情况下，Propagation.REQUIRED修饰的内部方法会加入到外围方法
     * 的事务中，所有Propagation.REQUIRED修饰的内部方法和外围方法均属于同一事务，只要一个方法回滚，整个事务均回滚。
     */


    //************************************场景三******************************************//

    /**
     * 外围方法没有事务，插入“张三”、“李四”方法都在自己的事务中独立运行,外围方法抛出异常回滚不会影响内部方法。
     * 因为外层方法没有事务，所以内部的事务方法都各自在自己的事务中执行
     *
     * 张三，李四都可以插入成功
     */
    public void noTransactionAndNoExceptionByRequiresNew(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.REQUIRES_NEW)
        userService1.addRequiresNew(user1);

        User2 user2=new User2();
        user2.setName("李四");
        //@Transactional(propagation = Propagation.REQUIRES_NEW)
        userService2.addRequiresNew(user2);

        //因为外层方法没有事务，所以发生异常也不影响内部
        throw new RuntimeException("外层方法发生了异常");
    }


    /**
     *外围方法没有开启事务，插入“张三”方法和插入“李四”方法分别开启自己的事务，
     * 插入“李四”方法抛出异常回滚，其他事务不受影响。
     */
    public void noTransactionAndHasExceptionByRequiresNew(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.REQUIRES_NEW)
        userService1.addRequiresNew(user1);

        User2 user2=new User2();
        user2.setName("李四");
        //@Transactional(propagation = Propagation.REQUIRES_NEW)
        userService2.addRequiresNewWithException(user2);
    }

    /**
     * 通过这两个方法我们证明了在外围方法未开启事务的情况下
     * Propagation.REQUIRES_NEW修饰的内部方法会新开启自己的事务，且开启的事务相互独立，互不干扰。
     */

    //************************************场景四******************************************//

    /**
     * 这里一共有几个事务？一共是3个
     * 外层方法一个（内层的 userService1.addRequired(user); 会和外层公用一个）
     * userService1.addRequiresNew(user1); 开启一个
     *  userService2.addRequiresNew(user2); 开启一个
     *
     *  因为张三1111 和外层公用一个事务，所以会回滚
     *  张三2222 会单独开启一个事务，不受外部事务的影响，所以可以提交，同理李四也一样
     *
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void hasTransactionAndNoExceptionByRequiresNew(){

        User1 user=new User1();
        user.setName("张三1111");
        //@Transactional(propagation = Propagation.REQUIRED)
        userService1.addRequired(user);

        User1 user1=new User1();
        user1.setName("张三2222");
        //@Transactional(propagation = Propagation.REQUIRES_NEW)
        userService1.addRequiresNew(user1);

        User2 user2=new User2();
        user2.setName("李四");
        //@Transactional(propagation = Propagation.REQUIRES_NEW)
        userService2.addRequiresNew(user2);

        //外层方法有事务，所以如果外层方法发生异常，内部也要回滚
        throw new RuntimeException("外层方法发生了异常");
    }


    /**
     *外围方法开启事务，插入“张三”方法和外围方法用同一个事务，插入“李四”方法、插入“王五”方法分别在独立的新建事务中。
     * 插入“王五”方法抛出异常，首先插入 “王五”方法的事务被回滚，异常继续抛出被外围方法感知，
     * 外围方法事务亦被回滚，故插入“张三”方法也被回滚。
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void hasTransactionAndHasExceptionByRequiresNew(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.REQUIRED)
        userService1.addRequired(user1);

        User2 user2=new User2();
        user2.setName("李四");
        //@Transactional(propagation = Propagation.REQUIRES_NEW)
        userService2.addRequiresNew(user2);

        User2 user3=new User2();
        user3.setName("王五");
        //@Transactional(propagation = Propagation.REQUIRES_NEW)
        userService2.addRequiresNewWithException(user3);
    }

    /**
     *外围方法开启事务，插入“张三”方法和外围方法一个事务，插入“李四”方法、插入“王五”方法分别在独立的新建事务中。
     * 插入“王五”方法抛出异常，首先插入“王五”方法的事务被回滚，异常被catch不会被外围方法感知，外围方法事务不回滚，
     * 故插入“张三”方法插入成功。
     *
     * 张三”插入，“李四”插入，“王五”未插入。
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void hasTransactionAndHasExceptionButHasTryByRequiresNew(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.REQUIRED)
        userService1.addRequired(user1);

        User2 user2=new User2();
        user2.setName("李四");
        //@Transactional(propagation = Propagation.REQUIRES_NEW)
        userService2.addRequiresNew(user2);

        User2 u3 = new User2();
        u3.setName("王五");
        try {
            //@Transactional(propagation = Propagation.REQUIRES_NEW)
            userService2.addRequiresNewWithException(user2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以上结论：
     * 在外围方法开启事务的情况下Propagation.REQUIRES_NEW修饰的内部方法依然会单独开启独立事务，
     * 且与外部方法事务也独立，内部方法之间、内部方法和外部方法事务均相互独立，互不干扰。
     */

    //************************************ 场景五 ******************************************//

    /**
     * 外围方法未开启事务，插入“张三”、“李四”方法在自己的事务中独立运行，
     * 外围方法异常不影响内部插入“张三”、“李四”方法独立的事务。
     */
    public void noTransactionAndNoExceptionByNested(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.NESTED)
        userService1.addNested(user1);

        User2 user2=new User2();
        user2.setName("李四");
        //@Transactional(propagation = Propagation.NESTED)
        userService2.addNested(user2);

        //因为外层方法没有事务，所以发生异常也不影响内部
        throw new RuntimeException("外层方法发生了异常");
    }

    /**
     *外围方法没有事务，插入“张三”、“李四”方法都在自己的事务中独立运行,
     * 所以插入“李四”方法抛出异常只会回滚插入“李四”方法，插入“张三”方法不受影响。
     */
    public void noTransactionAndHasExceptionByNested(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.NESTED)
        userService1.addNested(user1);

        User2 user2=new User2();
        user2.setName("李四");
        //@Transactional(propagation = Propagation.NESTED)
        userService2.addNestedWithException(user2);

    }

    /**
     * 通过这两个方法我们证明了在外围方法未开启事务的情况下Propagation.NESTED和Propagation.REQUIRES_NEW 作用相同，
     * 修饰的内部方法都会新开启自己的事务，且开启的事务相互独立，互不干扰。
     */


    //************************************ 场景六 ******************************************//

    /**
     * 	外围方法开启事务，内部事务为外围事务的子事务，外围方法回滚，内部方法也要回滚。
     * 	张三，李四 都不能插入
     */
    @Transactional
    public void hasTransactionAndNoExceptionByNested(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.NESTED)
        userService1.addNested(user1);

        User2 user2=new User2();
        user2.setName("李四");
        //@Transactional(propagation = Propagation.NESTED)
        userService2.addNested(user2);

        //
        throw new RuntimeException("外层方法发生了异常");
    }


    /**
     * 外围方法开启事务，内部事务为外围事务的子事务，内部方法抛出异常回滚，且外围方法感知异常致使整体事务回滚
     */
    @Transactional
    public void hasTransactionAndHasExceptionByNested(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.NESTED)
        userService1.addNested(user1);

        User2 user2=new User2();
        user2.setName("李四");
        //@Transactional(propagation = Propagation.NESTED)
        userService2.addNestedWithException(user2);

    }

    /**
     * 	外围方法开启事务，内部事务为外围事务的子事务，插入“李四”内部方法抛出异常，可以单独对子事务回滚。
     */
    @Transactional
    public void hasTransactionAndHasExceptionAndCatchByNested(){

        User1 user1=new User1();
        user1.setName("张三");
        //@Transactional(propagation = Propagation.NESTED)
        userService1.addNested(user1);

        User2 user2=new User2();
        user2.setName("李四");
        try {
            //@Transactional(propagation = Propagation.NESTED)
            userService2.addNestedWithException(user2);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 以上结论：
     * 以上试验结果我们证明在外围方法开启事务的情况下，Propagation.NESTED修饰的内部方法属于外部事务的子事务，
     * 外围主事务回滚，子事务一定回滚，而内部子事务可以单独回滚而不影响外围主事务和其他子事务
     */

    //************************************ 场景7 ******************************************//


    public void hasTransactionAndHasExceptionByMandatory(){

        User1 user1=new User1();
        user1.setName("张三");
        /**
         * 如果外层方法没有事务，则抛出异常
         * 如果外层方法有事务，则正常执行
         */
        //@Transactional(propagation = Propagation.MANDATORY)
        userService1.addMandaory(user1);

    }

    @Transactional
    public void hasTransactionAndHasExceptionByNever(){

        User1 user1=new User1();
        user1.setName("张三");
        /**
         * 如果外层方法没有事务，则正常执行
         * 如果外层方法有事务，则抛出异常
         */
        //@Transactional(propagation = Propagation.NEVER)
        userService1.addNever(user1);

    }
}

