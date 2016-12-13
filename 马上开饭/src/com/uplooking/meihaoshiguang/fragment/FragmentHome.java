package com.uplooking.meihaoshiguang.fragment;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import android.widget.TextView;


import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.uplooking.meihaoshiguang.Constants;
import com.uplooking.meihaoshiguang.activity.SearchActivity;
import com.uplooking.meihaoshiguang.entity.ResponceHome;
import com.uplooking.meihaoshiguang.handler.HomeHotRecipeHandler;
import com.uplooking.meihaoshiguang.handler.HomeIngredientHandler;
import com.uplooking.meihaoshiguang.tools.IntentUtil;
import com.uplooking.meihaoshiguang.tools.ViewPagerScroller;
import com.uplooking.meihaoshiguang.view.AutoScrollViewPager;
import com.uplooking.meihaoshiguang.view.IndicatorView;
import com.uplooking.meihaoshiguang.view.AutoScrollViewPager.AutoSlidingPagerAdapter;
import com.uplooking.meihaoshiguang.view.AutoScrollViewPager.OnScollChangeListener;
import  com.uplooking.meihaoshiguang.R;




public class FragmentHome extends Fragment 
		implements OnPageChangeListener,
		OnClickListener
		{
	private ListView mListView;
	private AutoScrollViewPager mHeadViewPager;
	private HttpUtils mHttpUtils;
	private IndicatorView mIndicatorView;
	private View mSearchView;
	private AutoSlidingPagerAdapter pagerAdapter;
	
	
	private GridView mHotRecipeGridView;
	private TextView mPagerTitle;
	private GridView mIngredientGridView;
	public AutoScrollViewPager.OnScollChangeListener asvpocl=new OnScollChangeListener() {
		
		@Override
		public void scroll(int x) {
			// TODO Auto-generated method stub
			mIndicatorView.move(x, mHeadViewPager.getWidth());
			
		}
	};
	
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.fragment_home, null);
	
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);//ע�����������
		mHttpUtils = new HttpUtils();
		
		initViews();
		loadDatas();
	}

	private void loadDatas() {
		mHttpUtils.send(HttpMethod.GET, 
				Constants.URL_HOME,	new RequestCallBack<Object>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
								Toast.makeText(FragmentHome.this.getActivity(), "��������ʧ��,��ȷ��������������,�Ժ����ԣ�����", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(ResponseInfo<Object> arg0) {
						
						String result = arg0.result.toString();
				
						Gson gson = new Gson();
						ResponceHome responceHome = gson.fromJson(result,
								ResponceHome.class);
						
						//�����ڲ����adpter
						pagerAdapter = mHeadViewPager.new AutoSlidingPagerAdapter(
								responceHome.getHeadObject().list);
						mHeadViewPager.setAdapter(pagerAdapter);
						
						mHeadViewPager
								.setOnScollChangeListener(asvpocl);
						
						
						
						mIndicatorView.setIndicatorsCount(responceHome.getHeadObject().list.size());

						// ������ʳ
						HomeHotRecipeHandler hotRecipeHandler = new HomeHotRecipeHandler();
						hotRecipeHandler.init(getActivity(),
								responceHome.getRecipe_object().list,
								mHotRecipeGridView);
						//����ʳ��
						HomeIngredientHandler ingredientHandler = new HomeIngredientHandler();
						ingredientHandler.init(getActivity(),
								mIngredientGridView);
						List<String> datas = new ArrayList<String>();
						for (int i = 0; i < 100; i++) {
							datas.add("ģ������" + i);
						}
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(
								getActivity(),
								android.R.layout.simple_list_item_1, datas);
						mListView.setAdapter(adapter);
					}
				}
				);
	
	}

	private void initViews() {
		// ����ʹ��getView()��getActivity()һ��
		mListView = (ListView) getView().findViewById(R.id.home_listview);
		// �ڴ�����ʹ�� listView .addHeaderView(...)
		// ����������ListView����Ϸ�������������������������һ������һ���������
		// ������ʹ�� .addHeaderView(...) ,
		// ��������ӵ���������Ϸ�������ӵ��Ⱥ�˳�����ϵ������С�
		mListView.addHeaderView(getViewPagerOfHeadView());

		mSearchView = getView().findViewById(R.id.home_searchView);
		mSearchView.setOnClickListener(this);
	}
	//��ù������ؼ�
	private View getViewPagerOfHeadView() {
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.v_home_head, null);
		mHeadViewPager = (AutoScrollViewPager) view
				.findViewById(R.id.home_head_viewpager);
		
	
		//��ҳ��ı�����������л�������ֵ
		mHeadViewPager.setOnPageChangeListener(this);
	
		mPagerTitle = (TextView) view.findViewById(R.id.pager_title);
		
		mIndicatorView = (IndicatorView) view.findViewById(R.id.indicatorView);
		
		
		mHotRecipeGridView = (GridView) view
				.findViewById(R.id.home_head_recipe_gridview);
		mIngredientGridView = (GridView) view
				.findViewById(R.id.home_head_ingredient_gridview);
//		try {
//			Field field = ViewPager.class.getDeclaredField("mScroller");
//			field.setAccessible(true);
//			ViewPagerScroller scroller = new ViewPagerScroller(
//					mHeadViewPager.getContext());
//			field.set(mHeadViewPager, scroller);
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		}
		return view;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		Log.e("onPageSelected",position+"");
		mPagerTitle.setText(pagerAdapter.getItem(position).name);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onClick(View v) {
		if (v == mSearchView) {
			Intent intent = new Intent(getActivity(), SearchActivity.class);
			//getActivity().startActivity(intent);
			
			IntentUtil.startActivity(getActivity(), intent);
			
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		Log.e("yyy",hidden+"");
		if (hidden) {
			mHeadViewPager.stopSliding();
		} else {
			mHeadViewPager.startSliding();
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mHeadViewPager.stopSliding();
	}

	@Override
	public void onResume() {
		super.onResume();
		mHeadViewPager.startSliding();

	}

	
	
	
	
}


