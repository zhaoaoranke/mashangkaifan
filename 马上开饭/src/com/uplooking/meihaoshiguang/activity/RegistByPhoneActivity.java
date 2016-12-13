package com.uplooking.meihaoshiguang.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.uplooking.meihaoshiguang.Constants;
import com.uplooking.meihaoshiguang.lib.sliding_activity.SlidingActivity;
import com.uplooking.meihaoshiguang.tools.EditTextClearUtil;
import com.uplooking.meihaoshiguang.tools.IntentUtil;
import com.uplooking.meihaoshiguang.tools.ToastUtil;
import  com.uplooking.meihaoshiguang.R;

public class RegistByPhoneActivity extends SlidingActivity implements OnClickListener{
	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = Constants.MOB_APP_KEY;
	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = Constants.MOB_APP_SECRET;

	private View mBack,mClearPhoneBtn;
	private EditText mPhoneEt;
	private EditTextClearUtil mEditTextUtil;
	private View mNextBtn;
	OnEditorActionListener editorActionListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			switch (actionId) {
			case EditorInfo.IME_ACTION_SEND:
			case EditorInfo.IME_ACTION_GO:
			case EditorInfo.IME_ACTION_DONE:
				nextStep();
				break;

			}
			return false;
		}
	};
	private ProgressDialog mProgressDialog;
	EventHandler mEventHandler = new EventHandler(){

		@Override
		public void afterEvent(final int event, final int result, final Object data) {
			//这里可以操作主线程的控件
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ToastUtil.show(getApplicationContext(), event+"");
					if(mProgressDialog != null){
						mProgressDialog.dismiss();
					}
					
					if (result == SMSSDK.RESULT_COMPLETE) {
						if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
							// 请求支持国家列表
							//							onCountryListGot((ArrayList<HashMap<String,Object>>) data);
						} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
							//请求验证码后，跳转到验证码填写页面
							afterVerificationCodeRequested();
						}
					}else{
						// 根据服务器返回的网络错误，给toast提示
						try {
							((Throwable) data).printStackTrace();
							Throwable throwable = (Throwable) data;
							
							JSONObject object = new JSONObject(throwable.getMessage());
							
							String des = object.optString("detail");
							if (!TextUtils.isEmpty(des)) {
								ToastUtil.show(getApplicationContext(), des);
								return;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						// 如果木有找到资源，默认提示
						ToastUtil.show(getApplicationContext(), getString(R.string.network_error_retry_later));
					}
				}
			});

		}
	};
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_regist_by_phone);
		initSDK();
		initViews();
		initProgressDialog();
	}
	@Override
	protected void onPause() {
		super.onPause();
		SMSSDK.unregisterEventHandler(mEventHandler);
	};
	@Override
	protected void onResume() {
		super.onResume();
		SMSSDK.registerEventHandler(mEventHandler);
	}
	private void initProgressDialog() {
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setTitle("正在连接服务器");
		mProgressDialog.setMessage("请稍后...");
		mProgressDialog.setCancelable(false);
	}
	private void initViews() {
		mBack = findViewById(R.id.back);
		mBack.setOnClickListener(this);
		
		mEditTextUtil = new EditTextClearUtil();
		mPhoneEt = (EditText) findViewById(R.id.v_regist_by_phone_EditText_phone);
		mPhoneEt.setOnEditorActionListener(editorActionListener);
		mClearPhoneBtn = findViewById(R.id.v_regist_by_phone_Button_phoneClear);
		mEditTextUtil.addEditText(mPhoneEt);
		mEditTextUtil.addClearBtn(mClearPhoneBtn);
		mNextBtn = findViewById(R.id.v_regist_by_phone_Button_next);
		mNextBtn.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v) {
		if(v == mBack){
			onBackPressed();
		}else if(v == mNextBtn){
			nextStep();
		}
	}
	private void nextStep() {
		String phoneNumber = mPhoneEt.getText().toString();
		if(TextUtils.isEmpty(phoneNumber) || phoneNumber.length() < 11){
			ToastUtil.show(this, getString(R.string.phone_num_not_right));
			return;
		}
		mProgressDialog.setTitle("正在检查手机号是否已注册");
		mProgressDialog.show();
		checkPhoneFromServer(phoneNumber);
	}

	
	/**
	 * 检查手机号是否已经注册
	 * @param phone
	 */
	private void checkPhoneFromServer(String phone){
		new HttpUtils().send(HttpMethod.GET,
				Constants.URL_CHECK_PHONE.replace("_phone", mPhoneEt.getText().toString()), httpCallBack);
	}
	RequestCallBack<Object> httpCallBack = new RequestCallBack<Object>() {

		@Override
		public void onSuccess(final ResponseInfo<Object> responseInfo) {
			String result = (String) responseInfo.result;
			
			if("0".equals(result)){
				//请求发送短信验证码
				SMSSDK.getVerificationCode("86", mPhoneEt.getText().toString());
				mProgressDialog.setTitle("正在请求短信验证码");
			}else{
				ToastUtil.show(getApplicationContext(), "手机号已经注册");
				//销毁加载等待窗口
				mProgressDialog.dismiss();
			}
			
		}
		@Override
		public void onFailure(HttpException error, String msg) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.show(getApplicationContext(), getString(R.string.network_error_retry_later));
					mProgressDialog.dismiss();
				}
			});

		}
	};

	

	/**请求验证码后，跳转到验证码填写页面*/
	private void afterVerificationCodeRequested() {
		Intent intent = new Intent(this, RegistByPhoneAddInfoActivity.class);
		intent.putExtra("phone", mPhoneEt.getText().toString());
		IntentUtil.startActivity(this, intent);
	}
	/**
	 * 初始化短信SDK
	 */
	private void initSDK(){
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
	}
}

