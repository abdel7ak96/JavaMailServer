package MailUserAgent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/*
    this class should be able to maintain a conversation with the server
    (IMAPsender agent of a server) that establishes IMAP protocol
*/

class IMAPreceiver {

    private ObjectInputStream input;
    private ObjectOutputStream output;

    private Scanner scanner;

    private String connectedUserEmail;

    public IMAPreceiver(ObjectInputStream input, ObjectOutputStream output, String connectedUserEmail) {
        this.input = input;
        this.output = output;
        this.connectedUserEmail = connectedUserEmail;
        scanner = new Scanner(System.in);
        run();
    }

    private void run() {
        IMAPreceiverProcedure();
    }

    private void IMAPreceiverProcedure() {

        int allEmailsCount, recentEmailsCount;
        String flag;
        String[] inboxHeaders;
        String email;

        sendMessage(connectedUserEmail);                                                    // S: user1@mailserver.com
        allEmailsCount = Integer.valueOf(receiveMessage().replaceAll("[^0-9]", ""));        // R: 18 EXISTS
        flag = receiveMessage().replaceAll(".*(|)", "");                                    // R: FLAGS (\inbox)
        recentEmailsCount = Integer.valueOf(receiveMessage().replaceAll("[^0-9]", ""));     // R: 2 RECENT

        inboxHeaders = receiveStringArray();                                                // R: header1 - FROM <user1@mailserver.com>

        int choice = 0;
        while(true){
            displayInbox(inboxHeaders, recentEmailsCount);
            System.out.println("\n0) Exit");
            System.out.print("\n..?  ");
            choice = scanner.nextInt();
            if (choice==0) {
                break;
            }
            email = fetchEmail(choice);                                                     // S: 4
                                                                                            // R: an instance of Email
            System.out.print("\n\n"+email);
        }
    }

    private void sendMessage(String message) {
        try {
            output.writeObject(message);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String receiveMessage() {
        String returnString = "ERROR";

        try {
            returnString = (String) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return returnString;
    }

    private String[] receiveStringArray() {
        String[] returnedString;
        try {
            returnedString = (String[]) input.readObject();
            return returnedString;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return new String[1];
        }
    }

    private String fetchEmail(int choice) {
        String email = "";
        try {
            output.writeObject(String.valueOf(choice));
            output.flush();
            email = (String) input.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return email;
    }

    private void displayInbox(String[] inbox, int recent){
        System.out.print("\n\n\\inbox("+recent+") :\n--------------------\n");

        for (int i = 0; i < inbox.length; i++) {
            System.out.println("  "+(i+1)+") "+inbox[i]);
        }

    }

    private void displayEmail(Email email){
        System.out.print("\n------------------------------------------------\n");
        System.out.print("FROM : "+email.getMailFrom()+"\t   "+email.getDateAndTime()+"\n----------");
        System.out.print("HEADER : "+email.getHeader()+"\n------------");
        System.out.print("\t----------------------\n");
        System.out.print(email.getBody()+"\n\t----------------------\n");
    }
}