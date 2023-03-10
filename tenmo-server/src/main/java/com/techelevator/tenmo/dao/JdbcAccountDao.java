package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{
    private Account account;
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // Finding the account by user ID, and returning account.
    @Override
    public Account findAccountByUserId(int userId) {
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
            return mapRowToAccount(results);
        } else {
            return null;
        }
    }

    // Finding account ID by the user ID and returning account ID.
    @Override
    public int findAccountIdByUserId(int userId) {
        int accountId;
        try {
            accountId = jdbcTemplate.queryForObject("SELECT account_id FROM account WHERE user_id = ?", int.class, userId);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("UserId " + userId + " was not found.");
        }
        return accountId;
    }

    // Adding funds to account and updating balance using user ID.
    @Override
    public BigDecimal addToBalance(BigDecimal addAmount, int id) {
        Account account = findAccountByUserId(id);
        BigDecimal newBalance = account.getBalance().add(addAmount);
        System.out.println(newBalance);
        String sqlString = "UPDATE account SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sqlString, newBalance, id);
        } catch (Exception e) {
            System.out.println("Error accessing data");
        }
        return account.getBalance();
    }

    // Removing funds from account and updating balance using user ID.
    @Override
    public BigDecimal subtractFromBalance(BigDecimal subtractAmount, int id) {
        Account account = findAccountByUserId(id);
        BigDecimal newBalance = account.getBalance().subtract(subtractAmount);
        System.out.println(newBalance);
        String sqlString = "UPDATE account SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sqlString, newBalance, id);
        } catch (Exception e) {
            System.out.println("Error accessing data");
        }
        return account.getBalance();
    }

    // Getting account balance using the user ID.
    @Override
    public BigDecimal getBalance(int userId) {
        String sqlString = "SELECT balance FROM account WHERE user_id = ?";
        SqlRowSet results = null;
        BigDecimal balance = null;
        try {
            results = jdbcTemplate.queryForRowSet(sqlString, userId);
            if (results.next()) {
                balance = results.getBigDecimal("balance");
            }
        } catch (DataAccessException e) {
            System.out.println("Error accessing data");
        }
        return balance;
    }

    // Finding the user ID by using the account ID.
    @Override
    public Integer findUserIdByAccountId(int accountId) {
        String sql = "SELECT user_id from account WHERE account_id = ?";
        SqlRowSet results = null;
        Integer userId = null;

        try {
            userId = jdbcTemplate.queryForObject(sql, Integer.class,accountId);

        } catch (DataAccessException e) {
            System.out.println("Error accessing data");
        }
        return userId;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        Account account = new Account();
        account.setAccountId(rs.getInt("account_id"));
        account.setUserId(rs.getInt("user_id"));
        account.setBalance(rs.getBigDecimal("balance"));
        return account;
    }
}
