package hash;

import constant.Constants;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashGenerator {

    /**
     * A custom byte to hex converter to get the hashed value in hexadecimal
     * @param hash bytes array
     * @return hexadecimal string
     */
    public static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * Hash string using SHA-256
     * @param originalString
     * @return hashed id
     */
    public static int getHash(String originalString) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encodedHash = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));
        // convert sha-256 hash to int id
        int hashVal = 0;

        for (int l = 0; l < encodedHash.length; l++)
            hashVal = (31 * hashVal + encodedHash[l]) % constant.Constants.TOTAL_ID_SPACE;


        // todo: Consistent hash?
        // id mod (2^m), need to convert to positive
        hashVal = hashVal % Constants.MODULE;
        return (hashVal < 0) ? (hashVal + Constants.MODULE) : hashVal;
    }
}


