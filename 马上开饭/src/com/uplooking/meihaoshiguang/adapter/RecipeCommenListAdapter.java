package com.uplooking.meihaoshiguang.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.uplooking.meihaoshiguang.entity.ResponceRecipeDetail.RecipeComment;
import com.uplooking.meihaoshiguang.tools.BitmapHelper;
import com.uplooking.meihaoshiguang.tools.TimeFormater;
import  com.uplooking.meihaoshiguang.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeCommenListAdapter extends BaseAdapter{
	private List<RecipeComment> list;
	private LayoutInflater inflater;
	private BitmapUtils bitmapUtils;
	public RecipeCommenListAdapter(Context context,List<RecipeComment> list) {
		super();
		inflater = LayoutInflater.from(context);
		this.list = list;
		bitmapUtils = BitmapHelper.getBitmapUtils(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public RecipeComment getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_recipe_comment, null);
			holder = new ViewHolder();
			holder.headPhotoImg = (ImageView) convertView.findViewById(R.id.item_recipe_comment_headImg);
			holder.nickNameTv = (TextView) convertView.findViewById(R.id.item_recipe_comment_nickNameTv);
			holder.contentTv = (TextView) convertView.findViewById(R.id.item_recipe_comment_contentTv);
			holder.dateTimeTv = (TextView) convertView.findViewById(R.id.item_recipe_comment_dateTv);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		RecipeComment comment = getItem(position);
		holder.nickNameTv.setText(comment.getUserNickName());
		holder.contentTv.setText(comment.getContent());
		holder.dateTimeTv.setText(TimeFormater.formatTime(comment.getCommentDate()));
//		bitmapUtils.display(holder.headPhotoImg, comment.getUserHeadPhoto());
		return convertView;
	}

	private class ViewHolder{
		ImageView headPhotoImg;
		TextView nickNameTv;
		TextView contentTv;
		TextView dateTimeTv;
	}

	public void addDatas(List<RecipeComment> list) {
		this.list.addAll(list);
		notifyDataSetChanged();
	}
	
}
