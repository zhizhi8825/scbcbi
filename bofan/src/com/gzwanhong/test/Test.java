package com.gzwanhong.test;

import static org.apache.ibatis.jdbc.SqlBuilder.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] ids = new String[]{"a","b"};
		Object o = ids;
		List<?> idList = Arrays.asList((Object[])o);
		System.out.println(idList.get(0).getClass());
	}
	
	public static void aaa (String... strs){
		System.out.println(strs[0]);
	}

}
