package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {

    Account findAccountByUserId(int userId);
    int findAccountIdByUserId(int userId);

}
