package cn.kgc.util;

import org.apache.shiro.crypto.hash.SimpleHash;

public class ShiroUtil {
	
	private ShiroUtil() {
	}
	
	/**
	 * md5加盐加密
	 * @param pwd
	 * @param salt
	 * @return
	 */
	public static String md5(String pwd, String salt) {
		String hashAlgorithmName = "MD5";//加密方式
	    //Object crdentials = "123456";//密码原值
	    //Object salt = "manager";//盐值
	    int hashIterations = 1024;//加密1024次
	    Object result = new SimpleHash(hashAlgorithmName,pwd,salt,hashIterations);
	    return result.toString();
	}

}
