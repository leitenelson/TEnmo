package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/account")
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountdao;
    private UserDao userdao;

    public AccountController (AccountDao accountdao) {
        this.accountdao = accountdao;
        this.userdao = userdao;
    }

    @GetMapping
    public Account myAccount(@AuthenticationPrincipal User user) {
        int id = user.getId();
        Account account = accountdao.findAccountByUserId(id);
        return account;
    }


}
