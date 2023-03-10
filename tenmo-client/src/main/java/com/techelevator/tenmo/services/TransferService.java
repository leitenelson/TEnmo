package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class TransferService {

    private final String BASE_URL;
    private final RestTemplate restTemplate = new RestTemplate();
    private final AuthenticatedUser currentUser;
    private final Transfer transfer = new Transfer();

    private final AccountService accountService;

    public TransferService(String url, AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
        this.BASE_URL = url;
        accountService = new AccountService(url, currentUser);
    }


    public void sendBucks() {
        User[] users = null;
        Scanner scanner = new Scanner(System.in);
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

    // This is formatting for the Transfer History Dialog when transfer history is called.
    private void printTransfers(Transfer[] transfers) {
        System.out.println("Transfers");
        System.out.printf("%-12s %-24s %-12s%n", "ID", "From/To", "Amount");
        System.out.printf("%-12s %-24s %-12s%n", "-----------", "------------------------", "-----------");

        String fromTo = "";
        String userName = "";

        for (Transfer i : transfers) {
            if (currentUser.getUser().getUsername().equals(i.getUserFrom())) {
                System.out.printf("%-12s %-24s $%-12.2f%n", i.getTransferId(), "To: " + i.getUserTo(), i.getAmount());
                //System.out.printf(" %-24s $%-12.2f%n", "To: " + i.getUserTo(), i.getAmount());
            } else if ((currentUser.getUser().getUsername().equals(i.getUserTo()))) {
                System.out.printf("%-12s %-24s $%-12.2f%n", i.getTransferId(), "From: " + i.getUserFrom(), i.getAmount());
                //System.out.printf("%-12s %-24s $%-12.2f%n", currentUser.getUser().getId(), "To: " + i.getUserTo(), accountService.getBalance());
            } else {
                System.out.println("null");
            }
        }
        System.out.print("-------------------------------------------\r\n" +
                "Enter ID of transfer to view details (0 to cancel): ");
    }

    // This is the formatting when looking up a user's ID.
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

    // Method for viewing transfer detail.
    private void viewTransferDetail(Transfer transfer) {
        System.out.println("Id: " + transfer.getTransferId());
        System.out.println("From: " + transfer.getUserFrom());
        System.out.println("To: " + transfer.getUserTo());
        System.out.println("Amount: " + transfer.getAmount());

    }


    // Method for transferring money to another account.
    private void moneyTransfer() {
        Scanner scanner = new Scanner(System.in);
        if (transfer.getAccountTo() != 0) {
            System.out.print("Enter amount: ");
            try {
                transfer.setAmount(BigDecimal.valueOf(Double.parseDouble(scanner.nextLine())));
            } catch (NumberFormatException e) {
                System.out.println("Error when entering amount");
            }
        }
    }


    // Method for viewing transfer history.
    public void transferHistory() {
        Transfer[] transfer = null;

        try {
            transfer = restTemplate.exchange(BASE_URL + "transfer/" + currentUser.getUser().getId(),
                    HttpMethod.GET, makeAuthentication(), Transfer[].class).getBody();
            printTransfers(transfer);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        Scanner scanner = new Scanner(System.in);
        int transferId = Integer.parseInt(scanner.nextLine());
        if (transferId != 0) {
            for (Transfer i : transfer) {
                if (i.getTransferId() == transferId) {
                    viewTransferDetail(i);
                    break;
                }
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