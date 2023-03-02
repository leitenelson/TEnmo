package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.AuthenticatedUser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


public class AccountService {

    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    public AccountService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        BASE_URL = url;
    }

    public BigDecimal getBalance() {
        BigDecimal balance2 = new BigDecimal(0);
        try {
            //+ currentUser.getUser().getId()
            ResponseEntity<BigDecimal> balance = restTemplate.exchange(BASE_URL + "account/balance", HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance2 = balance.getBody();
            System.out.println("Your current account balance is: $" + balance2);
        } catch (RestClientException e) {
            System.out.println("Error getting balance");
        }
        return balance2;
    }

    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}
