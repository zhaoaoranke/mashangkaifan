package com.uplooking.meihaoshiguang.fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.uplooking.meihaoshiguang.MyApplication;
import com.uplooking.meihaoshiguang.activity.LoginActivity;
import com.uplooking.meihaoshiguang.tools.IntentUtil;
import com.uplooking.meihaoshiguang.tools.ToastUtil;
import com.uplooking.meihaoshiguang.tools.UserPrefrence;
import com.uplooking.meihaoshiguang.tools.UserPrefrence.OnLoginStateChangeListener;
import  com.uplooking.meihaoshiguang.R;
import com.zhuoxin.slidingmenulib.SlidingMenuActivity;

public class FragmentMenu extends Fragment implements OnClickListener {
	/** 所有CheckedTextView id的数组 **/
	private int[] mCheckedTvIds;
	/** CheckedTextView的数组 **/
	private CheckedTextView[] mCheckedTvs;
	/** 对应的Fragment的Class数组 **/
	private Class<?>[] mFragmentClasses;

	private SlidingMenuActivity mSlidingMenuAty;
	private ImageView mUserImg;
	private TextView mUserNicknameText;
	OnLoginStateChangeListener OnLoginStateChangeListener = new OnLoginStateChangeListener() {

		@Override
		public void onLoginStateChange(boolean login) {
			if (login) {
				mUserNicknameText.setText(UserPrefrence
						.getNickName(getActivity()));
				String headPhoto = UserPrefrence.getHeadPhoto(getActivity());
				if (TextUtils.isEmpty(headPhoto)) {
					mUserImg.setImageResource(R.drawable.default_user_photo);
				} else {
					// TODO
				}
			} else {
				mUserNicknameText.setText("欢迎回家");
				mUserImg.setImageResource(R.drawable.person_icon);
			}
		}
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_menu, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mSlidingMenuAty = (SlidingMenuActivity) getActivity();

		initViews();
		UserPrefrence.registOnLoginStateChangeListener(OnLoginStateChangeListener);
	}

	

	/**
	 * 初始化控件
	 */
	private void initViews() {
		
		
		mFragmentClasses = new Class<?>[] { 
				FragmentHome.class,
				FragmentCategory.class, 
				FragmentPhoto.class,
				FragmentSetup.class };
		mCheckedTvIds = new int[] { R.id.menu_home, R.id.menu_menu,
				R.id.menu_photo, R.id.menu_setup };
		mCheckedTvs = new CheckedTextView[mCheckedTvIds.length];

		for (int i = 0; i < mCheckedTvIds.length; i++) {
			mCheckedTvs[i] = (CheckedTextView) getView().findViewById(
					mCheckedTvIds[i]);
			mCheckedTvs[i].setOnClickListener(this);
		}

		mUserImg = (ImageView) getView().findViewById(R.id.menu_user_img);
		mUserImg.setOnClickListener(this);

		mUserNicknameText = (TextView) getView().findViewById(
				R.id.menu_user_nickname_text);

		if (UserPrefrence.isLogin(getActivity())) {
			mUserNicknameText.setText(UserPrefrence.getNickName(getActivity()));
			String headPhoto = UserPrefrence.getHeadPhoto(getActivity());
			if (TextUtils.isEmpty(headPhoto)) {
				mUserImg.setImageResource(R.drawable.default_user_photo);
			} 
		}
		
		
		
	}

	@Override
	public void onClick(View v) {
		if (v == mUserImg) {
			// 这里应该要判断当前是否已经登入
			if (UserPrefrence.isLogin(getActivity())) {
				ToastUtil.show(getActivity(), "跳到个人信息界面");
			} else {
				// MyApplication app = (MyApplication) getActivity()
				// .getApplication();
				// app.cls = getActivity().getClass();
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				IntentUtil.startActivity(getActivity(), intent);
			}
		} else {
			for (int i = 0; i < mCheckedTvs.length; i++) {
				if (v == mCheckedTvs[i]) {
					mSlidingMenuAty.switchFragment(mFragmentClasses[i]);
					mCheckedTvs[i].setChecked(true);
				} else {
					mCheckedTvs[i].setChecked(false);
				}
			}
		}
	}

	/**
	 * 选择主FragmentHome
	 */
	public void checkHome() {
		onClick(mCheckedTvs[0]);
	}
}



