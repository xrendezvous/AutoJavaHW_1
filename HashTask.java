import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class HashTask {

    public static void main(String[] args) {
        try {
            String data = "someExampleDataToHash";

            String md5Hash = hashGen("MD5", data);
            String sha1Hash = hashGen("SHA-1", data);
            String sha256Hash = hashGen("SHA-256", data);

            System.out.println("MD5 Hash: " + md5Hash);
            System.out.println("SHA-1 Hash: " + sha1Hash);
            System.out.println("SHA-256 Hash: " + sha256Hash);

            byte[] random1 = secureRandGen("SHA1PRNG");
            byte[] random2 = secureRandGen("Windows-PRNG");
            byte[] random3 = secureRandGen("SHA1PRNG");

            System.out.println("SecureRandom SHA1PRNG: " + bytesToHex(random1));
            System.out.println("SecureRandom Windows-PRNG: " + bytesToHex(random2));
            System.out.println("SecureRandom SHA1PRNG (again): " + bytesToHex(random3));

            correctFunctions correctObject1 = new correctFunctions(1, "Object1");
            correctFunctions correctObject2 = new correctFunctions(1, "Object1");

            incorrectFunctions incorrectObject1 = new incorrectFunctions(1, "Object1");
            incorrectFunctions incorrectObject2 = new incorrectFunctions(2, "Object2");

            Map<correctFunctions, String> correctMap = new HashMap<>();
            correctMap.put(correctObject1, "CorrectObject1");
            correctMap.put(correctObject2, "CorrectObject2");

            Map<incorrectFunctions, String> incorrectMap = new HashMap<>();
            incorrectMap.put(incorrectObject1, "IncorrectObject1");
            incorrectMap.put(incorrectObject2, "IncorrectObject2");

            System.out.println("Correct Map Size: " + correctMap.size());
            System.out.println("Incorrect Map Size: " + incorrectMap.size());

            hashToFile(md5Hash, sha1Hash, sha256Hash, random1, random2, random3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String hashGen(String algorithm, String data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = messageDigest.digest(data.getBytes());
        return bytesToHex(hashBytes);
    }

    public static byte[] secureRandGen(String algorithm) throws NoSuchAlgorithmException {
        SecureRandom secureRandom = SecureRandom.getInstance(algorithm);
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);
        return randomBytes;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static void hashToFile(String md5, String sha1, String sha256, byte[] random1, byte[] random2, byte[] random3) throws IOException {
        try (FileWriter writer = new FileWriter("hashes.txt")) {
            writer.write("MD5 Hash: " + md5 + "\n");
            writer.write("SHA-1 Hash: " + sha1 + "\n");
            writer.write("SHA-256 Hash: " + sha256 + "\n");
            writer.write("SecureRandom SHA1PRNG: " + bytesToHex(random1) + "\n");
            writer.write("SecureRandom Windows-PRNG: " + bytesToHex(random2) + "\n");
            writer.write("SecureRandom SHA1PRNG (again): " + bytesToHex(random3) + "\n");
        }
    }
}

class correctFunctions {
    private int id;
    private String name;

    public correctFunctions(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        correctFunctions that = (correctFunctions) o;
        return id == that.id && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(id);
        result = 31 * result + name.hashCode();
        return result;
    }
}

class incorrectFunctions {
    private int id;
    private String name;

    public incorrectFunctions(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        return true;
    }

    @Override
    public int hashCode() {
        return 42;
    }
}

