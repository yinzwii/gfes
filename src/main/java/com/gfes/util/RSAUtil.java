package com.gfes.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA加密、解密工具类
 */
public class RSAUtil {
    private static Logger logger = LoggerFactory.getLogger(RSAUtil.class);

    // 加密算法
    public static final String KEY_ALGORITHM = "RSA";

    // RSA密钥长度必须是64 的倍数,在 512~65536之间,默认是 1024
    public static final int KEY_SIZE = 2048;
    /**
     * 公钥实例
     */
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvt4rLUwnqJdQPzHheIltnvOmBDWU929/ssI6u4uOo7FN8ax3uhOdM53HbML9cG4lVx78fhgNnsc2GeqHGl+ZGGXm5l5vNRh8tEHApilkZTESklauj6VMO7HxzbYfPp7IG7V+1aWNaDaPz1LNaERkfQNXlO/ATffkoxOkWJ5gszvEzMqsmt9WeoEaumCHcYbzlARa9TWed/GMZLzBN21XXgYvSgfdT3PSHWnuA4b7bkzbBdIKJTmjA364vMERVnSifRkZHofNGA5BcBUZ+w1asEwfagyc5/0qZqv1YPCadTcR/Uw/ZxapiCZtfSAXyrhG7NWrJ7WH8KacYRVGjBf5rQIDAQAB";
	/**
	 * 私钥实例
	 */
    public static final String PRIVATE_KEY = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC+3istTCeol1A/MeF4iW2e86YENZT3b3+ywjq7i46jsU3xrHe6E50zncdswv1wbiVXHvx+GA2exzYZ6ocaX5kYZebmXm81GHy0QcCmKWRlMRKSVq6PpUw7sfHNth8+nsgbtX7VpY1oNo/PUs1oRGR9A1eU78BN9+SjE6RYnmCzO8TMyqya31Z6gRq6YIdxhvOUBFr1NZ538YxkvME3bVdeBi9KB91Pc9Idae4DhvtuTNsF0golOaMDfri8wRFWdKJ9GRkeh80YDkFwFRn7DVqwTB9qDJzn/Spmq/Vg8Jp1NxH9TD9nFqmIJm19IBfKuEbs1asntYfwppxhFUaMF/mtAgMBAAECggEAIpHexB+M904grLF+IvTRM/7xTMXfq0k4mcnwFVPbi+5rLaCNNtiimlyzaBGREbIBdVwZY0U8AkwWkm8V9ULnWbHrNkJYZgd/Xq6iE/ZfRqyyrmqgx0erHQzHvPiFhDQCDnK6/Ds193Aab/SRW5c3lCTs0f7yl6yUbqz6EUruPMgATx2JPUxALe0fjJRZn9FK0vMyX88B6wMlOZ2HtJNBgYUVc/VxsnCll0O6EdR3f3rgX98YfW9aFztgFWnouq6nODLtMqr8MHwxzRWjsZX/LqYlpgszk246R8Y3AA0/s6kolCG+mlefI+zBLl0nS6GAXHSW9WJmQFZie3RBDC6dSQKBgQDqxf/fKiu0sSEgnF61aRkHhMYHjf9UVJNUlLS5N0c/04OuFP0My8noILlUG5GaK0SMg7Pc+x610Ib2zq7P9UbNWPXUASFVFSmxS6Mv3wGVxq8Ss/Sbgmt6kpwigytOOKy7SI1OhYBSGDwzxf2TgTWjZbHWZr/QYC3QnqpM0oUzrwKBgQDQH/FQP14QxDbAlTtFPXbhUGASNMI410zJaptvQtAq2SxjbBreJarcvzIPf7EzHBMM157saLIqF3W5MywL4XkEZcjUwnhOyWMRmleWA9rAoNxoMb2Pz7nZzDNkexMo7uyBo75+Zt3IVBiHB8nBIdpfl8Tn5+FoqA1q+Gfb6PETYwKBgQC4aOqBSsil1/RjfzBNYs5MP5ilWn5SqVRIvb3vNmJ62hO267mREYn5EatBS55k8NOZY+ImFAKdYbyBpPtfUhRZPCVsb67U/BiRsQ3vjkXadgQSttPOSvVNhFesLEgZ2x6ixWxpXyfaVDsFl+GEuZt+S2UTOgY1ETcrhVXTnhEO9QKBgQCr28SjlD/zrmLb8h7IVTHmaj16qzA6Sabxp2OEo3JL/5tE2g7xo78Mm6EV5UaIwsUEVMS5yNZeJZMjhBr6EIyi0f3dmlxICsoQzhdHHBW1/N8j9vbmWwzHyXQio/K1WV3Yy0LRE9ZNxwud7RNvtKmjSCrjqLmcdzDlRQxWLmtrqwKBgQC5VA78C+ty7aZE1dEv4poqoGWOQt+JpJ7YlORcjVDH3pbH4C7KVBIZ5WAtcrQNI3mxUIuqhup1PXdgiDGqcy6xNS0KiRnGhhaqCpM3QVD8hQfIrYNgR14NWHrlcFEPlIEa1lXHUmLWbNAZYGk8dSRAzE6L5EOUKF9nQNoSbPi7aQ==";

    /**
     * 生成密钥对,注意这里是生成密钥对 keyPair,再有密钥对获取公私钥.
     *
     * @return
     */
    public static Map<String, String> generateKeyStrings() {

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
            keyPairGenerator.initialize(KEY_SIZE);

            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            Map<String, String> keyMap = new HashMap<>();
            keyMap.put("puk", Base64Utils.encodeToString(publicKey.getEncoded()));
            keyMap.put("prk", Base64Utils.encodeToString(privateKey.getEncoded()));

            return keyMap;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;

    }

    /**
     * 还原公钥,X509 EncodeKeySpec 用于构建公钥的规范
     *
     * @param keyBytes
     * @return
     */
    public static PublicKey getPublicKey(byte[] keyBytes) {

        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);

        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);

            PublicKey publicKey = factory.generatePublic(x509EncodedKeySpec);

            return publicKey;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;

    }

    /**
     * 还原私钥, PKCS8EncodedKeySpec 用于构建私钥的规范
     *
     * @param keyBytes
     * @return
     */
    public static PrivateKey getPrivateKey(byte[] keyBytes) {

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);

        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);

            PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);

            return privateKey;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 加密
     *
     * @param key
     * @param plainText
     * @return
     */
    public static byte[] RSAEncode(Key key, byte[] plainText) {

        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(plainText);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 解密
     *
     * @param key
     * @param encodedText
     * @return
     */
    public static String RSADecode(Key key, byte[] encodedText) {

        try {
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(encodedText));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;

    }
}
