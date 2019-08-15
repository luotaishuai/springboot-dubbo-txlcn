package com.example.people.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.codingapi.txlcn.tc.annotation.TccTransaction;
import com.example.api.DepositWithdrawService;
import com.example.people.entity.PeopleAccount;
import com.example.people.repo.PeopleAccountRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author anonymity
 * @create 2019-08-13 17:50
 **/
@Slf4j
@Service
public class PeopleService {

    // 本地
    @Resource
    private PeopleAccountRepo peopleAccountRepo;

    // dubbo 远程
    @Reference(version = "${demo.service.version}")
    private DepositWithdrawService depositWithdrawService;

    @LcnTransaction
    public String deposit(int amount) {
        // 本地加钱
        PeopleAccount alice = peopleAccountRepo.findByUsername("alice");
        alice.setAmount(alice.getAmount() + amount);
        peopleAccountRepo.save(alice);
        // 远程就减钱
        String withdraw = depositWithdrawService.withdraw(amount);
        // 手动异常，验证被调用方事务是否回滚
        // int s = 1/0;
        return JSON.toJSONString(withdraw);
    }
}
