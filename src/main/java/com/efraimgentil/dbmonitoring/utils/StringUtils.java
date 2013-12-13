package com.efraimgentil.dbmonitoring.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {
	
	
	public String md5(String string){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] digest = md.digest( string.getBytes() );
			BigInteger bigInt = new BigInteger(1,digest);
			return bigInt.toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return string;
	}
	
}
