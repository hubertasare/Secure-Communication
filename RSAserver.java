import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;

public class RSAserver {
    public static void main(String[] args) throws Exception {
        int port = 7999;
        try (ServerSocket server = new ServerSocket(port)) {
            Socket s = server.accept();

            ObjectInputStream fromClient = new ObjectInputStream(s.getInputStream());
            ObjectOutputStream toClient = new ObjectOutputStream(s.getOutputStream());

            // Generating server's pri, pub keys and sending to pub key to client
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024, new SecureRandom());
            KeyPair keyPair = keyPairGen.genKeyPair();
            RSAPublicKey ServerPubKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey ServerPriKey = (RSAPrivateKey) keyPair.getPrivate();
            toClient.writeObject(ServerPubKey);

            // get client pub key
            RSAPublicKey ClientPubkey = (RSAPublicKey) fromClient.readObject();
            Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");

            int option = fromClient.readInt();

            if (option == 1) {
                // Confidentiality: deciphering with server's pri key
                byte[] receivedEncryptedMessage = (byte[]) fromClient.readObject();
                cipher.init(Cipher.DECRYPT_MODE, ServerPriKey);
                byte[] decryptedMessage = cipher.doFinal(receivedEncryptedMessage);
                System.out.println("Confidentiality: The Message was decrypted with server's private key");
                System.out.println("Message is: " + new String(decryptedMessage).trim());
            }

            if (option == 2) {
                // authentication and integrity
                byte[] receivedEncryptedMessageI = (byte[]) fromClient.readObject();
                cipher.init(Cipher.DECRYPT_MODE, ClientPubkey);
                byte[] decryptedMessageI = cipher.doFinal(receivedEncryptedMessageI);
                System.out.println("Authetication and Integrity: The Message was decrypted with client's public key ");
                System.out.println("Message is: " + new String(decryptedMessageI).trim());
            }

            if (option == 3) {
                // Deciphering with server pri key and then with client pub key.
                byte[] receivedEncryptedMessageB = (byte[]) fromClient.readObject();
                cipher.init(Cipher.DECRYPT_MODE, ServerPriKey);
                byte[] decryptedMessageB = cipher.doFinal(receivedEncryptedMessageB);
                cipher.init(Cipher.DECRYPT_MODE, ClientPubkey);
                byte[] decryptedMessage2 = cipher.doFinal(decryptedMessageB);
                System.out.println(
                        "Both Confidentiality and integrity: The Message was first decrypted with server's private key before client public key ");
                System.out.println("Message is: " + new String(decryptedMessage2).trim());
            }
            s.close();
        }
    }

}