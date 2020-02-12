import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Agents.MailSubmissionAgent.MSA;
import Agents.MailDeliveryAgent.MDA;

class RedirectionAgent {

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public RedirectionAgent(Socket socket){
        this.socket = socket;
        run();
    }

    private void run(){
        setupStreams();

        boolean clientExit = false;
        while (!clientExit) {
            switch (choosenOperation()) {
                case 1:
                    MSA msa = new MSA(input, output);
                    break;
                case 2:
                    MDA mda = new MDA(input, output);
                    break;
                case 3:
                    clientExit = true;
                    break;
                default:
                    clientExit = true;
                    System.out.println("SYS INFO : weird option for choosen operation");
                    break;
            }
        }
    }

    // establishing the input & output streams with the client
    private void setupStreams() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.reset();
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // for receiving the operation choosen by a client
    private int choosenOperation(){
        int clientResponse = 0;

        try {
            clientResponse = (int) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return clientResponse;
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