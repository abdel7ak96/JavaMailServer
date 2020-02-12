package Agents.MailDeliveryAgent;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MDA {

    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    public MDA(ObjectInputStream input, ObjectOutputStream output){
        this.input = input;
        this.output = output;
        run();
    }

    private void run(){
        IMAPsender imapSender = new IMAPsender(input, output);
    }

}