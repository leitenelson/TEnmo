package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.util.Map;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
//            } else if (menuSelection == 2) {
//                viewTransferHistory();
//            } else if (menuSelection == 3) {
//                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() { //Kendra
//        // TODO Auto-generated method stub
        AccountService as = new AccountService(API_BASE_URL, currentUser);
        try {
            as.getBalance();
        } catch (NullPointerException e) {
            System.out.println("No balance found!");
        }
//        AuthenticatedUser currentUser = authenticationService.login();
//        if (currentUser != null) {
//            consoleService.printLoginMenu(currentUser);
//            String sqlGetCurrentBalance = "SELECT balance * FROM account WHERE user_id = ?;";
////            SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetCurrentBalance);
//            if (results.next()) {
//                Balance balance = ma
//            }
//        }
    }
//
//
//            //  System.out.println(activeService.getUserBalance(currentUser.getUser().getId()));
//
//        }
//
//        private void viewTransferHistory () { //Kendra
//            // TODO Auto-generated method stub
//            Map<Long, Transfer> transfer = transferService.getAllTransferByAccountId(currentUser, userAccountId);
//            consoleService.printTransferHistory(transfers);
//            long transferId = console.service.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
//            if (transferId != 0) {
//                Transfer transfer = transferService.getTransferByTransferId(currentUser, transferId);
//                consoleService.printTransferDetails(transfer);
//            }
//        }

    private void viewPendingRequests() { //Kendra
        // TODO Auto-generated method stub

    }

    private void sendBucks() {
        TransferService ts = new TransferService(API_BASE_URL, currentUser);
        ts.sendBucks();
    }

    private void requestBucks() {
        TransferService ts = new TransferService(API_BASE_URL, currentUser);
        ts.requestBucks();
    }

}
