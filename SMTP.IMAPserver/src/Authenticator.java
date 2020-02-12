import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

class Authenticator {

    // Implement a Datebase connection to PostgreSQL

    // For the purpose of testing, I'm using hard coded authentication info
    private Map<String, String> signedUsers = new HashMap<String, String>();

    private Socket socket;
    private ServerSocket serverSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Authenticator() {

        // hardcoded dummydate for testing!
        signedUsers.put("user1@mailserver.com", "password1");
        signedUsers.put("user2@mailserver.com", "password2");
        signedUsers.put("user3@mailserver.com", "password3");

        run();
    }

    private void run() {

        try {
            serverSocket = new ServerSocket(50000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        while (true) {
            waitingForConnection();
            setupStreams();
            if(authenticate()){
                redirection();
            }
            closeConnections();
        }
    }

    private void waitingForConnection(){
        // puts the server in state of waiting & listening at a given port for upcoming connections from a client
        try {
            socket = serverSocket.accept();
            System.out.println("\n\t Connected to server! \n");
        } catch (IOException e) {
            e.printStackTrace();
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

    private boolean authenticate() {

    /*  
        This function does:
        - test whether the user is a signed user
        - displays INFO STATE in the terminal along the way
        - assigns the connected user's email "to connectedUserEmail" variable 
        - returns "true" if the user was successfully connected, "false" otherwise
    */

        String email;
        String password;
        int attemptsCount = 0;
        boolean connectedStatus = false;

        while (attemptsCount < 5 && !connectedStatus) {
            try {
                email = (String) input.readObject();
                password = (String) input.readObject();
                if (signedUsers.containsKey(email)) {
                    if (signedUsers.get(email).equals(password)) {
                        System.out.println("\nINFO : a user <"+email+"> has connected!\n---------------------------------\n");
                        connectedStatus = true;
                        output.writeObject(true);
                        return true;
                    } else {
                        System.out.println("INFO : wrong password!");
                    }
                } else {
                    System.out.println("INFO : Unknown user!");
                    output.writeObject(false);
                }
                output.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            attemptsCount++;
        }
        return false;
    }
    
    private void redirection() {
        // this function calls the redirection agent
        RedirectionAgent redirectionAgent = new RedirectionAgent(socket);
    }

    private void closeConnections() {
        // for cleaning up
        try {
            output.close();
            input.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print("\n\tUser disconnected\n\t---------------\n");
    }

}