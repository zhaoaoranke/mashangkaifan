package com.uplooking.meihaoshiguang.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.uplooking.meihaoshiguang.Constants;
import com.uplooking.meihaoshiguang.MyApplication;
import com.uplooking.meihaoshiguang.entity.ResponceLogin;
import com.uplooking.meihaoshiguang.entity.ResponceUserInfo;
import com.uplooking.meihaoshiguang.lib.sliding_activity.SlidingActivity;
import com.uplooking.meihaoshiguang.tools.EditTextClearUtil;
import com.uplooking.meihaoshiguang.tools.IntentUtil;
import com.uplooking.meihaoshiguang.tools.MD5;
import com.uplooking.meihaoshiguang.tools.ToastUtil;
import com.uplooking.meihaoshiguang.tools.UserPrefrence;
import  com.uplooking.meihaoshiguang.R;
public class LoginActivity extends SlidingActivity implements OnClickListener{
	private View mBack;
	private EditText mUserNameEt,mPasswordEt;
	private View mUserNameClearBtn,mPasswordClearBtn;
	private View mLoginBtn;
	private View mRegistBtn;
	private HttpUtils mHttpUtils;
	private EditTextClearUtil mEditTextUtil;
	private ProgressDialog mDialog;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_login);
		mHttpUtils = new HttpUtils();
		initViews();
		initDialog();
	}
	private void initDialog() {
		mDialog = new ProgressDialog(this);
		mDialog.setTitle("正在登入");
		mDialog.setMessage("请稍后...");
	}
	private void initViews() {
		mEditTextUtil = new EditTextClearUtil();
		
		mBack = findViewById(R.id.v_login_back);
		mBack.setOnClickListener(this);
		
		mUserNameEt = (EditText) findViewById(R.id.v_login_username_et);
		mEditTextUtil.addEditText(mUserNameEt);
		mPasswordEt = (EditText) findViewById(R.id.v_login_password_et);
		mEditTextUtil.addEditText(mPasswordEt);
		//添加软键盘完成的监听
		mPasswordEt.setOnEditorActionListener(editorActionListener);
		mUserNameClearBtn = findViewById(R.id.v_login_username_clearBtn);
		mPasswordClearBtn = findViewById(R.id.v_login_password_clearBtn);
		
		mEditTextUtil.addClearBtn(mUserNameClearBtn);//mUserNameClearBtn
		mEditTextUtil.addClearBtn(mPasswordClearBtn);//mPasswordClearBtn
		mLoginBtn = findViewById(R.id.v_login_login_btn);
		mLoginBtn.setOnClickListener(this);
		//注册按钮
		mRegistBtn = findViewById(R.id.v_login_regist_by_phone);
		mRegistBtn.setOnClickListener(this);
	}
	OnEditorActionListener editorActionListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			switch (actionId) {
			case EditorInfo.IME_ACTION_SEND:
			case EditorInfo.IME_ACTION_GO:
			case EditorInfo.IME_ACTION_DONE:
				Log.e("yyy",actionId+"");
				login();
				break;

			}
			return false;
		}
	};
	@Override
	public void onClick(View v) {
		if(v == mBack){
			onBackPressed();
		}else if(v == mLoginBtn){
			login();
		}else if(v == mRegistBtn){
			Intent intent = new Intent(this, RegistByPhoneActivity.class);
			IntentUtil.startActivity(this, intent);
		}
	}

	private void login() {
		String userName = mUserNameEt.getText().toString();
		if(TextUtils.isEmpty(userName)){
			ToastUtil.show(this, "用户名不能为空！");
			mUserNameEt.requestFocus();
			return;
		}
		if(userName.contains(" ")){
			ToastUtil.show(this, "用户名格式不正确~");
			mUserNameEt.requestFocus();
			return;
		}
		String password = mPasswordEt.getText().toString();
		if(TextUtils.isEmpty(password)){
			ToastUtil.show(this, "密码不能为空");
			mPasswordEt.requestFocus();
			return;
		}
		mDialog.show();
		RequestParams params = new RequestParams();
		params.addBodyParameter("user_name", userName);
		password = MD5.encrypt(password);
		params.addBodyParameter("password", password);
		mHttpUtils.send(HttpMethod.POST, Constants.URL_LOGIN, params , callBack);
	}
	RequestCallBack<Object> callBack = new RequestCallBack<Object>() {

		@Override
		public void onSuccess(ResponseInfo<Object> responseInfo) {
			String result = (String) responseInfo.result;
			Gson gson = new Gson();
			ResponceLogin responceObject = gson.fromJson(result, ResponceLogin.class);
			switch (responceObject.getLoginState()) {
			case 0:
				UserPrefrence.putUserName(getApplicationContext(), mUserNameEt.getText().toString());
				UserPrefrence.putPassword(getApplicationContext(), mPasswordEt.getText().toString());
				ResponceUserInfo userInfo = responceObject.getUserInfo();
				UserPrefrence.putNickName(getApplicationContext(), userInfo.getNickName());
				UserPrefrence.putHeadPhoto(getApplicationContext(), userInfo.getHeadPhoto());
				UserPrefrence.setLogin(getApplicationContext(), true);
				MyApplication app = (MyApplication) getApplication();
				Intent intent = new Intent(LoginActivity.this, app.cls);
				startActivity(intent);
				UserPrefrence.putUserInfoJson(getApplicationContext(), gson.toJson(userInfo));
				break;
			case -1:
				ToastUtil.show(getApplicationContext(), "账号或密码错误");
				break;
			}
			mDialog.dismiss();
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			ToastUtil.show(getApplicationContext(), "连接服务器失败");
		}
	};
}
