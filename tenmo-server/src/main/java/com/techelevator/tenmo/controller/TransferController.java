package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

//get the information
@RestController
public class TransferController {

    private TransferDao dao;

    public TransferController(TransferDao dao) {
        this.dao = dao;
    }

    //List of transfers
    @RequestMapping(value = "/account/transfer/{id}", method = RequestMethod.GET)
    public List<Transfer> listAll(@PathVariable int transactionId){
        if(transactionId > 0){
            return dao.getAllTransfers(transactionId);
        }
        return null;
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer listOne(@PathVariable int id) { //when you write it sends it to the sql
        Transfer transfer = dao.getTransferById(id);
        if (transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer Not Found");
        } else {
            return transfer;
        }
    }

    //sendTransfer --- just sending
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public String send(@RequestBody Transfer transfer) {
        return dao.sendTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }

    //requestTransfer --- user request a transfer from someone
    @RequestMapping(path="/request"  ,    method = RequestMethod.POST)
    public String request(@RequestBody Transfer transfer) {
        return dao.requestTransfer(transfer.getAccountFrom(), transfer.getAccountTo(), transfer.getAmount());
    }
    //getPendingRequests
    @RequestMapping(path = "/request/{id}", method = RequestMethod.GET)
    public List<Transfer> getRequest(@PathVariable int id){
        List<Transfer> listTransfer = dao.getPendingRequests(id);
        if(listTransfer == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pending Requests Not Found" );
        }else{
            return dao.getPendingRequests(id);
        }

    }

    //updateTransferRequest "put"
    @RequestMapping(path = "/transfer/status/{id}", method = RequestMethod.PUT)
    public String update(@RequestBody Transfer transfer, @PathVariable int id){
        String updatedTransfer = dao.updateTransferRequest(transfer, id);
        if(updatedTransfer == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer to be updated not found");
        }else {
            return updatedTransfer;
        }

    }



}