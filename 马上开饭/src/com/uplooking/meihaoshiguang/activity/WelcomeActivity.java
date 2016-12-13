package com.uplooking.meihaoshiguang.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import  com.uplooking.meihaoshiguang.R;

public class WelcomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		
 new Handler(new Handler.Callback(){

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

 });
		
		new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				
					if (isFirstRun()) {
						// ���Ӧ���ǵ�һ�����У�����ת��"��������"
						startActivity(new Intent(WelcomeActivity.this,
								GuideActivity.class));
					} else {
						// ���Ӧ�ò��ǵ�һ�����У�����ת��"������"
						startActivity(new Intent(WelcomeActivity.this,
								MainActivity.class));
					}
					WelcomeActivity.this.finish();// ���Լ��رյ�
					// ����Activity����ת����
					WelcomeActivity.this.overridePendingTransition(R.anim.activity_enter,
							R.anim.activity_exit);

				
				return false;
			}
		}).sendEmptyMessageDelayed(0, 2500);
		
	
		
		
	}

	/**
	 * �ж�Ӧ���Ƿ��ǵ�һ������
	 * 
	 * @return
	 */
	private boolean isFirstRun() {
	
		// ����ģʽ
		SharedPreferences preferences = getSharedPreferences("app",
				MODE_PRIVATE);
		boolean isFirst = preferences.getBoolean("first_run", true);
		if (isFirst) {
			preferences.edit().putBoolean("first_run", false).commit();
		}
		return isFirst;
	}
}
