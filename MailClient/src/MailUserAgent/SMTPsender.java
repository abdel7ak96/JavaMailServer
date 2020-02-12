package MailUserAgent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/*
    this class should be able to maintain a conversation with the server
    (SMTPreceiver agent of a server) that establishes SMTP protocol
*/

class SMTPsender {

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Scanner scanner;
    private String connectedUserEmail;

    public SMTPsender(ObjectInputStream input, ObjectOutputStream output, String connectedUserEmail){
        this.input = input;
        this.output = output;
        this.connectedUserEmail = connectedUserEmail;
        scanner = new Scanner(System.in);
        run();
    }

    private void run(){
        Email email = fillEmail();

        if(SMTPsenderProcedure(email)==1){
            System.out.println("\n--------------------------\n| Email sent successfully! |\n--------------------------\n");
        }
        else {
            System.out.println("\n--------------------------\n| Failed to send the email! ||\n--------------------------\n");
        }
    }

    private int SMTPsenderProcedure(Email email){

        sendMessage("MAIL FROM:<" + email.getMailFrom()+">");               // S: MAIL FROM:<user1@mailserver.com>
        if (!receiveMessage().equals("250 OK")) {                           // R: 250 OK
            System.out.println("Error at the MAIL FROM");
            return 0; // Error
        }
        
        String[] mailList = email.getMailTo();                              // S: RCPT TO:<user2@mailserver.com>
        boolean noneOfTheRecipientsIsValid = true;                          // R: 250 OK
        for (int i = 0; i < mailList.length; i++) {                         // S: RCPT TO:<noisyboy@gmail.com>
            sendMessage("RCPT TO:<"+mailList[i]+">");                       // R: 550 No such user here
            if (receiveMessage().equals("250 OK")) {
                noneOfTheRecipientsIsValid = false;                         // todo : think about the case where none of the recipient is accepted
            }
        }
        if(noneOfTheRecipientsIsValid){
            System.out.println("Error at the RCPT TO");
            return 0; // Error
        }

        sendMessage("HEADER:<"+email.getHeader()+">");                      // S: HEADER:<Header test>
        if (!receiveMessage().equals("250 OK")) {                           // R: 250 OK
            System.out.println("Error at the HEADER");
            return 0; // Error
        }

        sendMessage("DATA");                                                // S: DATA
        if (!receiveMessage().equals("345 Start mail input")) {             // R: 354 Start mail input; end with <CRLF>.<CRLF>
            System.out.println("Error at the DATA");
            return 0; // Error
        }

        sendMessage(email.getBody());                                       // S : Blah blah blah...etc. etc. etc.
        sendMessage("<CRLF>.<CRLF>");                                       // S : <CRLF>.<CRLF>
        if (!receiveMessage().equals("250 OK")) {                           // R : 250 OK
            System.out.println("Error at the BODY");
            return 0; // Error
        }
        return 1; // Success
    }

    private void emailDisplay(Email email){
        System.out.println("MAIL FROM:<" + email.getMailFrom()+">");

        String[] mailList = email.getMailTo();
        for(int i=0;i<mailList.length;i++){
            System.out.println("RCPT TO:<"+mailList[i]+">");
        }

        System.out.println("Body :\n"+email.getBody());
    }


    private Email fillEmail(){
        Email email = new Email();

        System.out.println("\n\tEmail composing :\n\t-----------------");
        email.setMailFrom(connectedUserEmail);

        System.out.print("\nRecipient address (separated by ',' if many) : ");
        String recipientEmailsList = scanner.nextLine();
        email.setMailTo(recipientEmailsList.split(", *"));

        System.out.print("\nHeader : ");
        email.setHeader(scanner.nextLine());

        System.out.print("\nBody : ");
        email.setBody(scanner.nextLine());

        return email;
    }

    private void sendMessage(String message){
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String receiveMessage(){
        String returnString = "ERROR";

        try {
            returnString = (String) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return returnString;
    }

    // for testing purposes
    private void test() {

        
        String message;

        System.out.println("Enter the message you want to send :");
        message = scanner.nextLine();

        try {
            output.writeObject(message);
            output.flush();
            System.out.println("\nMessage sent!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}