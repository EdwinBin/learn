package com.org.edwin.safety.DES3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import com.org.edwin.safety.DES3.variable.SecretStaticVariable;
import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

/**
 * 把生成的密钥key保存到文件 和 读取文件中保存的密钥key (发送方进行加密，接收方进行解密)
 * 
 * @author win7
 *
 */
@SuppressWarnings("restriction")
public class SaveKey {
	
	
	public static byte[] result; // 需要传输给 接收方 接收方进行解密


	/**
	 * 模拟发送方 生成秘钥，并保存秘钥 (通过其他方式把秘钥提供给接收方，邮件，网络，U盘...)
	 */
	public static void sendSecret(String text) {
		try {
			// 1.初始化key秘钥
			KeyGenerator keyGenerator = KeyGenerator.getInstance(SecretStaticVariable.Encryption);
			keyGenerator.init(new SecureRandom());
			SecretKey secretKey = keyGenerator.generateKey();
			// 2 转换key秘钥
			DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(secretKey.getEncoded());
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(SecretStaticVariable.Encryption);
			Key key = secretKeyFactory.generateSecret(deSedeKeySpec);
			// 3.对生成的密钥key进行编码保存
			String keyencode = HexBin.encode(key.getEncoded());
			// 4 写入文件保存
			File file = new File(SecretStaticVariable.key_save_path + SecretStaticVariable.key_save_name);
			// 5 文件不存在则生成文件夹及文件
	        if(!file.exists()){
	        	/**
	        	 * getParentFile 返回此抽象路径名的父目录的抽象路径名
	        	    *        直接执行file.mkdirs()会将key_save_name生成文件夹
	        	 */
	        	File parentFile = file.getParentFile();
	        	parentFile.mkdirs();//创建父级文件路径
	        }
			OutputStream outputStream = new FileOutputStream(file);
			outputStream.write(keyencode.getBytes());
			outputStream.close();
			System.out.println(keyencode + " -----> key保存成功");

			// 6.进行加密      
			Cipher cipher = Cipher.getInstance(SecretStaticVariable.Encryption_mode_Fill);
			cipher.init(Cipher.ENCRYPT_MODE, key);
			result = cipher.doFinal(text.getBytes());
			System.out.println("发送方进行加密：" + HexBin.encode(result));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 模拟接收方 读取文件中的秘钥，进行加解密
	 */
	public static void receiveSecret() {
		try {
			// 1.读取文件中的密钥
			File file = new File(SecretStaticVariable.key_save_path + SecretStaticVariable.key_save_name);
			InputStream inputStream = new FileInputStream(file);// 文件内容的字节流
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream); // 得到文件的字符流
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader); // 放入读取缓冲区
			String readd = "";
			StringBuffer stringBuffer = new StringBuffer();
			while ((readd = bufferedReader.readLine()) != null) {
				stringBuffer.append(readd);
			}
			inputStream.close();
			String keystr = stringBuffer.toString();
			System.out.println(keystr + " -----> key读取成功"); // 读取出来的key是编码之后的 要进行转码

			// 2.通过读取到的key 获取到key秘钥对象
			byte[] keybyte = HexBin.decode(keystr);
			DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(keybyte);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(SecretStaticVariable.Encryption);
			Key key = secretKeyFactory.generateSecret(deSedeKeySpec); // 获取到key秘钥

			// 3.进行解密
			Cipher cipher = Cipher.getInstance(SecretStaticVariable.Encryption_mode_Fill);
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = cipher.doFinal(result);
			System.out.println("接收方进行解密：" + new String(result));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
