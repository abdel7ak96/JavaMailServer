package Agents.MailTransferAgent;

/* 
    if the MTA can store an email for a reason like (the recipient is not local or reachable or no recipient is
    mentioned in the email) then that email is sent back to the sender, which is like storing it as if the sender
    was also the recipient and adding something to indicate that this is an error email

    this agent is going to be static because I don't want to lose at any instance the data it holds also I want
    that data to be same one shared between all agents
*/

import java.util.ArrayList;

import Agents.Email;

public class MTA {

    private static ArrayList<Email> storedEmails = new ArrayList<Email>();

    public static void storeEmail(Email email){
        storedEmails.add(email);
    }

    public static ArrayList<Email> retrieveEmails(String recipientAddress){
        ArrayList<Email> returnedList = new ArrayList<Email>();

        for (int i = 0; i < storedEmails.size(); i++) {
            String[] recipientListForEachEmail = storedEmails.get(i).getMailTo();
            for (int j = 0; j < recipientListForEachEmail.length; j++) {
                if(recipientListForEachEmail[j].equals(recipientAddress)){
                    returnedList.add(storedEmails.get(i));
                }
            }
        }

        return returnedList;
    }

}