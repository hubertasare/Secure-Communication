import java.io.*;
import java.security.*;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.net.Socket;

public class X509client {
    public static void main(String[] args) {
        String message = "Confidential message is The quick brown fox jumps over lazy dog!";
        String host = "localhost";
        int port = 7999;

        try {
            // Load the server's certificate
            X509Certificate serverCert = loadCertificate("server_cert.crt");

            // Verify the server's certificate
            verifyCertificate(serverCert);

            // Create a regular socket
            Socket socket = new Socket(host, port);

            // Send the confidential message
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(message);
            System.out.println("Confidential message sent to the server.");

            // Receive the server's response
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            String response = (String) in.readObject();
            System.out.println("Server response: " + response);

            // Close the connection
            socket.close();
        } catch (Exception e) {
            System.out.println("Exception occurred.");
            e.printStackTrace();
        }
    }

    private static void verifyCertificate(X509Certificate cert) throws Exception {
        try {
            // Check expiration date
            cert.checkValidity();

            // Verify the certificate's signature using the public key of the issuer
            cert.verify(cert.getPublicKey());

            // Print content of the certificate
            System.out.println("Server Certificate Details:");
            System.out.println("Subject: " + cert.getSubjectDN());
            System.out.println("Issuer: " + cert.getIssuerDN());
            System.out.println("Public Key: " + cert.getPublicKey());
            System.out.println("Not Before: " + cert.getNotBefore());
            System.out.println("Not After: " + cert.getNotAfter());
            // Additional details as needed

            // Verification successful
            System.out.println("Certificate verification successful.");
        } catch (CertificateExpiredException e) {
            System.out.println("Certificate is expired.");
            throw e;
        } catch (CertificateNotYetValidException e) {
            System.out.println("Certificate is not yet valid.");
            throw e;
        } catch (CertificateException | NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            System.out.println("Certificate verification failed.");
            throw e;
        }
    }

    private static X509Certificate loadCertificate(String fileName) {
        try (InputStream in = X509client.class.getClassLoader().getResourceAsStream(fileName)) {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) factory.generateCertificate(in);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
