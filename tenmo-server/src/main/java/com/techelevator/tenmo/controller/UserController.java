package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("users")
public class UserController {

    private final UserDao userDao;


    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    //endpoint /users (is the base endpoint for this call)
    @GetMapping()
    public List<User> list() {

        return userDao.findAll();
    }

    // endpoint is /users/"name?username="  Finds users id by their name
    @GetMapping("/name")
    public int findIdByUsername(@RequestParam String username) {
        return userDao.findIdByUsername(username);
    }

    //endpoint is /users/account?id="
    @GetMapping("/account")
    public User findUserByAccountId(@PathVariable int id) {
        return userDao.findUserByAccountId(id);
    }

}