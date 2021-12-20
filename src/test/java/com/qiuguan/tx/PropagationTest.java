package com.qiuguan.tx;

import com.qiuguan.tx.propagation.service.CallService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author created by qiuguan on 2021/12/14 14:13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PropagationTest {

    @Autowired
    private CallService callService;

    @Test
    public void call(){

        /****************** 场景一测试 ************************/
        //callService.noTransactionAndNoExceptionByRequired();

        //callService.noTransactionAndHasExceptionByRequired();


        /****************** 场景二测试 ************************/
        //callService.hasTransactionAndNoExceptionByRequired();

        //callService.hasTransactionAndHasExceptionByRequired();

        //callService.hasTransactionAndHasExceptionButHasTryByRequired();

        /********************* 场景三测试 ***************************/
        //callService.noTransactionAndNoExceptionByRequiresNew();

        /********************* 场景四 测试 ***************************/
        //callService.hasTransactionAndNoExceptionByRequiresNew();

        callService.hasTransactionAndHasExceptionByRequiresNew();

        //callService.hasTransactionAndHasExceptionButHasTryByRequiresNew();

        /********************* 场景五 测试 ***************************/
        //callService.noTransactionAndNoExceptionByNested();

        //callService.noTransactionAndHasExceptionByNested();


        /********************* 场景6 测试 ***************************/
        //callService.hasTransactionAndNoExceptionByNested();


        /********************* 场景7 测试 ***************************/
        //callService.hasTransactionAndHasExceptionByMandatory();

        //callService.hasTransactionAndHasExceptionByNever();

    }
}
