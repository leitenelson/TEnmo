package com.techelevator.tenmo.services;

import java.math.BigDecimal;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;


public class AccountService {

    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;
    private Account accounts = new Account();
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
        } catch (RestClientException e) {
            System.out.println("Error getting balance");
        }
        return balance2;
    }

    public Integer getUserIdByAccountId( int accountId) {


        Integer number =0;
        try {
           number = restTemplate.exchange(BASE_URL + "account/" + accountId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    Integer.class).getBody();


        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }

        return number;
    }
    public User getUserByAccountId(int accountId){
        User user = null;
        try {
          user =  restTemplate.exchange(
                    BASE_URL+ "users/account/" + accountId,
                    HttpMethod.GET,
                    makeAuthEntity(),
                    User.class).getBody();


        } catch(RestClientResponseException e) {
            System.out.println("Could not complete request. Code: " + e.getRawStatusCode());
        } catch(ResourceAccessException e) {
            System.out.println("Could not complete request due to server network issue. Please try again.");
        }

        return user;

    }
    private HttpEntity makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }


}
