package Agents.MailSubmissionAgent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MSA {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    public MSA(ObjectInputStream input, ObjectOutputStream output){
        this.input = input;
        this.output = output;
        run();
    }

    private void run(){
       SMTPreceiver smtpReceiver = new SMTPreceiver(input, output);
    }

    // for testing purposes
    private void test() {
        String message;

        try {
            message = (String) input.readObject();
            System.out.println("\n\n message : "+message);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}