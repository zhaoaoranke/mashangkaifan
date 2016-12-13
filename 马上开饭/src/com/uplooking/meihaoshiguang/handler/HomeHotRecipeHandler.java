package com.uplooking.meihaoshiguang.handler;


import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.uplooking.meihaoshiguang.activity.RecipeDetailActivity;
import com.uplooking.meihaoshiguang.entity.ResponceHome.HotRecipeObject.HotRecipe;


import com.uplooking.meihaoshiguang.tools.BitmapHelper;
import com.uplooking.meihaoshiguang.tools.IntentUtil;
import com.uplooking.meihaoshiguang.R;

public class HomeHotRecipeHandler implements OnItemClickListener {
	private static final int NUM_COLUMNS = 3;
	private GridView mGridView;
	private HomeHotRecipeAdapter mAdapter;
	private Context context;
	
	public void init(Context context, List<HotRecipe> list, GridView gridView) {
		this.context = context;
		
		mGridView = gridView;
		mGridView.setNumColumns(NUM_COLUMNS);
		mAdapter = new HomeHotRecipeAdapter(list);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(context, RecipeDetailActivity.class);
		intent.putExtra("recipe_id", mAdapter.getItem(position).id);
		IntentUtil.startActivity(context, intent);
	}

	private class HomeHotRecipeAdapter extends BaseAdapter {
		private List<HotRecipe> list;
		private LayoutInflater mInflater;
		private BitmapUtils mBitmapUtils;

		public HomeHotRecipeAdapter(List<HotRecipe> list) {
			super();
			// this.list=new ArrayList<HotRecipe>();
			// for (int i = 0; i < list.size()*5; i++) {
			// this.list.add(list.get(i%6));
			// }
			this.list = list;
			mInflater = LayoutInflater.from(context);
			mBitmapUtils = BitmapHelper.getBitmapUtils(context);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public HotRecipe getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.item_home_hot_recipe,
						null);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView
						.findViewById(R.id.item_home_head_recipe_img);
				holder.title = (TextView) convertView
						.findViewById(R.id.item_home_head_recipe_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			HotRecipe recipe = getItem(position);
			holder.title.setText(recipe.title);
			mBitmapUtils.display(holder.img, recipe.img);
			return convertView;
		}

		private class ViewHolder {
			ImageView img;
			TextView title;
		}
	}
}
