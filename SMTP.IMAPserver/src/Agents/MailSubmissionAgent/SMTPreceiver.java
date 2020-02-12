package Agents.MailSubmissionAgent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Agents.Email;
import Agents.MailTransferAgent.MTA;

public class SMTPreceiver {

    private ObjectInputStream input;
    private ObjectOutputStream output;

    public SMTPreceiver(ObjectInputStream input, ObjectOutputStream output) {
        this.input = input;
        this.output = output;
        run();
    }

    private void run() {
        Email email = SMTPreceiverProcedure();
        MTA.storeEmail(email);
        System.out.println("\n---------------\n| Email Stored |\n----------------\n");
    }

    
    private Email SMTPreceiverProcedure(){
        Email receivedEmail = new Email();

        receivedEmail.setMailFrom(receiveMessage().replaceAll(".*<|>","")); // R: MAIL FROM:<user1@mailserver.com>
        sendMessage("250 OK");                                              // S: 250 OK

        receivedEmail.setDateAndTime();                                     // saves the reception time
        receivedEmail.setRecentStatus(true);                                      // sets the status of the email as recent
        
        ArrayList<String> mailList = new ArrayList<String>();
        String tmp = receiveMessage();                                      // R: RCPT TO:<user2@mailserver.com>
        while(tmp.matches("RCPT TO:<.*>")){                                 // todo : fix white space issue
            if (tmp.matches("RCPT TO:<.*@mailserver.com>")) {
                mailList.add(tmp.replaceAll(".*<|>",""));
                sendMessage("250 OK");                                      // S: 250 OK
            }
            else {
                sendMessage("550 No such user here");
            }
            tmp = receiveMessage();                                         // R: RCPT TO:<user2@mailserver.com> || HEADER:<Header test>
        }
        
        receivedEmail.setMailTo(mailList.toArray(new String[0]));

        if (tmp.matches("HEADER:<.*>")) {
            receivedEmail.setHeader(tmp.replaceAll(".*<|>",""));            // R: HEADER:<Header test>
            sendMessage("250 OK");                                          // S: 250 OK
        }
        else {
            sendMessage("660 Not a correct Header format");
        }
        
        tmp = receiveMessage();                                             // R: DATA
        if (tmp.equals("DATA")) {
            sendMessage("345 Start mail input");                            // S: 345 Start mail input
        }
        else {
            sendMessage("770 Error at receiving DATA");
        }
        
        receivedEmail.setBody(receiveMessage());                            // R: Blah blah blah...etc. etc. etc.

        tmp = receiveMessage();                                             // R: <CRLF>.<CRLF>
        if (tmp.equals("<CRLF>.<CRLF>")) {
            sendMessage("250 OK");                                          // S: 250 OK
        }
        else {
            sendMessage("880 Error at closing the email body");
        }

        return receivedEmail;
    }

    private void emailDisplay(Email email){
        System.out.println(email.getMailFrom());

        String[] mailList = email.getMailTo();
        for(int i=0;i<mailList.length;i++){
            System.out.println(mailList[i]);
        }

        System.out.println("Time "+email.getDateAndTime());

        System.out.println(email.getHeader());

        System.out.println("Body :\n"+email.getBody());
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

        try {
            message = (String) input.readObject();
            System.out.println("\n\n message : "+message);
            output.writeObject("250 OK");
            output.flush();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}