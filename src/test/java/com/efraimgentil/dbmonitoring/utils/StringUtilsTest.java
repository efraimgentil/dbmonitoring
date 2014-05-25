package com.efraimgentil.dbmonitoring.utils;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.efraimgentil.dbmonitoring.utils.StringUtils;

import static org.testng.AssertJUnit.*;

public class StringUtilsTest {
	
	private StringUtils util;
	
	@BeforeClass
	public void initClass(){
		util = new StringUtils();
	}
	
	@Test(groups = {"success"},
		description = "Should successfully generate a MD5 string based on the passed parameter" )
	public void shouldSuccessfullyGenerateAMd5(){
		String string = "123456";
		String stringMd5 = util.md5(string);
		assertEquals("e10adc3949ba59abbe56e057f20f883e", stringMd5);
	}
	
	
}
