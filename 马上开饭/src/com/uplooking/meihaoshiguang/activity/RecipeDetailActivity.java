package com.uplooking.meihaoshiguang.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.uplooking.meihaoshiguang.Constants;
import com.uplooking.meihaoshiguang.MyApplication;
import com.uplooking.meihaoshiguang.adapter.RecipeStepListAdapter;
import com.uplooking.meihaoshiguang.entity.ResponceRecipeDetail;
import com.uplooking.meihaoshiguang.entity.ResponceRecipeDetail.RecipeComment;
import com.uplooking.meihaoshiguang.entity.ResponceRecipeDetail.RecipeCommentsObject;
import com.uplooking.meihaoshiguang.handler.MaterialsHandler;
import com.uplooking.meihaoshiguang.handler.RecipeDetailCommentHandler;
import com.uplooking.meihaoshiguang.lib.sliding_activity.SlidingActivity;
import com.uplooking.meihaoshiguang.tools.BitmapHelper;
import com.uplooking.meihaoshiguang.tools.IntentUtil;
import com.uplooking.meihaoshiguang.tools.SharedPrefrenceTool;
import com.uplooking.meihaoshiguang.tools.ToastUtil;
import com.uplooking.meihaoshiguang.tools.UserPrefrence;
import com.uplooking.meihaoshiguang.view.ParallaxScrollListView;
import com.uplooking.meihaoshiguang.R;

/**
 * 菜谱详情界面
 * 
 */
public class RecipeDetailActivity extends SlidingActivity implements
		OnClickListener, OnScrollListener, OnItemClickListener {
	private View mBackBtn;
	private ParallaxScrollListView mListView;
	private HttpUtils mHttpUtils;
	private RecipeStepListAdapter mRecipeStepListAdapter;
	private BitmapUtils mBitmapUtils;
	private LayoutInflater mInflater;
	// 头部视图里的控件
	/** 头部视图里显示菜谱大图片的ImageView **/
	private ImageView mHeadBigImg;
	private TextView mHeadRecipeNameTv;
	/** 头部视图里显示菜谱介绍的TextView **/
	private TextView mHeadIntroduceTv;
	/** 头部视图里显示菜谱步骤的容器 **/
	private LinearLayout mMaterialsContainer;
	/****/
	private View mHeaderIconLayout;

	private View mIconLayout;
	private ResponceRecipeDetail recipeDetail;

	// 脚步视图中的控件
	private View mFootTrickLayout;
	private TextView mFootTrickTv;
	private View mFooterCommentLayout;

	/** 评论按钮相关 **/
	private EditText mCommentEt;
	private View mCommentLayout, mCommentCommitBtn;
	private Animation mCommentInAnim, mCommentOutAnim;
	/** 菜谱的id **/
	private int recipeId;
	private RecipeDetailCommentHandler commentHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recipe_detail);
		// 初始控件以及常用对象
		initViews();
		// 加载数据
		loadDatas();

	}

	/**
	 * 加载数据
	 */
	private void loadDatas() {
		mHttpUtils = new HttpUtils();
		recipeId = getIntent().getIntExtra("recipe_id", 0);
		// 先从SharedPreferences中根据地址获取数据
		String jsonData = SharedPrefrenceTool.getStr(getApplicationContext(),
				"recipe_details", "" + recipeId);
		// 如果获取到的数据不为空，则直接处理数据并显示；否则从网络获取数据
		if (!TextUtils.isEmpty(jsonData)) {
			// 1.本地有数据则直接转成对象
			// 2.通过查询URL_GET_BRIEF_COOK_COMMENTS接口判断是否有更新
			// 3.注意id和时间戳参数
			// 4.得到的json直接转化成评论对象，这个评论类型在ResponceRecipeDetail已经定义了

			recipeDetail = new Gson().fromJson(jsonData,
					ResponceRecipeDetail.class);
			// 连接服务器，查看是否评论有更新
			String url = Constants.URL_GET_BRIEF_COOK_COMMENTS.replace(
					"_cook_id", recipeId + "")
			// 如果没有新的评论，可以自己手动更改时间戳来获得
					.replace("_time_stamp",
							recipeDetail.getComment_time_stamp());
			mHttpUtils.send(HttpMethod.GET, url, new RequestCallBack<Object>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					loadDataFromObject(recipeDetail);
				}

				@Override
				public void onSuccess(ResponseInfo<Object> arg0) {
					String result = (String) arg0.result;
					// 检测服务器数据是否更新，不等于0则有更新
					RecipeCommentsObject comments = new Gson().fromJson(result,
							RecipeCommentsObject.class);
					if (comments.getTotalCount() != 0) {
						// 采用更新部分缓存概念
						recipeDetail.setComments(comments);
						recipeDetail.setComment_time_stamp(comments
								.getTimeStamp());
						// 将数据存入SharedPreference
						SharedPrefrenceTool.putStr(getApplicationContext(),
								"recipe_details", recipeId + "",
								new Gson().toJson(recipeDetail));
					}
					loadDataFromObject(recipeDetail);
				}
			});

		} else {
			loadDataFromNetwork();
		}

	}

	/**
	 * 从服务器获取菜谱详情
	 */
	private void loadDataFromNetwork() {
		String url = Constants.URL_RECIPE_DETAIL.replace("_id", recipeId + "");
		mHttpUtils.send(HttpMethod.GET, url, new RequestCallBack<Object>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {

			}

			@Override
			public void onSuccess(ResponseInfo<Object> arg0) {
				String result = arg0.result.toString();
				recipeDetail = new Gson().fromJson(result,
						ResponceRecipeDetail.class);

				// 将数据存入SharedPreference
				SharedPrefrenceTool.putStr(getApplicationContext(),
						"recipe_details", recipeId + "", result);
				loadDataFromObject(recipeDetail);
			}
		});
	}

	/**
	 * 将菜谱详情实体类里的数据显示在界面上
	 * 
	 * @param object
	 */
	protected void loadDataFromObject(ResponceRecipeDetail recipeDetail) {
		mListView.addHeaderView(getHeadView());
		mListView.addFooterView(getFootView());

		/** 设置头部的大图片 **/
		mBitmapUtils.display(mHeadBigImg, recipeDetail.getImg());
		// 设置菜谱名字
		mHeadRecipeNameTv.setText(recipeDetail.getName());
		// 设置菜谱的介绍(要判断有没有菜谱介绍信息)

		if (!TextUtils.isEmpty(recipeDetail.getIntroduce())) {
			mHeadIntroduceTv
					.setText(Html.fromHtml(recipeDetail.getIntroduce()));
		}

		// 设置食材
		MaterialsHandler materialsHandler = new MaterialsHandler(
				RecipeDetailActivity.this);
		materialsHandler.setDatas(mMaterialsContainer,
				recipeDetail.getMain_materials(),
				recipeDetail.getAssist_materials());
		// 多说几句
		if (!TextUtils.isEmpty(recipeDetail.getTips())) {
			mFootTrickLayout.setVisibility(View.VISIBLE);
			mFootTrickTv.setText(Html.fromHtml(recipeDetail.getTips()));
		}
		// // 处理 评论
		RecipeCommentsObject comments = recipeDetail.getComments();
		commentHandler = new RecipeDetailCommentHandler(this);
		commentHandler.setDatas(mFooterCommentLayout, comments, recipeId);
		// 获取菜谱步骤的集合数据，并填充listView
		mRecipeStepListAdapter = new RecipeStepListAdapter(
				RecipeDetailActivity.this, recipeDetail.getSteps());
		mListView.setAdapter(mRecipeStepListAdapter);

	}

	/**
	 * 获取ListView的头部视图
	 * 
	 * @return
	 */
	private View getHeadView() {
		View headView = mInflater.inflate(R.layout.v_recipe_detail_head, null);
		mHeadBigImg = (ImageView) headView
				.findViewById(R.id.v_recipe_detail_head_imageview);
		mHeadRecipeNameTv = (TextView) headView
				.findViewById(R.id.v_recipe_detail_head_recipeNameTv);
		mHeadIntroduceTv = (TextView) headView
				.findViewById(R.id.v_recipe_detail_head_introduceTv);
		mMaterialsContainer = (LinearLayout) headView
				.findViewById(R.id.v_recipe_header_detail_material_container);
		mHeaderIconLayout = headView.findViewById(R.id.v_header_hideableview);
		return headView;
	}

	/**
	 * 获取ListView的脚部视图
	 * 
	 * @return
	 */
	protected View getFootView() {
		View footView = mInflater.inflate(R.layout.v_recipe_detail_foot, null);
		mFootTrickLayout = footView
				.findViewById(R.id.recipe_detail_footer_trick_layout);
		mFootTrickTv = (TextView) footView
				.findViewById(R.id.recipe_detail_footer_trick_content);
		mFooterCommentLayout = footView
				.findViewById(R.id.v_recipe_detail_comment_layout);
		return footView;
	}

	/**
	 * 初始化控件
	 * 
	 */
	private void initViews() {
		mBitmapUtils = BitmapHelper.getBitmapUtils(getApplicationContext());

		mCommentInAnim = AnimationUtils.loadAnimation(this, R.anim.comment_in);
		mCommentOutAnim = AnimationUtils
				.loadAnimation(this, R.anim.comment_out);

		mInflater = LayoutInflater.from(this);

		mBackBtn = findViewById(R.id.back);
		mBackBtn.setOnClickListener(this);

		mListView = (ParallaxScrollListView) findViewById(R.id.listView);

		mListView.setOnScrollListener(this);
		mListView.setOnItemClickListener(this);

		mIconLayout = findViewById(R.id.recipe_detail_top_right_icon_layout);
		// include的布局
		mCommentLayout = findViewById(R.id.comment_layout);
		mCommentEt = (EditText) findViewById(R.id.comment_input);
		mCommentCommitBtn = findViewById(R.id.comment_commit);
		mCommentCommitBtn.setOnClickListener(this);
		mCommentLayout.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		if (v == mBackBtn) {
			onBackPressed();
		} else if (v == mCommentCommitBtn) {
			if (UserPrefrence.isLogin(getApplicationContext())) {
				commentAction();
				mCommentEt.getEditableText().clear();
			} else {
				ToastUtil.show(this, "此操作需要登入");
				MyApplication app = (MyApplication) getApplication();
				app.cls = this.getClass();
				Intent intent = new Intent(this, LoginActivity.class);
				IntentUtil.startActivity(this, intent);
			}
		}
	}

	/**
	 * 提交评论
	 */
	private void commentAction() {
		String commentContent = mCommentEt.getText().toString();
		if (TextUtils.isEmpty(commentContent)) {
			ToastUtil.show(getApplicationContext(),
					getString(R.string.please_input_comment_content));
		} else {
			RequestParams params = new RequestParams();
			
			
			params.addBodyParameter("user_name",
					UserPrefrence.getUserName(getApplicationContext()));
			params.addBodyParameter("cook_id", recipeId + "");
			params.addBodyParameter("content", mCommentEt.getText().toString());
			// 提交评论

			new HttpUtils().send(HttpMethod.POST,
					Constants.URL_ADD_COOK_COMMENT, params, commentCallBack);
		}
	}

	RequestCallBack<Object> commentCallBack = new RequestCallBack<Object>() {

		@Override
		public void onSuccess(ResponseInfo<Object> responseInfo) {

			String result = (String) responseInfo.result;
			Log.e("onSuccess", result);
		
			if ("1".equals(result)) {
				RecipeComment comment = new RecipeComment();
				comment.setContent(mCommentEt.getText().toString());
				comment.setCommentDate(System.currentTimeMillis() + "");
				comment.setUserHeadPhoto(UserPrefrence
						.getHeadPhoto(getApplicationContext()));
				comment.setUserNickName(UserPrefrence
						.getNickName(getApplicationContext()));
				commentHandler.addComment(comment);
				mListView.setSelection(mListView.getAdapter().getCount() + 3);
			}
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			ToastUtil.show(getApplicationContext(),
					getString(R.string.network_error_retry_later));
		}
	};

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		// Log.e("yyy", firstVisibleItem + "|" + visibleItemCount + "|"
		// + totalItemCount);
		if (firstVisibleItem == 0 && mListView.getChildAt(0) != null) {
			// getChildAt到底算不算头部和脚部，还是只算身体部分？下面的代码测试就可以知道

			// ImageView ivtest = (ImageView) mListView.getChildAt(0)
			// .findViewById(R.id.v_recipe_detail_head_imageview);
			// ivtest.setImageResource(R.drawable.abc_ic_search);

			// mListView.getChildAt(0)得到的就是添加的头部
			// 因为头部在最顶端，只要滑动下去，getTop就变为负数，所以添加负号
			// mListView.getChildCount()获取当前可见的Item
			System.gc();
			if (-mListView.getChildAt(0).getTop() >= mHeaderIconLayout.getTop()) {
				mIconLayout.setVisibility(View.VISIBLE);
				mHeaderIconLayout.setVisibility(View.INVISIBLE);
				 if (mCommentLayout.getVisibility() == View.GONE) {
				 mCommentLayout.setVisibility(View.VISIBLE);
				 mCommentLayout.startAnimation(mCommentInAnim);
				 }
			} else {
				mIconLayout.setVisibility(View.GONE);
				mHeaderIconLayout.setVisibility(View.VISIBLE);
				if (mCommentLayout.getVisibility() == View.VISIBLE) {
					mCommentLayout.setVisibility(View.GONE);
					mCommentLayout.startAnimation(mCommentOutAnim);
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (position != 0 && position != recipeDetail.getSteps().size() + 1) {
			Intent intent = new Intent(this, RecipeStepActivity.class);
			intent.putExtra("object", recipeDetail);
			intent.putExtra("position", position - 1);
			startActivity(intent);
		}
		
	}
	
}
