import javax.crypto.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RSAclient {
    public static void main(String[] args) throws Exception {
        String message = "The quick brown fox jumps over the lazy dog.";
        String host = "localhost";
        int port = 7999;

        try {
            Socket s = new Socket(host, port);

            ObjectOutputStream toRSAserver = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream fromRSAserver = new ObjectInputStream(s.getInputStream());

            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(1024, new SecureRandom());
            KeyPair keyP = keyPairGen.genKeyPair();

            // Assigning client pub,pri keys and sending pub key to server
            RSAPublicKey ClientPubKey = (RSAPublicKey) keyP.getPublic();
            RSAPrivateKey ClientPriKey = (RSAPrivateKey) keyP.getPrivate();
            toRSAserver.writeObject(ClientPubKey);

            // getting Server pub key
            RSAPublicKey ServerPublicKey = (RSAPublicKey) fromRSAserver.readObject();
            Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");

            if (args[0].equals("1")) {
                System.out.println("Confidentiality: The Message will be encrypted using server's public Key");
                cipher.init(Cipher.ENCRYPT_MODE, ServerPublicKey);
                byte[] encryptedMessage = cipher.doFinal(message.getBytes());
                toRSAserver.writeInt(1);
                toRSAserver.writeObject(encryptedMessage);
                toRSAserver.flush();
                toRSAserver.close();
            }

            if (args[0].equals("2")) {
                System.out
                        .println(
                                "Integrity & Authentication: The Message will be encrypted using client's private key");

                cipher.init(Cipher.ENCRYPT_MODE, ClientPriKey);
                byte[] encryptedMessage = cipher.doFinal(message.getBytes());
                toRSAserver.writeInt(2);
                toRSAserver.writeObject(encryptedMessage);
                toRSAserver.flush();
                toRSAserver.close();
            }

            if (args[0].equals("3")) {

                System.out.println(
                        "Both : The Message will be first encrypted using Client's private key and then using Server's public key");
                cipher.init(Cipher.ENCRYPT_MODE, ClientPriKey);
                byte[] encryptedMessage = cipher.doFinal(message.getBytes());
                Cipher cipher2 = Cipher.getInstance("RSA/ECB/NoPadding");
                cipher2.init(Cipher.ENCRYPT_MODE, ServerPublicKey);
                byte[] encryptedMessage2 = cipher2.doFinal(encryptedMessage);
                toRSAserver.writeInt(3);
                toRSAserver.writeObject(encryptedMessage2);
                toRSAserver.flush();
                toRSAserver.close();
            }
            s.close();
        } catch (Exception e) {
            // Catching exception
            System.out.println(
                    "Please provide an option as commandline argument (1 for confidentiality or 2 for Authentication and Integrity or 3 for Both integrity and confidentialiy) E.g. java RSAclient 1");
            e.printStackTrace();
        }

    }

}