package com.uplooking.meihaoshiguang.handler;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.uplooking.meihaoshiguang.Constants;
import com.uplooking.meihaoshiguang.activity.RecipeListActivity;
import com.uplooking.meihaoshiguang.adapter.IngredientGridViewAdapter;
import com.uplooking.meihaoshiguang.entity.IngredientEntity;
import com.uplooking.meihaoshiguang.tools.IntentUtil;

public class HomeIngredientHandler implements OnItemClickListener {
	private IngredientGridViewAdapter mIngredientGridViewAdapter;
	private Context context;
	public void init(Context context,GridView mGridView){
		this.context = context;
		mIngredientGridViewAdapter = new IngredientGridViewAdapter(context);
		mGridView.setAdapter(mIngredientGridViewAdapter);
		mGridView.setOnItemClickListener(this);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		IngredientEntity item = mIngredientGridViewAdapter.getItem(position);
		Intent intent = new Intent(context, RecipeListActivity.class);
		intent.putExtra("tag", Constants.TAG_SEARCH_BY_INGREDIENT);
		intent.putExtra("title", item.getName());
		IntentUtil.startActivity(context, intent);
	}
}
