package com.uplooking.meihaoshiguang.tools;

import java.util.Calendar;
/**
 * ʱ��ת���Ĺ�����
 * ��һ��long�͵�ʱ��ת�ɣ�
 * �ո� ��1�������ڣ�
 * 3����ǰ��60�������ڣ�
 * ���� 12:44 
 * ���� 18:50
 * ǰ�� 20:11
 * 7��20�� 13:22
 * 2014��2��1��
 * @author Administrator
 *
 */
public class TimeFormater {
	
	public static String formatTime(String dateTime){
		long time = Long.parseLong(dateTime);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(time);
		
		Calendar curCalendar = Calendar.getInstance();
		curCalendar.setTimeInMillis(System.currentTimeMillis());
		
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int min = calendar.get(Calendar.MINUTE);
		
		long curTime = System.currentTimeMillis();
		long offset = curTime - time;//���۵�ʱ������ڵ�ʱ�����ĺ�����
		if(offset < 60 * 1000){
			return "�ո�";
		}
		if(offset < 60 * 60 * 1000){
			return offset/60000+"����ǰ";
		}
		if(offset < 24*60*60*1000 && calendar.get(Calendar.DAY_OF_YEAR) == curCalendar.get(Calendar.DAY_OF_YEAR)){
			return "����" + hour + ":" + min;
		}
		if(offset < 2*24*60*60*1000 && calendar.get(Calendar.DAY_OF_YEAR) == -1+curCalendar.get(Calendar.DAY_OF_YEAR)){
			return "����" + hour + ":" + min;
		}
		if(offset < 3*24*60*60*1000 && calendar.get(Calendar.DAY_OF_YEAR) == -2+curCalendar.get(Calendar.DAY_OF_YEAR)){
			return "ǰ��" + hour + ":" + min;
		}
		if(calendar.get(Calendar.YEAR) == curCalendar.get(Calendar.YEAR)){
			return month + "��" + day + "�� " + hour + ":" + min;
		}
		return year + "��" + month + "��" + day + "�� ";
	}
}
