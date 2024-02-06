import java.io.*;
import java.net.*;
import java.security.*;
import javax.crypto.*;

public class CipherClient {
	public static void main(String[] args) throws Exception {
		String message = "The quick brown fox jumps over the lazy dog.";
		String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);

		// DES key generation
		KeyGenerator keyDES = KeyGenerator.getInstance("DES");
		SecureRandom random = new SecureRandom();
		keyDES.init(random);
		SecretKey secretKey = keyDES.generateKey();

		// Saving the key in a file to use it later
		String fileName = "des.txt";
		ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName));
		outputStream.writeObject(secretKey);
		outputStream.close();

		// Sending key to the server
		ObjectOutputStream socket = new ObjectOutputStream(s.getOutputStream());
		socket.writeObject(secretKey);
		socket.flush();

		Cipher desCipher = Cipher.getInstance("DES");
		desCipher.init(Cipher.ENCRYPT_MODE, secretKey); // making the encryption with the specified key
		byte[] byteDataToEncrypt = message.getBytes(); // encode the string into bytes

		// building a cipher output stream from the output stream and the desCipher
		CipherOutputStream cipherOut = new CipherOutputStream(s.getOutputStream(), desCipher);
		cipherOut.write(byteDataToEncrypt); // sending through the socket
		cipherOut.flush();
		cipherOut.close();

		s.close();
	}
}