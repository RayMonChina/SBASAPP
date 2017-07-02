package com.ideal.zsyy;

import com.ideal.zsyy.entity.PhUser;

public class Config {

	public static Double hospital_lat = 31.253355;
	public static Double hospital_lng = 121.489272;
	public static String hospital_name = "上海市第一人医院";
	public static String hospital_address = "海宁路100号";

	/* 10.4.251.234:8080 180.166.162.42:8080 180.168.123.138:8439 */
//	public static String post = "172.31.20.135:8470";
	//public static String post = "180.168.123.138:8439";
//	public static String post = "10.4.251.211:8433";
	public static String post = "192.168.8.101:8005";
//	public static String post = "180.166.162.37:8420";
	public static String url = "http://" + post + "/Service/AndroidService.ashx";
//	public static String url = "http://" + post
//			+ "/PalmHospital/HenanPHJsonService";
	public static String down_url = "http://" + post + "/Service/";

	public static String hosId = "CD3FDDCD-97CA-4810-85AC-BFBDFD60F2BC";

	public static PhUser phUsers = null;

	public static String down_path = "zzyy";

	public static final String SKIN_DEFAULT = "0";
	public static final String SKIN_1 = "1";
	public static String man = "01";
	public static String woman = "02";
}
