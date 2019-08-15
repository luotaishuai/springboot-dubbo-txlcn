package com.example.bank.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.TxTransaction;
import com.example.api.DepositWithdrawService;
import com.example.bank.entity.Bank;
import com.example.bank.repo.BankRepo;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @author anonymity
 * @create 2019-08-13 17:43
 **/
@Slf4j
@Service(version = "${demo.service.version}")
public class BankService implements DepositWithdrawService {

    @Resource
    private BankRepo bankRepo;

    @Override
    public String deposit(int amount) {
        return null;
    }


    @TxTransaction
    @Override
    public String withdraw(int amount) {
        log.info("bank service withdraw...");
        Bank alice = bankRepo.findByUsername("alice");
        alice.setAmount(alice.getAmount() - amount);
        Bank save = bankRepo.save(alice);
        return JSON.toJSONString(save);
    }
}
