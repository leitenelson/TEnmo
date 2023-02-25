package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import java.util.List;
import java.awt.*;
import java.math.BigDecimal;

public class JdbcTransferDao implements TransferDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }




    @Override
    public List<Transfer> getAllTransfers(int id) {
        return null;
    }

    @Override
    public Transfer getTransferById(int Id) {
        return null;
    }

    @Override
    public String sendTransfer(int From, int To, BigDecimal amount) {
        return null;
    }

    @Override
    public String requestTransfer(int From, int To, BigDecimal amount) {
        return null;
    }

    @Override
    public List<Transfer> getPendingRequests(int userId) {
        return null;
    }

    @Override
    public String updateTransferRequest(Transfer transfer, int statusId) {
        return null;
    }

    public Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(results.getInt("transfer_id"));
        transfer.setTransferTypeId(results.getInt("transfer_type_id"));
        transfer.setTransferStatusId(results.getInt("transfer_status_id"));
        transfer.setAccountFrom(results.getInt("account_From"));
        transfer.setAccountTo(results.getInt("account_to"));
        transfer.setAmount(results.getBigDecimal("amount"));
        return transfer;
    }
}
