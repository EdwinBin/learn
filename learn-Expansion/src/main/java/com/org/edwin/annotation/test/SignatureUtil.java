package com.org.edwin.annotation.test;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.codec.Hex;


public class SignatureUtil {
	
	public static String md5(String content) throws Exception{
		return DigestUtils.md5Hex(content);
	}
	
	/**
	 * 私钥加密
	 * @param params
	 * @param consumerPrivateKey
	 * @return
	 * @throws Exception
	 */
	public static String getSign(Map<String, String> params, String consumerPrivateKey) throws Exception{
		Set<String> ketSet = params.keySet();
		//使用treeset排序
		TreeSet<String> sortSet = new TreeSet<String>();
		sortSet.addAll(ketSet);
		String keyvalueStr = "";
		Iterator<String> it = sortSet.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = params.get(key);
			keyvalueStr += key + value;
		}
		
		PrivateKey privateKey = AsymmetricalUtil.string2PrivateKey(consumerPrivateKey);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initSign(privateKey);
		signature.update(keyvalueStr.getBytes());
		
		return Hex.encodeToString(signature.sign());
	}

	/**
	 * 公钥验签
	 * @param params
	 * @param sign
	 * @param consumerPublicKey
	 * @return
	 * @throws Exception
	 */
	public static boolean validate(Map<String, Object> params, String sign, String consumerPublicKey) throws Exception{
		Set<String> ketSet = params.keySet();
		//使用treeset排序
		TreeSet<String> sortSet = new TreeSet<String>();
		sortSet.addAll(ketSet);
		String keyvalueStr = "";
		Iterator<String> it = sortSet.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String[] values = (String[]) params.get(key);
			keyvalueStr += key + values[0];
		}
		
		PublicKey publicKey = AsymmetricalUtil.string2PublicKey(consumerPublicKey);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initVerify(publicKey);
		signature.update(keyvalueStr.getBytes());
		
		return signature.verify(Hex.decode(sign));
	}
	
	/**
	 * 公钥加密
	 * @param params
	 * @param publicKeyStr
	 * @return
	 * @throws Exception
	 */
	public static String signature(Map<String, Object> params, String publicKeyStr) throws Exception{
		Set<String> ketSet = params.keySet();
		//使用treeset排序
		TreeSet<String> sortSet = new TreeSet<String>();
		sortSet.addAll(ketSet);
		String keyvalueStr = "";
		Iterator<String> it = sortSet.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = (String) params.get(key);
			keyvalueStr += key + value;
		}
		String hexStr = md5(keyvalueStr);
		
		PublicKey publicKey = AsymmetricalUtil.string2PublicKey(publicKeyStr);
		byte[] encryptBytes = AsymmetricalUtil.publicEncrypt(hexStr.getBytes(), publicKey);
		
		return Hex.encodeToString(encryptBytes);
	}
	
	/**
	 * 私钥验签
	 * @param params
	 * @param sign
	 * @param consumerPublicKey
	 * @return
	 * @throws Exception
	 */
	public static boolean checkSignature(Map<String, Object> params, String sign, String privateKeyStr) throws Exception{
		Set<String> ketSet = params.keySet();
		//使用treeset排序
		TreeSet<String> sortSet = new TreeSet<String>();
		sortSet.addAll(ketSet);
		String keyvalueStr = "";
		Iterator<String> it = sortSet.iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			Object object = params.get(key);
			
			String value = "";
			if(object instanceof String[]){
				String[] values = (String[]) object;
				value = values[0];
			}else if(object instanceof String){
				value = (String) object;
			}
			keyvalueStr += key + value;
		}
		String hexStr = md5(keyvalueStr);
		
		PrivateKey privateKey = AsymmetricalUtil.string2PrivateKey(privateKeyStr);
		byte[] decryptBytes = AsymmetricalUtil.privateDecrypt(Hex.decode(sign), privateKey);
		String decryptSign = new String(decryptBytes);
		
		if(hexStr.equals(decryptSign)){
			return true;
		}else{
			return false;
		}
	}
}
