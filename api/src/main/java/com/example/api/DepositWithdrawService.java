package com.example.api;

/**
 * @author anonymity
 * @create 2019-08-13 17:44
 **/
public interface DepositWithdrawService {

    String deposit(int amount);

    String withdraw(int amount);
}
