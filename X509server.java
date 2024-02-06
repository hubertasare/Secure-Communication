import java.io.*;
import java.security.*;

import javax.net.ssl.KeyManagerFactory;

import java.net.ServerSocket;
import java.net.Socket;

public class X509server {
    public static void main(String[] args) {
        int port = 7999;

        try {
            // Load the keystore
            char[] keystorePass = "password".toCharArray();
            KeyStore keystore = KeyStore.getInstance("JKS");
            FileInputStream fis = new FileInputStream("server_keystore.jks");
            keystore.load(fis, keystorePass);

            // Set up key manager factory to use the keystore
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keystore, keystorePass);

            // Create a regular server socket
            ServerSocket serverSocket = new ServerSocket(port);

            // Accept connections
            System.out.println("Server started. Waiting for connections...");
            Socket socket = serverSocket.accept();
            System.out.println("Connection established.");

            // Receive and print the confidential message
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            String confidentialMessage = (String) in.readObject();
            System.out.println("Received confidential message: " + confidentialMessage);

            // Send a response
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            String response = "Server response: Message received.";
            out.writeObject(response);

            // Close the connection
            socket.close();
            serverSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
