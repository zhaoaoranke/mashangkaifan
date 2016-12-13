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
		if(!menuIsOpen()){//菜单关闭
			if(getCurFragment().getClass().getName().equals(FragmentHome.class.getName())){
				//菜单关闭--如果现在处于主Fragment
				if(exit){
					finish();
				}else{
					exit = true;
					Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
					handler.sendEmptyMessageDelayed(0, 2000);
				}
			}else{//菜单关闭--如果现在处于其它Fragment
				if(getCurFragment().getClass().getName().equals(FragmentCategory.class.getName())){
					//如果处于菜谱分类的Fragment时，需要判断是否有展开项
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
			fragmentMenu.checkHome(); //切换到FragmentHome
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
