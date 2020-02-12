import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import MailUserAgent.MUA;

class Client {

    private static Socket socket;
    private static ObjectInputStream input;
    private static ObjectOutputStream output;

    private static Scanner scanner;
    private static String connnectedUserEmail;

    public static void main(String agrs[]) {
        run();
    }

    private static void run(){
        System.out.println("\n\tAuthentication:\n\t---------------\n");

        try {
            socket = new Socket("localhost", 50000);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupStreams();
        if (authenticate()) {
            MUA mua = new MUA(socket,connnectedUserEmail);
        }
        closingConnections();
    }


    // Establishes input & output streams with the server
    private static void setupStreams() {
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // takes input from the user (email|password) and sends it to the server
    private static boolean authenticate() {

        boolean connected = false;
        int attemptsCount = 1;
    //    String[] userID = new String[2];
        String email;
        String password;

        scanner = new Scanner(System.in);

        while (!connected && attemptsCount <= 5) {

            System.out.println("\tAttempt - " + attemptsCount + " -\n\t-------------\n");

            System.out.print("Email : ");
            email = scanner.nextLine();
            System.out.print("Password : ");
            password = scanner.nextLine();

            attemptsCount++;

            try {
                output.writeObject(email);
                output.flush();
                output.writeObject(password);
                output.flush();
                connected = (boolean) input.readObject();
            } catch (IOException | ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            connnectedUserEmail = email;
        }
        return connected;
    }

    //for cleaning up
    private static void closingConnections() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}