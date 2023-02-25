package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {
    public List<Transfer> getAllTransfers(int id);
    public Transfer getTransferById(int transactionId);
    public String sendTransfer(int accountFrom, int accountTo, BigDecimal amount);
    public String requestTransfer(int accountFrom, int accountTo, BigDecimal amount);
    public List<Transfer> getPendingRequests(int id);
    public String updateTransferRequest(Transfer transfer, int statusId);
}
