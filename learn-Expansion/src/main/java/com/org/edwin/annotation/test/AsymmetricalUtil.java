package com.org.edwin.annotation.test;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.codec.Hex;


public class AsymmetricalUtil {

	public static final String ALGORITHM = "RSA";
	
	/**
	 * Base64 encode
	 * */
	public static String base64Encode(byte[] bytes){
		return Base64.encodeBase64String(bytes);
	}
	
	/**
	 * Base64 decode
	 * @throws UnsupportedEncodingException 
	 * */
	public static byte[] base64Decode(String data) throws UnsupportedEncodingException{
		return Base64.decodeBase64(data.getBytes());
	}
	
	/**
	 * 获取密钥对
	 * @return
	 * @throws Exception
	 */
	public static KeyPair getKeyPair() throws Exception{
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
		keyPairGenerator.initialize(512);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}
	
	/**
	 * 获取公钥
	 * @param keyPair
	 * @return
	 */
	public static String getPublicKey(KeyPair keyPair){
		PublicKey publicKey = keyPair.getPublic();
		byte[] bytes = publicKey.getEncoded();
		return base64Encode(bytes);
	}
	
	/**
	 * 获取私钥
	 * @param keyPair
	 * @return
	 */
	public static String getPrivateKey(KeyPair keyPair){
		PrivateKey privateKey = keyPair.getPrivate();
		byte[] bytes = privateKey.getEncoded();
		return base64Encode(bytes);
	}
	
	/**
	 * 公钥字符串转公钥对象
	 * @param pubStr
	 * @return
	 * @throws Exception
	 */
	public static PublicKey string2PublicKey(String pubStr) throws Exception{
		byte[] keyBytes = base64Decode(pubStr);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}
	
	/**
	 * 私钥字符串转私钥对象
	 * @param pubStr
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey string2PrivateKey(String priStr) throws Exception{
		byte[] keyBytes = base64Decode(priStr);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}
	
	/**
	 * 公钥加密
	 * @param content
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] bytes = cipher.doFinal(content);
		return bytes;
	}
	
	/**
	 * 私钥解密
	 * @param content
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception{
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] bytes = cipher.doFinal(content);
		return bytes;
	}
	
	public static void main(String[] args) throws Exception {
		/*KeyPair keyPair = getKeyPair();
		String privateKey = getPrivateKey(keyPair);
		System.out.println(privateKey);
		String publicKey = getPublicKey(keyPair);
		System.out.println(publicKey);*/
		
		String sign = testEncrypt();
		
		testDecrypt(sign);
	}
	
	public static Map<String, Object> getParameterMap(){
		String username = "700@qq.com";
		String password = "a123456";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("password", password);
		return params;
	}
	
	public static String testEncrypt() throws Exception{
		String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAILho76AqLkeilrjmOUCKhXQAe9Ul4QzfiS/y0HXmdx64mPtvukXi++dJGTWuIMxwlXR4+0ynb1yPRX+hV10yAkCAwEAAQ==";
		String sign = SignatureUtil.signature(getParameterMap(), publicKey);
		System.out.println("sign:" + sign);
		
		return sign;
	}
	
	public static void testDecrypt(String sign) throws Exception{
		String privateKey = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAguGjvoCouR6KWuOY5QIqFdAB71SXhDN+JL/LQdeZ3HriY+2+6ReL750kZNa4gzHCVdHj7TKdvXI9Ff6FXXTICQIDAQABAkAzsaV4D98URvDEl1KDWUiAFwk8mz/BEomHQE2qstptj3cU5OOwkiA0Iz8T3lS4tZs2Dv5DZF1fz5V9lP7YfzXFAiEAt16Eof4zSY32mC1QhwKO57waenQQtGO74pwYdnsPc48CIQC2uOcTJtOEl3oSGu2vcKac5AwByycvZhrSaMjKKhte5wIgd6g4CLWmpiL7fcZkPBXRRysaZDoFBJHoczHFTNt088UCIDoROsX4UcHSTaXRTvWxDBHR2wr636INhbqWODEe/oHDAiAlVM37mqOmGzFRj5Te9+QhWNLyuU773Dcc0wY3bk8qaA==";
		boolean flag = SignatureUtil.checkSignature(getParameterMap(), sign, privateKey);
		System.out.println(flag);
	}
}
