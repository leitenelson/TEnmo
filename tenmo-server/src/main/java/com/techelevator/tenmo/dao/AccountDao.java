package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {

    Account findAccountByUserId(int userId);
    int findAccountIdByUserId(int userId);

     BigDecimal addToBalance(BigDecimal addAmount, int id);
    BigDecimal subtractFromBalance(BigDecimal subtractAmount, int id);
    BigDecimal getBalance(int id);
   Integer findUserIdByAccountId(int accountId);
}
