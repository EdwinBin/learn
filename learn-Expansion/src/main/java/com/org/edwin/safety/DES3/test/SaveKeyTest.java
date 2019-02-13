package com.org.edwin.safety.DES3.test;

import com.org.edwin.safety.DES3.SaveKey;

public class SaveKeyTest {
	
	public static void main(String[] args) {
		// 要加密的数据
		String text = "12345";
		SaveKey.sendSecret(text);
		SaveKey.receiveSecret();
	}
}
