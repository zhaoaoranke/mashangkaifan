package com.uplooking.meihaoshiguang.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.uplooking.meihaoshiguang.Constants;
import com.uplooking.meihaoshiguang.adapter.RecipeCommenListAdapter;
import com.uplooking.meihaoshiguang.entity.ResponceRecipeDetail.RecipeCommentsObject;
import com.uplooking.meihaoshiguang.lib.sliding_activity.SlidingActivity;
import com.uplooking.meihaoshiguang.tools.LogUtil;
import com.uplooking.meihaoshiguang.tools.ToastUtil;
import com.uplooking.meihaoshiguang.view.RefreshListView;
import com.uplooking.meihaoshiguang.view.RefreshListView.OnListener;
import  com.uplooking.meihaoshiguang.R;

public class RecipeCommentActivity extends SlidingActivity implements OnClickListener{
	private int mCurPage;
	private int recipeId;
	private View mBack;
	private RefreshListView mListView;
	private HttpUtils mHttpUtils;
	private RecipeCommenListAdapter mListAdapter;
	String  s=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_comment);
		recipeId = getIntent().getIntExtra("recipe_id", 0);
		mHttpUtils = new HttpUtils();
		initViews();
	}
	private void initViews() {
		mBack = findViewById(R.id.back);
		mBack.setOnClickListener(this);
		
		mListView = (RefreshListView) findViewById(R.id.listView);
		mListView.setOnListener(new OnListener() {
			
			@Override
			public void onRetry() {
				mCurPage--;
				loadNextPage();
			}
			
			@Override
			public void onLoadNextPage() {
				loadNextPage();
			}
		});
	}
	private void loadNextPage() {
		mCurPage++;
		mHttpUtils.send(HttpMethod.GET, getUrl(), httpCallBack);
	}
	RequestCallBack<Object> httpCallBack = new RequestCallBack<Object>() {
		
		@Override
		public void onSuccess(ResponseInfo<Object> arg0) {
			//ToastUtil.show(getApplicationContext(), "success");
			
			String json = arg0.result.toString();
			RecipeCommentsObject object = new Gson().fromJson(json, RecipeCommentsObject.class);
			if(mCurPage == 1){
				mListAdapter = new RecipeCommenListAdapter(getApplicationContext(), object.getList());
				mListView.setAdapter(mListAdapter);
			}else{
				mListAdapter.addDatas(object.getList());
				if(object.getTotalCount() == mListAdapter.getCount()){
					mListView.loadEnd();
				}else{
					mListView.loadSuccess();
				}
			}
		}
		
		@Override
		public void onFailure(HttpException arg0, String arg1) {
			ToastUtil.show(RecipeCommentActivity.this.getApplicationContext(), "failed");
			if(mCurPage == 1){
				
			}else{
				mListView.loadFailed();
			}
		}
	};
	private String getUrl(){
		return Constants.URL_GET_RECIPE_ALL_COMMENTS.replace("_cook_id", recipeId+"")
				.replace("_page", ""+mCurPage);
	}
	@Override
	public void onClick(View v) {
		if(v == mBack){
			onBackPressed();
		}
	}
}
