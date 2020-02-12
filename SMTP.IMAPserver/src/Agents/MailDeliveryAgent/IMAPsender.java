package Agents.MailDeliveryAgent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Agents.Email;
import Agents.MailTransferAgent.MTA;

public class IMAPsender {

    private ObjectInputStream input;
    private ObjectOutputStream output;

    public IMAPsender(ObjectInputStream input, ObjectOutputStream output){
        this.input = input;
        this.output = output;
        run();
    }

    private void run(){

        ArrayList<Email> emailsList;

        String connectedUserEmail;
        connectedUserEmail = receiveMessage();                                  // R: user1@mailserver.com
        emailsList = MTA.retrieveEmails(connectedUserEmail);

        sendMessage(String.valueOf(emailsList.size())+" EXISTS");               // S: 18 EXISTS
        sendMessage("FLAGS (\\inbox)");                                         // S: "FLAGS (\inbox)"
        sendMessage(String.valueOf(recentEmailCount(emailsList))+" RECENT");    // S: 2 RECENT

        sendStringArray(headerExtractor(emailsList));                           // S: header1 - FROM <user1@mailserver.com>
                                                                                // S: header2 - FROM <user3@mailserver.com>
        
        int choice;

        do {
            choice = Integer.valueOf(receiveMessage());                         // R: 4
            System.out.print("\nchoice = "+choice);
            sendEmail(fetchEmail(emailsList, choice));                          // S: An instance of an email
        } while (choice!=-1);
        System.out.print("\n\tjust exited the while lopp\n");
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

    private int receiveInt(){
        int returnedInt = 0;
        try {
            returnedInt = input.readInt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnedInt;
    }

    private void sendStringArray(String[] arrayString){
        try {
            output.writeObject(arrayString);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int recentEmailCount(ArrayList<Email> list){
        int count=0;
        for (int i = 0; i < list.size(); i++) {
            Email email = list.get(i);
            if(email.getRecentStatus()){
                count++;
                email.setRecentStatus(false);
            }
        }
        return count;
    }

    private String[] headerExtractor(ArrayList<Email> emailList){
        int n = emailList.size();
        String[] returnedList = new String[n];
        for (int i = 0; i < n ; i++) {
            Email email = emailList.get(i);
            returnedList[i] = email.getHeader() + " - FROM <"+email.getMailFrom()+">";
        }
        return returnedList;
    }

    // todo : change the send object from String to Email...somehow!!
    private void sendEmail(Email email){
        try {
            output.writeObject(email.getBody());
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Email fetchEmail(ArrayList<Email> emailsList, int index){
        Email returnedEmail = new Email();
        if(index <= emailsList.size()){
            returnedEmail = emailsList.get(index-1);
        }
        return returnedEmail;
    }
}