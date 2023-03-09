package com.techelevator.dao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Account;

import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JdbcTransferDaoTest extends BaseDaoTests {

    protected static final Transfer TRANSFER_1 = new Transfer(3001, 1, 1, 2001, 2002, new BigDecimal("100.00"));
    private static final Transfer TRANSFER_2 = new Transfer(4005, 2, 2, 5003, 5001, BigDecimal.valueOf(100.00));

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getTransferById() {

        Transfer transfer = sut.getTransferById(TRANSFER_1.getTransferId());
        assertNotNull("this boy null", transfer);
        assertTransfersMatch("do they match",TRANSFER_1,transfer);
    }
    private void assertTransfersMatch(String message, Transfer expected, Transfer actual){
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getTransferStatusId(), actual.getTransferStatusId());
        Assert.assertEquals(expected.getTransferTypeId(), actual.getTransferTypeId());
        Assert.assertEquals(expected.getAccountTo(), actual.getAccountTo());
        Assert.assertEquals(expected.getAmount(), actual.getAmount());
    }

}
