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
	// ��д�Ӷ���SDKӦ�ú�̨ע��õ���APPKEY
	private static String APPKEY = Constants.MOB_APP_KEY;
	// ��д�Ӷ���SDKӦ�ú�̨ע��õ���APPSECRET
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
			//������Բ������̵߳Ŀؼ�
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ToastUtil.show(getApplicationContext(), event+"");
					if(mProgressDialog != null){
						mProgressDialog.dismiss();
					}
					
					if (result == SMSSDK.RESULT_COMPLETE) {
						if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
							// ����֧�ֹ����б�
							//							onCountryListGot((ArrayList<HashMap<String,Object>>) data);
						} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
							//������֤�����ת����֤����дҳ��
							afterVerificationCodeRequested();
						}
					}else{
						// ���ݷ��������ص�������󣬸�toast��ʾ
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
						// ���ľ���ҵ���Դ��Ĭ����ʾ
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
		mProgressDialog.setTitle("�������ӷ�����");
		mProgressDialog.setMessage("���Ժ�...");
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
		mProgressDialog.setTitle("���ڼ���ֻ����Ƿ���ע��");
		mProgressDialog.show();
		checkPhoneFromServer(phoneNumber);
	}

	
	/**
	 * ����ֻ����Ƿ��Ѿ�ע��
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
				//�����Ͷ�����֤��
				SMSSDK.getVerificationCode("86", mPhoneEt.getText().toString());
				mProgressDialog.setTitle("�������������֤��");
			}else{
				ToastUtil.show(getApplicationContext(), "�ֻ����Ѿ�ע��");
				//���ټ��صȴ�����
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

	

	/**������֤�����ת����֤����дҳ��*/
	private void afterVerificationCodeRequested() {
		Intent intent = new Intent(this, RegistByPhoneAddInfoActivity.class);
		intent.putExtra("phone", mPhoneEt.getText().toString());
		IntentUtil.startActivity(this, intent);
	}
	/**
	 * ��ʼ������SDK
	 */
	private void initSDK(){
		SMSSDK.initSDK(this, APPKEY, APPSECRET);
	}
}

