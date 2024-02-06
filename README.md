# Secure-Communication-Systems
 End of course project for Introduction to Security and Privacy. This project focused on Secure Communication using Java classes.
 ---
 First is using MD5 and Sha-256 schemes and the messageDigest class in Java to hash strings passed to a program.
 To run this application: file with name myMessageDigest.java is the source code to this application. Compile the file with “javac” and run the compiled
 file with “java”. You will be prompted to enter a text to be hashed. Enter your text and press enter, the program will then return the MD5 and SHA-256
 hashes of the text you entered.
 
 Similarly, compile all the classes with “javac” and run the servers (ProtectedServer, CipherServer, RSAserver, X509server) first before the clients
 (ProtectedClient, CipherClient, RSAclient, X509client). In the case of the "ElGamal”, run the ElgamalBob class before ElgamalAlice. With the RSA
 encryption, I generate the public key within the individual classes (thus RSAclient.java and RSAsever.java). You will have to pass an option “1” or “2” or
 “3” as command line argument to the client class to choose whether “Confidentiality”, “Authentication and Integrity”, or “only Integrity” respectively.
 Example for confidentiality option, you can run the RSAclient.class as “java RSAclient 1”.
 Note: All independently generated keys and certificates are attached in the zip file. All you do is compile and run the classes.
 
