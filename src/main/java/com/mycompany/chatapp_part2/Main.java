package com.mycompany.chatapp;

import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        // Scanner lets the user type input into the program
        Scanner input = new Scanner(System.in);

        // Create a Login object so we can use its methods
        Login login = new Login();

        // -------------------------------------------------------
        // WELCOME MESSAGE
        // -------------------------------------------------------
        System.out.println("==========================================");
        System.out.println("   Welcome to QuickChat Messaging App!   ");
        System.out.println("==========================================");
        System.out.println("Please register and log in to get started.");
        System.out.println();

        // -------------------------------------------------------
        // REGISTRATION SECTION
        // -------------------------------------------------------
        System.out.println("=== STEP 1: REGISTER ===");

        System.out.print("Choose a username: ");
        String username = input.nextLine();

        System.out.print("Choose a password: ");
        String password = input.nextLine();

        System.out.print("Enter your SA phone number (+27...): ");
        String phone = input.nextLine();

        // Try to register — the method returns a message telling us if it worked
        String registerResult = login.registerUser(username, password, phone);
        System.out.println(registerResult);
        System.out.println();

        // -------------------------------------------------------
        // LOGIN SECTION
        // -------------------------------------------------------
        System.out.println("=== STEP 2: LOG IN ===");

        System.out.print("Enter your username: ");
        String loginUsername = input.nextLine();

        System.out.print("Enter your password: ");
        String loginPassword = input.nextLine();

        // Check if the username and password are correct
        boolean loggedIn = login.loginUser(loginUsername, loginPassword);

        // Get the login result and message 
        String loginResult = login.returnloginStatus(loggedIn);
        System.out.println(loginResult);
        System.out.println();

        // If login failed, stop the program 
        if (!loggedIn) {
            System.out.println("Access denied. Please restart and try again.");
            return;
        }

        // -------------------------------------------------------
        // QUICKCHAT SECTION
        // -------------------------------------------------------
        System.out.println("==========================================");
        System.out.println("          Welcome to QuickChat!          ");
        System.out.println("==========================================");
        System.out.println("You can send up to 5 messages.");
        System.out.println("Type 0 as the recipient number to quit early.");
        System.out.println();

        // messageNumber acts as a counter for sent messages
        int messageNumber = 0;

        // set maxium number of messages allowed 
        int maxMessages = 5;

        // -------------------------------------------------------
        // WHILE LOOP — keeps going until we reach 5 messages or the user quits
        // -------------------------------------------------------
        while (messageNumber < maxMessages) {

            // Add 1 to the counter each time the loop runs
            messageNumber++;

            System.out.println("------------------------------------------");
            System.out.println("Message " + messageNumber + " of " + maxMessages);
            System.out.println("------------------------------------------");

            // Create a Message object for this loop round
            Message msg = new Message(messageNumber);

            // --- Get and validate the recipient number ---
            String recipient = "";
            boolean validRecipient = false;

            while (!validRecipient) {
                System.out.print("Enter recipient number (+27...) or 0 to quit: ");
                recipient = input.nextLine().trim();

                // If the user types 0, exit the program early
                if (recipient.equals("0")) {
                    System.out.println("\nQuitting QuickChat...");
                    Message.displayAllMessages();
                    Message.saveMessagesToFile();
                    return;
                }

                // Check if the number is valid
                String recipientCheck = msg.checkRecipientCell(recipient);
                System.out.println(recipientCheck);

                // If valid, stop asking
                if (recipientCheck.equals("Cell phone number successfully captured.")) {
                    validRecipient = true;
                }
            }

            // Save the recipient to the message
            msg.setRecipient(recipient);

            // --- Get and validate the message text ---
            String messageText = "";
            boolean validText = false;

            while (!validText) {
                System.out.print("Enter your message (max 250 characters): ");
                messageText = input.nextLine();

                // Check if the message is within the character limit
                String lengthCheck = msg.checkMessageLength(messageText);
                System.out.println(lengthCheck);

                // If valid, stop asking
                if (lengthCheck.equals("Message ready to send.")) {
                    validText = true;
                }
            }

            // Save the message text
            msg.setMessageText(messageText);

            // Build and show the message hash
            // Hash format: first2ofID:messageNumber:FIRSTWORDlastword  e.g. 43:1:HITONIGHT
            String hash = msg.createMessageHash();
            System.out.println("Message Hash: " + hash);

            // -------------------------------------------------------
            // ACTION MENU — ask the user what to do with the message
            // -------------------------------------------------------
            System.out.println();
            System.out.println("What would you like to do?");
            System.out.println("1) Send");
            System.out.println("2) Disregard");
            System.out.println("3) Store for later");
            System.out.print("Enter your choice: ");

            int option = 0;

            // Try to read the number the user typed
            try {
                option = Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                // If they did not type a number, default to disregard
                System.out.println("That is not a valid number. Defaulting to disregard.");
                option = 2;
            }

            // Show the result of their choice
            System.out.println(msg.sentMessage(option));

            // If the user chose Send (1) or Store for later (3), save the message
            if (option == 1 || option == 3) {
                msg.storeMessage();
                System.out.println("Message saved to session.");
            }

            System.out.println();
        }

        // -------------------------------------------------------
        // END OF SESSION
        // The FOR LOOP inside displayAllMessages() prints each message
        // -------------------------------------------------------
        System.out.println("==========================================");
        System.out.println("You have reached the maximum of 5 messages.");
        System.out.println("==========================================");

        // Show all messages using the for loop inside displayAllMessages()
        Message.displayAllMessages();

        // Save everything to messages.json
        Message.saveMessagesToFile();

        System.out.println("\nThank you for using QuickChat. Goodbye!");
    }
}

    
