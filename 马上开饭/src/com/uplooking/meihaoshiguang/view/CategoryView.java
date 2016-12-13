package com.uplooking.meihaoshiguang.view;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.uplooking.meihaoshiguang.Constants;
import com.uplooking.meihaoshiguang.R;
import com.uplooking.meihaoshiguang.activity.RecipeListActivity;
import com.uplooking.meihaoshiguang.adapter.RecipeCategoryGridViewAdapter;
import com.uplooking.meihaoshiguang.entity.ResponceCategory;
import com.uplooking.meihaoshiguang.entity.ResponceCategory.Category;
import com.uplooking.meihaoshiguang.entity.ResponceCategory.CategoryParent;
import com.uplooking.meihaoshiguang.tools.DensityUtil;
import com.uplooking.meihaoshiguang.tools.IntentUtil;


@SuppressLint("NewApi")
public class CategoryView extends ScrollView implements OnItemClickListener{

	private LinearLayout layout;
	private LayoutInflater mInflater;
	/**װ������ͼ�ļ���**/
	private List<View> mGroupViews;
	/**������ͼ�ϼ�ͷImageView�ļ���**/
	private List<ImageView> mArrowImgs;
	
	/**����Դ---��Ӧ�ӷ�������ȡ���� �������з���**/
	private ResponceCategory mResponceCategory;
	private GridView mGridView;
	private RecipeCategoryGridViewAdapter mAdapter;
	/**GridView������**/
	private static final int NUM_COLUMNS = 3;
	private float paddingDip = 12;
	private float verticalSpaceDip = 12;
	private float horizontalSpacingDip = 12;
	/**��������ScrollView��ƽ������**/
	private Scroller mScroller;
	/**������ͼ�ϼ�ͷ�Ķ���**/
	private Animation mArrowOpenAnim,mArrowCloseAnim;
	/**xUtils����е��࣬��������Bitmap���첽����**/
	private BitmapUtils mBitmapUtils;
	public CategoryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		mInflater = LayoutInflater.from(context);
		mScroller = new Scroller(context);
		mArrowOpenAnim = AnimationUtils.loadAnimation(context, R.anim.arrow_open);
		mArrowCloseAnim = AnimationUtils.loadAnimation(context, R.anim.arrow_close);
		//����BitmapUtils���󣬲�ָ��ͼƬ�����ַ�� ͼƬ�ڴ滺���С
		mBitmapUtils = new BitmapUtils(context, Constants.IMG_CACHE_PATH, 0.3f);
	}

	private void init() {
		//����һ�����������������뵽ScrollView
		layout = new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		addView(layout);

		mGridView = new GridView(getContext());
		//����GridView������
		mGridView.setNumColumns(NUM_COLUMNS);
		//����GridView��padding������item�ļ��
		int paddingPx = DensityUtil.dip2px(getContext(), paddingDip);
		mGridView.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
		mGridView.setHorizontalSpacing(DensityUtil.dip2px(getContext(), horizontalSpacingDip));
		mGridView.setVerticalSpacing(DensityUtil.dip2px(getContext(), verticalSpaceDip));
		mAdapter = new RecipeCategoryGridViewAdapter(getContext());
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
	}
	/**
	 * ��ʼ�����ݣ��������ͼ
	 * @param responceCategory
	 */
	public void initDatas(ResponceCategory responceCategory){
		mResponceCategory = responceCategory;
		List<CategoryParent> list = responceCategory.getResult();
		mGroupViews = new ArrayList<View>();
		mArrowImgs = new ArrayList<ImageView>();
		for (CategoryParent categoryParent : list) {
			View groupView = getGroupView(categoryParent);
			layout.addView(groupView);
			groupView.setOnClickListener(onClickListener);
			mGroupViews.add(groupView);
			ImageView imgArrow = (ImageView) groupView.findViewById(R.id.img_arrow);
			mArrowImgs.add(imgArrow);
		}
	}
	/**Ϊ������ͼ���õĵ���������**/
	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			for (int i = 0; i < mGroupViews.size(); i++) {
				if(v == mGroupViews.get(i)){
					//����ľ��ǵ�i��
					clickGroupItem(i);
					break;
				}
			}
		}
	};
	/**��һ��չ���ķ���λ��**/
	private int mLastExpandPos = -1;
	/**
	 * �����i������֮��Ĵ���
	 * @param i
	 */
	private void clickGroupItem(int i) {
		if(mLastExpandPos == -1){//�����ʱû��չ���ķ���
			mAdapter.resetDatas(mResponceCategory.getResult().get(i).getList());
			layout.addView(mGridView, i+1);
			mLastExpandPos = i;
			computeGridViewHeight();
			setGridViewHeight();
			//�������Զ���
			startAnimator(-mGridViewHeight, 0);
			
			//����ScrollView�Ĺ���
			if(i >= 3){
				smoothScrollTo((i - 2)*mGroupViews.get(0).getHeight());
			}
			mArrowImgs.get(i).startAnimation(mArrowOpenAnim);
			
		}else if(mLastExpandPos == i){//�����ʱ����ľ���չ�����Ǹ�����
			if(end == 0){
				startAnimator((Integer)mAnimator.getAnimatedValue(), -mGridViewHeight);
				mArrowImgs.get(i).startAnimation(mArrowCloseAnim);
			}else{
				startAnimator((Integer)mAnimator.getAnimatedValue(), 0);
				mArrowImgs.get(i).startAnimation(mArrowOpenAnim);
			}
		}else{//�����ʱ��չ���ķ��飬���ҵ��������������
			layout.removeView(mGridView);
			mArrowImgs.get(mLastExpandPos).startAnimation(mArrowCloseAnim);
			mLastExpandPos = -1;
			clickGroupItem(i);
		}
	}
	private int end;
	/**
	 * ΪGridView���ÿ��
	 */
	private void setGridViewHeight() {
		LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) mGridView.getLayoutParams();
		params.height = mGridViewHeight;
	}
	/**����GridView�ĸ߶�--��仯**/
	private int mGridViewHeight;
	/**
	 * ����GridView�ĸ߶�
	 */
	private void computeGridViewHeight(){
		int totalHeight = 0;
		//�ȼ���GridView�м���
		int lineNum = mAdapter.getCount()/NUM_COLUMNS;
		if(mAdapter.getCount()%NUM_COLUMNS != 0){
			lineNum++;
		}
		//�ټ���GridView��item�ĸ߶�
		View itemView = mAdapter.getView(0, null, mGridView);
		itemView.measure(0, 0); // ����View�Լ��Ĵ�С
		totalHeight += itemView.getMeasuredHeight()*lineNum;
		//�ټ���padding
		totalHeight += 2*DensityUtil.dip2px(getContext(), paddingDip);
		//�ټ���item��ֱ����ļ��
		totalHeight += (lineNum-1)*DensityUtil.dip2px(getContext(), verticalSpaceDip);

		mGridViewHeight = totalHeight;
	}

	/**
	 * ��ȡһ��������ͼ
	 * @param categoryParent
	 * @return
	 */
	private View getGroupView(CategoryParent categoryParent) {
		View view = mInflater.inflate(R.layout.item_recipe_category_parent, null);
		TextView nameTv = (TextView) view.findViewById(R.id.item_recipe_category_group_nameTv);
		nameTv.setText(categoryParent.getName());
		ImageView imgView = (ImageView) view.findViewById(R.id.img);
		mBitmapUtils.display(imgView, categoryParent.getImg());
		return view;
	}

	private ValueAnimator mAnimator;
	/**
	 * �������Զ���
	 * @param start
	 * @param end
	 */
	private void startAnimator(int start,int end){
		if(mAnimator == null){
			//���ִ�����ʽ
			//			mAnimator = new ValueAnimator();
			//			mAnimator.setIntValues(start,end);
			mAnimator = ValueAnimator.ofInt(start,end);
			mAnimator.addUpdateListener(updateListener);
			mAnimator.addListener(animatorListener);
			mAnimator.setDuration(500);//���ó���ʱ��
		}else{
			mAnimator.setIntValues(start,end);
		}
		//�������Զ���
		mAnimator.start();
		this.end = end;
	}
	AnimatorListener animatorListener = new AnimatorListener() {

		@Override
		public void onAnimationStart(Animator animation) {

		}
		@Override
		public void onAnimationRepeat(Animator animation) {

		}
		@Override
		public void onAnimationEnd(Animator animation) {
			//�ж� ����������������Զ�������
			if((Integer)mAnimator.getAnimatedValue() == -mGridViewHeight){
				layout.removeView(mGridView);
				mLastExpandPos = -1;
			}
		}
		@Override
		public void onAnimationCancel(Animator animation) {
		}
	};
	AnimatorUpdateListener updateListener = new AnimatorUpdateListener() {
		
		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			//�����ʵʱ��ȡ��ValueAnimator��ֵ�ı仯
			Integer value = (Integer) animation.getAnimatedValue();
			LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) mGridView.getLayoutParams();
			params.bottomMargin = value;
			mGridView.setLayoutParams(params);
		}
	};
	/**
	 * ƽ���Ĺ���������
	 * @param y
	 */
	private void smoothScrollTo(int y){
		int startY = getScrollY();
		int dy = y - startY;
		mScroller.startScroll(0, startY, 0, dy, 600);
		invalidate();//Ϊ�˴���computeScroll()
	}
	@Override
	public void computeScroll() {
		super.computeScroll();
		if(!mScroller.isFinished()){
			if(mScroller.computeScrollOffset()){
				scrollTo(0, mScroller.getCurrY());
				invalidate();
			}
		}
	}
	private boolean itemCanClick = true;
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(!itemCanClick){
			return;
		}
		Category category = mAdapter.getItem(position);
		Intent intent = new Intent(getContext(), RecipeListActivity.class);
		intent.putExtra("tag", Constants.TAG_CATEGORY);
		intent.putExtra("id", category.getId());
		intent.putExtra("title", category.getName());
		IntentUtil.startActivity(getContext(), intent);
		itemCanClick = false;
		
	}
	
	private int mLastScrollY;
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		if(!hasWindowFocus){
			mLastScrollY = getScrollY();
		}else{
			scrollTo(0, mLastScrollY);
			itemCanClick = true;
		}
	}
	/**
	 * �ж��Ƿ��з���չ��
	 * @return
	 */
	public boolean isExpand() {
		return mLastExpandPos != -1;
	}
	/**
	 * �رյ�ǰչ���ķ���
	 */
	public void collapse() {
		clickGroupItem(mLastExpandPos);
	}
}
