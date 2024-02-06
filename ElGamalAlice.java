import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

public class ElGamalAlice {
	private static BigInteger computeY(BigInteger p, BigInteger g, BigInteger d) {
		// IMPLEMENT THIS FUNCTION;
		// y = g^d mod p
		// modPow(BigInteger exponent, BigInteger m) -> Returns a BigInteger whose value
		// is (this exponent mod m).
		BigInteger y = g.modPow(d, p);
		return y;
	}

	private static BigInteger computeK(BigInteger p) {
		// IMPLEMENT THIS FUNCTION;

		// Choosing k - relatively prime to (p-1)
		SecureRandom sm = new SecureRandom();
		int numBits = 1024;
		BigInteger k = new BigInteger(numBits, sm);
		BigInteger pOne = BigInteger.valueOf(1);
		BigInteger pMinusOne = p.subtract(pOne);
		// BigInteger pMinusOne = p.subtract(BigInteger.ONE
		while (!k.gcd(pMinusOne).equals(BigInteger.ONE)) {
			k = new BigInteger(numBits, sm);
		}

		return k;
	}

	private static BigInteger computeA(BigInteger p, BigInteger g, BigInteger k) {
		// IMPLEMENT THIS FUNCTION;
		// a = g^k mod p
		BigInteger a = g.modPow(k, p);
		return a;
	}

	private static BigInteger computeB(String message, BigInteger d, BigInteger a, BigInteger k, BigInteger p) {
		// IMPLEMENT THIS FUNCTION;
		BigInteger m = new BigInteger(message.getBytes()); // converting message into bigInteger
		BigInteger pMinusOne = p.subtract(BigInteger.ONE);
		BigInteger p1 = pMinusOne;
		BigInteger x0 = BigInteger.ZERO;
		BigInteger x1 = BigInteger.ONE;
		BigInteger x2 = k;
		BigInteger z, z2, z3;

		while (!x2.equals(BigInteger.ZERO)) {
			z = p1.divide(x2);
			z2 = p1.subtract(x2.multiply(z));
			p1 = x2;
			x2 = z2;
			z3 = x0.subtract(x1.multiply(z));
			x0 = x1;
			x1 = z3;
		}

		BigInteger b = x0.multiply(m.subtract(d.multiply(a))).mod(pMinusOne); // b = ((m-da)/k) mod (p-1)

		return b;
	}

	public static void main(String[] args) throws Exception {
		String message = "The quick brown fox jumps over the lazy dog.";

		String host = "localhost";
		int port = 7999;
		Socket s = new Socket(host, port);
		ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());

		// You should consult BigInteger class in Java API documentation to find out
		// what it is.
		BigInteger y, g, p; // public key
		BigInteger d; // private key

		int mStrength = 1024; // key bit length
		SecureRandom mSecureRandom = new SecureRandom(); // a cryptographically strong pseudo-random number

		// Create a BigInterger with mStrength bit length that is highly likely to be
		// prime.
		// (The '16' determines the probability that p is prime. Refer to BigInteger
		// documentation.)
		p = new BigInteger(mStrength, 16, mSecureRandom);

		// Create a randomly generated BigInteger of length mStrength-1
		g = new BigInteger(mStrength - 1, mSecureRandom);
		d = new BigInteger(mStrength - 1, mSecureRandom);

		y = computeY(p, g, d);

		// At this point, you have both the public key and the private key. Now compute
		// the signature.

		BigInteger k = computeK(p);
		BigInteger a = computeA(p, g, k);
		BigInteger b = computeB(message, d, a, k, p);

		// send public key
		os.writeObject(y);
		os.writeObject(g);
		os.writeObject(p);

		// send message
		os.writeObject(message);

		// send signature
		os.writeObject(a);
		os.writeObject(b);

		s.close();
	}
}