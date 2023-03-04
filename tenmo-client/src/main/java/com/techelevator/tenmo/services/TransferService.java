package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferService {

    private String BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;
    private Transfer transfer = new Transfer();

    public TransferService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        BASE_URL = url;
    }


    public void sendBucks() {
        Scanner scanner = new Scanner(System.in);
        User[] users = null;
        try {
            users = restTemplate.exchange(BASE_URL + "users", HttpMethod.GET, makeAuthentication(), User[].class).getBody();
            printUsers(users);
            transfer.setAccountTo(Integer.parseInt(scanner.nextLine()));
            transfer.setAccountFrom(currentUser.getUser().getId());
            moneyTransfer();
                String output = restTemplate.exchange(BASE_URL + "transfer/send", HttpMethod.POST, makeTransfer(transfer), String.class).getBody();
                System.out.println(output);

        } catch (Exception e) {
            System.out.println("Input Error");
        }
    }

    public void requestBucks() {
        Scanner scanner = new Scanner(System.in);
        User[] users = null;
        try {
            users = restTemplate.exchange(BASE_URL + "listusers", HttpMethod.GET, makeAuthentication(), User[].class).getBody();
            printUsers(users);
            transfer.setAccountTo(currentUser.getUser().getId());
            transfer.setAccountFrom(Integer.parseInt(scanner.nextLine()));
            moneyTransfer();
                String output = restTemplate.exchange(BASE_URL + "request", HttpMethod.POST, makeTransfer(transfer), String.class).getBody();
                System.out.println(output);

        } catch (Exception e) {
            System.out.println("Input Error");
        }
    }

    private void printUsers(User[] users) {
        System.out.println("-------------------------------------------\r\n" +
                "Users\r\n" +
                "ID\t\tName\r\n" +
                "-------------------------------------------");
        for (User i : users) {
            if (i.getId() != currentUser.getUser().getId()) {
                System.out.println(i.getId() + "\t\t" + i.getUsername());
            }
        }
        System.out.print("-------------------------------------------\r\n" +
                "Enter ID of user you are sending to (0 to cancel): ");
    }

    private void moneyTransfer() {
        Scanner scanner = new Scanner(System.in);
        if (transfer.getAccountTo() != 0) {
            System.out.print("Enter amount: ");
            try {
                transfer.setAmount(new BigDecimal(Double.parseDouble(scanner.nextLine())));
            } catch (NumberFormatException e) {
                System.out.println("Error when entering amount");
            }
        }
    }



    private HttpEntity<Transfer> makeTransfer(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }

    private HttpEntity makeAuthentication() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

}