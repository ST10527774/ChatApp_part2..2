package com.mycompany.chatapp;

import java.util.Random;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.FileWriter;
import java.io.IOException;

public class Message {

    
    private String messageID;     // Variable to store the unique message ID  
    private int messageNumber;    // Variable to store the message number 
    private String recipient;     // Variable to store the recipient's phone number 
    private String messageText;   // Variable to store the actual message text  
    private String messageHash;   // Variable to store the generated message hash

    
    
    private static ArrayList<JSONObject> messageStore = new ArrayList<>();

    // This runs automatically whenever a new message object is created 
    
    public Message(int messageNumber) {

        // Store the message number passed into the constructor 
        this.messageNumber = messageNumber;

        // Create a random object to generate random numbers 
        Random random = new Random();
        // The number starts from 10000000 and adds a random value
        this.messageID = String.valueOf(
            1000000000L + (long)(random.nextDouble() * 9000000000L)
        );
    }

    // Method used to save the recipient's phone number
    public void setRecipient(String recipient) {
        // Assign the recipient value to the class variable 
        this.recipient = recipient;
    }

    // Method used to save the message text
    public void setMessageText(String messageText) {
        this.messageText = messageText; v cngf
    }

    // Return the message ID so other classes can access it
    public String getMessageID() {
        return messageID;
    }

    // Check whether the message ID is 10 digits or less
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    // Validate the recipient cell phone number 
    // The number must:
    // start with +27
    // Be 13 characters or less
    public String checkRecipientCell(String recipient) {
        if (recipient.startsWith("+27") && recipient.length() <= 13) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Check whether the message length is within the allowed limit
    // Maxium allowed length = 250 characters 
    public String checkMessageLength(String message) {
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            // Work out how many characters over the limit the message is
            int over = message.length() - 250;
            return "Message exceeds 250 characters by " + over + "; please reduce the size.";
        }
    }

    
    public String createMessageHash() {

        // Get the first 2 characters of the message ID
        String idPart = messageID.substring(0, 2);

        // Split the message text into individual words
        String[] words = messageText.split(" ");

        // Get the first and last word
        String firstWord = words[0];
        String lastWord  = words[words.length - 1];

        // Join everything together with colons
        messageHash = idPart + ":" + messageNumber + ":" + firstWord + lastWord;

        // Return the hash in uppercase
        return messageHash.toUpperCase();
    }

    // Return a result message depending on the option selected by the user
    public String sentMessage(int option) { 
        // Check the selected options
        switch (option) {
            case 1:
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                return "Message successfully stored.";
                // Any invalid option 
            default:
                return "Invalid option.";
        }
    }

    // Display the full details of the current message
    public String printMessages() {
        return "Message ID  : " + messageID
             + "\nMessage Hash: " + messageHash
             + "\nRecipient   : " + recipient
             + "\nMessage     : " + messageText;
    }

    // Return the message number (loop counter)
    public int returnTotalMessages() {
        return messageNumber;
    }

    // Save this message to the shared list as a JSON object
    public void storeMessage() {
        JSONObject obj = new JSONObject();
        obj.put("messageID",     messageID);
        obj.put("messageNumber", messageNumber);
        obj.put("recipient",     recipient);
        obj.put("messageText",   messageText);
        obj.put("messageHash",   messageHash != null ? messageHash : "");
        messageStore.add(obj);
    }

    // Show all messages that are stored in the list
    // This uses a FOR LOOP to go through each message one by one
    public static void displayAllMessages() {

        // Check if there is anything to show
        if (messageStore.isEmpty()) {
            System.out.println("No messages stored.");
            return;
        }

        System.out.println("\n=== ALL STORED MESSAGES ===");

        // FOR LOOP — go through every message in the list
        for (int i = 0; i < messageStore.size(); i++) {
            JSONObject obj = messageStore.get(i);
            System.out.println("\n--- Message " + (i + 1) + " ---");
            System.out.println("Message ID  : " + obj.getString("messageID"));
            System.out.println("Message Hash: " + obj.getString("messageHash"));
            System.out.println("Recipient   : " + obj.getString("recipient"));
            System.out.println("Message     : " + obj.getString("messageText"));
        }
    }

    // Write all stored messages to a file called messages.json
    public static void saveMessagesToFile() {
        JSONArray jsonArray = new JSONArray(messageStore);
        try (FileWriter file = new FileWriter("messages.json")) {
            file.write(jsonArray.toString(4)); // 4 spaces makes the file easy to read
            System.out.println("\nAll messages saved to messages.json");
        } catch (IOException e) {
            System.out.println("Could not save messages: " + e.getMessage());
        }
    }

    // This lets the test class read the message list
    public static ArrayList<JSONObject> getMessageStore() {
        return messageStore;
    }
}