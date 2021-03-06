package com.uplooking.meihaoshiguang.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;
import com.uplooking.meihaoshiguang.entity.ResponceRecipeDetail.RecipeStep;
import  com.uplooking.meihaoshiguang.R;

public class RecipeStepListAdapter extends BaseAdapter {

	private List<RecipeStep> recipeSteps;
	private LayoutInflater mInflater;
	private BitmapUtils bitmapUtils;
	private static final ColorDrawable TRANSPARENT_DRAWABLE = new ColorDrawable(
			android.R.color.black);
	
	
	
	public RecipeStepListAdapter(Context context, List<RecipeStep> cookSteps) {
		super();
		mInflater = LayoutInflater.from(context);
		this.recipeSteps = cookSteps;
		bitmapUtils = new BitmapUtils(context);
		// bitmapUtils.configDefaultBitmapMaxSize(100, 100);
		Bitmap bitmap = null;
		//Ĭ�ϱ���ͼƬ
		bitmapUtils.configDefaultLoadingImage(bitmap);
	}

	@Override
	public int getCount() {
		return recipeSteps.size();
	}

	@Override
	public RecipeStep getItem(int position) {
		return recipeSteps.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_recipe_step, null);
			holder = new ViewHolder();
			holder.stepNum = (TextView) convertView
					.findViewById(R.id.recipe_detail_step_position);
			holder.img = (ImageView) convertView
					.findViewById(R.id.recipe_detail_step_image);
			holder.text = (TextView) convertView
					.findViewById(R.id.recipe_detail_step_content);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		RecipeStep cookStep = getItem(position);
		holder.stepNum.setText(position + 1 + "");
		holder.text.setText(Html.fromHtml(cookStep.getTitle()));
		bitmapUtils.display(holder.img, cookStep.getImg(),
				new CustomBitmapLoadCallBack());
		return convertView;
	}

	public class CustomBitmapLoadCallBack extends
			DefaultBitmapLoadCallBack<ImageView> {

		

		@Override
		public void onLoadCompleted(ImageView container, String uri,
				Bitmap bitmap, BitmapDisplayConfig config, BitmapLoadFrom from) {
			
			//����Ч��
		    TransitionDrawable transitionDrawable = new TransitionDrawable(
					new Drawable[] { 
							TRANSPARENT_DRAWABLE,
							new BitmapDrawable(container.getResources(), bitmap) 
							});
			container.setImageDrawable(transitionDrawable);
			transitionDrawable.startTransition(500);
		}
	}

	

	

	private class ViewHolder {
		TextView stepNum;
		ImageView img;
		TextView text;
	}
}
