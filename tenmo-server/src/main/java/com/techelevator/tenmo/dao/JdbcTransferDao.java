package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.AllExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
@Component
public class JdbcTransferDao implements TransferDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AccountDao accountDao;
    private UserDao userDao;
    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }




    // Lists all transfers in account by user ID.
    @Override
    public List<Transfer> getAllTransfers(int id) {
        List<Transfer> list = new ArrayList<>();
        String sql = "SELECT t.*, u.username AS userFrom, v.username AS userTo FROM transfer t " +
                "JOIN account a ON t.account_from = a.account_id " +
                "JOIN account b ON t.account_to = b.account_id " +
                "JOIN tenmo_user u ON a.user_id = u.user_id " +
                "JOIN tenmo_user v ON b.user_id = v.user_id " +
                "WHERE a.user_id = ? OR b.user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql,id , id);
        while (results.next() ) {
            Transfer transfer = mapRowToTransfer(results);
            list.add(transfer);
        }
        return list;
    }

    // Getting transfer history by transfer ID.
    @Override
    public Transfer getTransferById(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);


        if(result.next()){
            transfer = mapRowToTransfer(result);
        }

        return transfer;
    }

    // Method for updating funds sent to an account using user ID.
    @Override
    public String sendTransfer(int userFrom, int userTo, BigDecimal amount) {
        if ( userFrom == userTo) {
            return "You are not allowed to send money to yourself";
        }

        int idFrom = accountDao.findAccountIdByUserId(userFrom);
        int idTo = accountDao.findAccountIdByUserId(userTo);

        if (amount.compareTo(accountDao.getBalance(userFrom)) == -1 && amount.compareTo(new BigDecimal(0)) == 1) {
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                    "VALUES (2, 2, ?, ?, ?)";
            jdbcTemplate.update(sql, idFrom, idTo, amount);
            accountDao.addToBalance(amount, userTo);
            accountDao.subtractFromBalance(amount, userFrom);
            return "Transfer complete";
        } else {
            return "Transfer invalid";
        }



    }


    @Override
    public String requestTransfer(int idFrom, int idTo, BigDecimal amount) {
        if ( idFrom == idTo) {
            return "You are not allowed to request money from yourself";
        }

        if ( amount.compareTo(new BigDecimal(0)) == 1) {
            String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                          "VALUES (1, 1, ?, ?, ?) returning transfer_id; ";
            jdbcTemplate.update(sql, idFrom, idTo, amount);
            return "Your request was sent";
        } else {
            return "There was a problem sending the request";
        }
    }

    // Code is not being used as this is part of the optional items.
    @Override
    public List<Transfer> getPendingRequests(int userId) {
        return null;
    }

    // Code is not being used as this is part of the optional items.
    @Override
    public String updateTransferRequest(Transfer transfer, int statusId) {
        return null;
    }

    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getInt("account_from"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        try {
            transfer.setUserFrom(results.getString("userFrom"));
            transfer.setUserTo(results.getString("userTo"));
        } catch (Exception e) {}
        try {
            transfer.setTransferType(results.getString("transfer_type_desc"));
            transfer.setTransferStatus(results.getString("transfer_status_desc"));
        } catch (Exception e) {}
        return transfer;
    }
}
