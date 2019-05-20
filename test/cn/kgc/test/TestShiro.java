package cn.kgc.test;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;

public class TestShiro {
	
	@Test
	public void testMD5() {
		String hashAlgorithmName = "MD5";//加密方式
	    Object crdentials = "123456";//密码原值
	    Object salt = "abc123";//盐值
	    int hashIterations = 1024;//加密1024次
	    Object result = new SimpleHash(hashAlgorithmName,crdentials,salt,hashIterations);
	    System.out.println(result);
	}

}
