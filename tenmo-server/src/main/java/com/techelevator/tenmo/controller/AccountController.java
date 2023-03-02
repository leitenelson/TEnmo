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

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("account")
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private AccountDao accountdao;
    private UserDao userdao;

    public AccountController (AccountDao accountdao, UserDao userdao) {
        this.accountdao = accountdao;
        this.userdao = userdao;
    }

    @GetMapping (path = "/balance")
    public BigDecimal getBalance(Principal principal) {

        User user = userdao.findByUsername(principal.getName());
        Account account = accountdao.findAccountByUserId(user.getId());
        return account.getBalance();
    }


}
