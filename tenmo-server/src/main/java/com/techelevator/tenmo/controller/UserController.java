package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("isAuthenticated()")
public class UserController {
    private UserDao userdao;

    public UserController (UserDao userdao) {
        this.userdao = userdao;
    }

    @GetMapping
    public List<User> listUsers() {
        return  userdao.findAll();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public User get(@PathVariable int id) {
        User user = userdao.getUserById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
        } else {
            return userdao.getUserById(id);
        }
    }
//    @RequestMapping( path = "", method = RequestMethod.GET)
//    public User userByIdOrName(@RequestParam(defaultValue = "") String user_name, @RequestParam(defaultValue = "0") int user_id) {
//
//        if( !user_name.equals("") ) {
//            return userdao.findByUsername(user_name);
//        }
//        if(user_id > 0) {
//            return userdao.getUserById(user_id);
//        }
//        else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found");
//        }
//    }



}
