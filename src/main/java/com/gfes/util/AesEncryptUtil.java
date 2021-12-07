package com.gfes.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 *
 * aes加解密工具类
 *
 * @author  yinzw
 * @version 1.0.0
 *<p>
 */
public class AesEncryptUtil {

	private static final Logger logger = LoggerFactory.getLogger(AesEncryptUtil.class);

    // 算法名称/加密模式/数据填充方式
    private static final String ALGORITHMSTR = "AES/ECB/ISO10126Padding";

    /**
     *
     * <p>Method ：getKey
     * <p>Description : 生成一个128位的秘钥
     *
     * @return
     * @author  yinzw
     */
    public static String getKey() {
    	KeyGenerator kg;
    	String key = null;
		try {
			kg = KeyGenerator.getInstance("AES");
			kg.init(128);
			SecretKey sk = kg.generateKey();
			byte[] b = sk.getEncoded();
			key = Base64.getEncoder().encodeToString(b);
		} catch (NoSuchAlgorithmException e) {
			logger.error("生成秘钥失败");
			return null;
		}
    	return key;
    }
    /**
     * 加密
     * @param content 加密的字符串
     * @return
     * @throws Exception
     */
    public static String encrypt(String content, String key){
    	try {
	        KeyGenerator kgen = KeyGenerator.getInstance("AES");
	        kgen.init(128);
	        Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
	        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
	        byte[] b;
			b = cipher.doFinal(content.getBytes("utf-8"));
			return Base64.getEncoder().encodeToString(b);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }

    /**
     * 加密
     * @param content 加密的字节数组
     * @return
     * @throws Exception
     */
    public static String encrypt(byte[] content, String key){
    	try {
    		KeyGenerator kgen = KeyGenerator.getInstance("AES");
    		kgen.init(128);
    		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
    		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
    		byte[] b;
    		b = cipher.doFinal(content);
    		return Base64.getEncoder().encodeToString(b);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    /**
     * 加密
     * @param content 加密的字节数组
     * @return
     * @throws Exception
     */
    public static byte[] encryptAsByte(byte[] content, String key){
    	try {
    		KeyGenerator kgen = KeyGenerator.getInstance("AES");
    		kgen.init(128);
    		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
    		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
    		byte[] b;
    		b = cipher.doFinal(content);
    		return b;
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }

    /**
     * 解密
     * @param ciphertext  解密的字符串
     * @return
     */
    public static String decrypt(String ciphertext, String key){
        KeyGenerator kgen;
		try {
			kgen = KeyGenerator.getInstance("AES");
			kgen.init(128);
			Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
			byte[] encryptBytes = Base64.getDecoder().decode(ciphertext);
			byte[] decryptBytes = cipher.doFinal(encryptBytes);
			return new String(decryptBytes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

}
