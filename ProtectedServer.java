import java.io.*;
import java.net.*;
import java.security.*;

public class ProtectedServer {
	public boolean authenticate(InputStream inStream) throws IOException, NoSuchAlgorithmException {
		DataInputStream in = new DataInputStream(inStream);

		// IMPLEMENT THIS FUNCTION.
		String user = in.readUTF();
		long t1 = in.readLong();
		double q1 = in.readDouble();

		int length = in.readInt();
		byte[] receivedDigest = new byte[length];
		in.readFully(receivedDigest);

		String password = lookupPassword(user);
		byte[] expectedDigest = Protection.makeDigest(user, password, t1, q1);

		return MessageDigest.isEqual(receivedDigest, expectedDigest);
	}

	protected String lookupPassword(String user) {
		return "abc123";
	}

	public static void main(String[] args) throws Exception {
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();

		ProtectedServer server = new ProtectedServer();

		if (server.authenticate(client.getInputStream()))
			System.out.println("Client logged in.");
		else
			System.out.println("Client failed to log in.");

		s.close();
	}
}