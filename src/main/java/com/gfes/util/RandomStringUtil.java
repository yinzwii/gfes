package com.gfes.util;

import java.util.Random;

/**
 *
 * <p>Class       : com.neuqsoft.zx.common.utils.code.RandomStringUtil
 * <p>Descdription: 随机字符串生成工具类
 *
 * @author  yinzw
 * @version 1.0.0
 *<p>
 */
public class RandomStringUtil {

	/**
	 *
	 * <p>Method ：randomLetterString
	 * <p>Description : 生成随机小写字母组成的字符串
	 *
	 * @param len 随机串长度
	 * @return
	 * @author  yinzw
	 */
	public static final String randomLetterString(int len) {
		if(len <= 0) {
			return "";
		}
		StringBuilder str = new StringBuilder();
		Random r = new Random();
		for(int i = 0; i < len; i++) {
			str.append((char)(r.nextInt(26) + 97));
		}
		return str.toString();
	}

}
