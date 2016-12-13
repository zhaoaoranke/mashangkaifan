package com.uplooking.meihaoshiguang.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.widget.Toast;


import com.uplooking.meihaoshiguang.fragment.FragmentCategory;
import com.uplooking.meihaoshiguang.fragment.FragmentHome;
import com.uplooking.meihaoshiguang.fragment.FragmentMenu;

//import com.zhuoxin.slidingmenulib.SlidingMenuActivity;
import com.zhuoxin.slidingmenulib.SlidingMenuActivity;

public class MainActivity extends SlidingMenuActivity {
	private FragmentMenu fragmentMenu;
	private FragmentHome fragmentHome;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fragmentMenu = new FragmentMenu();
		fragmentHome= new FragmentHome();
	
		initFragments(fragmentMenu, fragmentHome);
	//	initFragments(new FragmentTest(), new FragmentTestMain());
//		getSlidingMenu().setMenuMinScale(1).setMainMinScale(1);
	}
	
	@Override
	public void onBackPressed() {
		if(!menuIsOpen()){//�˵��ر�
			if(getCurFragment().getClass().getName().equals(FragmentHome.class.getName())){
				//�˵��ر�--������ڴ�����Fragment
				if(exit){
					finish();
				}else{
					exit = true;
					Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
					handler.sendEmptyMessageDelayed(0, 2000);
				}
			}else{//�˵��ر�--������ڴ�������Fragment
				if(getCurFragment().getClass().getName().equals(FragmentCategory.class.getName())){
					//������ڲ��׷����Fragmentʱ����Ҫ�ж��Ƿ���չ����
					FragmentCategory fragmentCategory = (FragmentCategory) getCurFragment();
					if(fragmentCategory.isExpand()){
						fragmentCategory.collapse();
					}else{
						openMenu();
					}
				}else{
					openMenu();
				}
			}
		}else{
			fragmentMenu.checkHome(); //�л���FragmentHome
		}
	}
	private boolean exit;
	Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			exit = false;
			return false;
		}
	});
	
	
	
}
