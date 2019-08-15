package com.example.bank;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author anonymity
 * @create 2019-08-13 17:36
 **/
@EnableDistributedTransaction
@EnableDubbo
@SpringBootApplication
public class BankApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApplication.class, args);
    }
}
