package com.techelevator.dao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Account;

import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcTransferDaoTest extends BaseDaoTests {

    private JdbcTransferDao sut;
    private Transfer testTransfer;
    private Transfer testTransfer2;

    @Before
    public void setup() {
        sut = new JdbcTransferDao(new JdbcTemplate(dataSource));
        testTransfer = new Transfer(4001, 1, 1, 2001, 2002, new BigDecimal("300.00"));
        testTransfer2 = new Transfer(4002, 1, 1, 2002, 2001, new BigDecimal("50.00"));
    }
    @Test
    public void getAllTransfers_returns_correct_number_of_transfers() {
        List<Transfer> transferList = sut.getAllTransfers(1001);

        Assert.assertEquals(2, transferList.size());
        assertTransfersMatch(testTransfer, transferList.get(0));
        assertTransfersMatch(testTransfer2, transferList.get(1));
    }
    private void assertTransfersMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getTransferStatusId(), actual.getTransferStatusId());
        Assert.assertEquals(expected.getTransferTypeId(), actual.getTransferTypeId());
        Assert.assertEquals(expected.getAccountTo(), actual.getAccountTo());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
    }
}
