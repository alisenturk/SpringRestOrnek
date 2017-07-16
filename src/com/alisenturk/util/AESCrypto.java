package com.alisenturk.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESCrypto {
    
    private SecretKeySpec secretKey;
    private byte[] key;
    
    public AESCrypto(String myKey) {
        setKey(myKey);
    }
    
    private void setKey(String myKey) {
        
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16); // use only first 128 bit
            secretKey = new SecretKeySpec(key, "AES");
            
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            Helper.errorLogger(getClass(), e, myKey);
        }
        
    }
    
    public String encrypt(String strToEncrypt) {
        String encryptedString = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            encryptedString = Base64.encodeBase64String(cipher
                    .doFinal(strToEncrypt.getBytes("UTF-8")));
            
        } catch (Exception e) {
            
            Helper.errorLogger(getClass(), e, strToEncrypt);
        }
        return encryptedString;
        
    }
    
    public String decrypt(String strToDecrypt) {
        String decryptedString = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedString = new String(cipher.doFinal(Base64
                    .decodeBase64(strToDecrypt)));
            
        } catch (Exception e) {
            
            Helper.errorLogger(getClass(), e, strToDecrypt);
            
        }
        return decryptedString;
    }
    
}
