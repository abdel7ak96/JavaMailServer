package MailUserAgent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

/*
    this class should keep looping, offering the user 3 choices:
    -   send an email -> (use SMTPsender)
    -   retrieve emails -> (use IMAPreceiver)
    -   Disconnect -> ends the loop
*/

public class MUA {

    private Scanner scanner;
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    private String connectedUserEmail;

    public MUA(Socket socket, String connectedUserEmail){
        this.socket = socket;
        this.connectedUserEmail = connectedUserEmail;
        scanner = new Scanner(System.in);
        run();
    }

    private void run(){

        setupStreams();
        
        boolean logout = false;

        while(!logout){

            System.out.println("\nChoose an operation :\n-------------------\n   1 - Send an Email\n   2 - Consult your Emails\n   3 - Logout");
            int response = scanner.nextInt();

            switch (response) {
                case 1:
                    sendMail();
                    break;
                case 2:
                    retrieveMail();
                    break;
                case 3:
                    logout = true;
                    sendOperationNumber(3);
                    System.out.println("INFO : User disconnected!");
                    break;
                default:
                    // do nothing and let the loop iterate again
                    break;
            }
        }
    }

    // Establishes input & output streams with the server
    private void setupStreams() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMail(){
        sendOperationNumber(1);
        SMTPsender sender = new SMTPsender(input,output,connectedUserEmail);
    }

    private void retrieveMail(){
        sendOperationNumber(2);
        IMAPreceiver imapReceiver = new IMAPreceiver(input, output, connectedUserEmail);
    }

    private void sendOperationNumber(int number){
        try {
            output.writeObject(number);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}