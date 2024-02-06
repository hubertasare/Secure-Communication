import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class MyMessageDigest {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter a text to be hashed: ");
            String text = scanner.nextLine();

            // md5
            String md5Hash = textHashing(text, "MD5");
            System.out.println("MD5 Hash of your text is: " + md5Hash);

            // SHA-256
            String sha256Hash = textHashing(text, "SHA-256");
            System.out.println("SHA-256 Hash of your text is: " + sha256Hash);

            scanner.close();
        }
    }

    public static String textHashing(String text, String method) {
        try {
            MessageDigest digest = MessageDigest.getInstance(method);
            digest.update(text.getBytes());
            byte[] myBytes = digest.digest();

            StringBuilder result = new StringBuilder();
            for (byte b : myBytes) {
                result.append(String.format("%02x", b));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException error) {
            error.printStackTrace();
            return null;
        }
    }

}
