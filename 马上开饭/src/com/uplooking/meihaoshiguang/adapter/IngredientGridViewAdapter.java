package com.uplooking.meihaoshiguang.adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.uplooking.meihaoshiguang.entity.IngredientEntity;
import com.uplooking.meihaoshiguang.view.CircleImageView;
import com.uplooking.meihaoshiguang.R;

/**
 * ÏÔÊ¾ÐÂÏÊÊ³²ÄµÄGridViewµÄAdapter
 * 
 * @author Li
 * 
 */
public class IngredientGridViewAdapter extends BaseAdapter {
	private List<IngredientEntity> ingredientsList;
	private LayoutInflater mInflater;
	private AssetManager assetManager;
	private List<Drawable> drawableList;

	public IngredientGridViewAdapter(Context context) {
		mInflater = LayoutInflater.from(context);
		// ¶ÁÈ¡asset×ÊÔ´
		assetManager = context.getAssets();
		initDatas();
	}

	private void initDatas() {
		ingredientsList = new ArrayList<IngredientEntity>();
		ingredientsList.add(new IngredientEntity("ÂÜ²·", "luobo"));
		ingredientsList.add(new IngredientEntity("°×²Ë", "baicai"));
		ingredientsList.add(new IngredientEntity("ÑòÈâ", "yangrou"));
		ingredientsList.add(new IngredientEntity("ÅÅ¹Ç", "paigu"));
		ingredientsList.add(new IngredientEntity("É½Ò©", "shanyao"));
		ingredientsList.add(new IngredientEntity("¶¬¹Ï", "donggua"));
		ingredientsList.add(new IngredientEntity("ÍÁ¶¹", "tudou"));
		ingredientsList.add(new IngredientEntity("¼¦µ°", "jidan"));
		ingredientsList.add(new IngredientEntity("¶¹¸¯", "doufu"));
		ingredientsList.add(new IngredientEntity("ºÚÄ¾¶ú", "heimuer"));
		ingredientsList.add(new IngredientEntity("¼¦³á", "jichi"));
		ingredientsList.add(new IngredientEntity("¼¦Èâ", "jirou"));
		ingredientsList.add(new IngredientEntity("ÄÏ¹Ï", "nangua"));
		ingredientsList.add(new IngredientEntity("Å£Èâ", "niurou"));
		ingredientsList.add(new IngredientEntity("ÇÑ×Ó", "qiezi"));

		drawableList = new ArrayList<Drawable>();
		for (IngredientEntity ingredient : ingredientsList) {
			try {
				
				Drawable db=Drawable.createFromStream(
						assetManager.open("shicai/" + ingredient.getImgName()
								+ ".jpg"), "");
				drawableList.add(db);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public int getCount() {
		return ingredientsList.size();
	}

	@Override
	public IngredientEntity getItem(int position) {
		return ingredientsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.item_home_ingredient, null);
			holder = new ViewHolder();
			holder.img = (CircleImageView) convertView
					.findViewById(R.id.item_home_head_ingredient_img);
			holder.tv = (TextView) convertView
					.findViewById(R.id.item_home_head_ingredient_text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(getItem(position).getName());
		holder.img.setImageDrawable(drawableList.get(position));
		return convertView;
	}

	private class ViewHolder {
		CircleImageView img;
		TextView tv;
	}
}
