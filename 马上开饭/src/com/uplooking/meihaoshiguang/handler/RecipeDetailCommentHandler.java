package com.uplooking.meihaoshiguang.handler;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.uplooking.meihaoshiguang.activity.RecipeCommentActivity;
import com.uplooking.meihaoshiguang.entity.ResponceRecipeDetail.RecipeComment;
import com.uplooking.meihaoshiguang.entity.ResponceRecipeDetail.RecipeCommentsObject;
import com.uplooking.meihaoshiguang.tools.BitmapHelper;
import com.uplooking.meihaoshiguang.tools.IntentUtil;
import com.uplooking.meihaoshiguang.tools.TimeFormater;
import com.uplooking.meihaoshiguang.R;

public class RecipeDetailCommentHandler implements OnClickListener {
	/** ���۵���ͼ **/
	private View contentView;
	/** ��̬��� ����item������ **/
	private LinearLayout container;
	private TextView commentCountTv;
	private Button commentAllBtn;
	private LayoutInflater inflater;
	private Context context;
	private int originalCount = 0;
	private int recipeId;

	public RecipeDetailCommentHandler(Context context) {
		super();
		this.context = context;
		inflater = LayoutInflater.from(context);

	}

	public void setDatas(View group, RecipeCommentsObject commentObject,
			int recipeId) {
		contentView = group;
		this.recipeId = recipeId;
		Log.e("setDatas",commentObject == null?"��":"�ǿ�");
		Log.e("setDatas",commentObject.getTotalCount()+"");
		if (commentObject != null && commentObject.getTotalCount() != 0) {
			
			originalCount = commentObject.getTotalCount();
			initViews();
			String s=String.format(context.getString(R.string.togeter_speak),originalCount);
			commentCountTv.setText(s);
			List<RecipeComment> commentList = commentObject.getList();
			for (RecipeComment cookbookComment : commentList) {
				container.addView(getCommentItem(cookbookComment));
			}
		}
	}

	private void initViews() {
		//��ʾ��װ���۵�����LinearLayout
		contentView.setVisibility(View.VISIBLE);
		//�õ�ֱ��װ�����۵�LinearLayout
		container = (LinearLayout) contentView
				.findViewById(R.id.v_recipe_detail_comment_layout_commentContainer);
		//��ʾ����������
		commentCountTv = (TextView) contentView
				.findViewById(R.id.v_recipe_detail_comment_layout_commentCount);
		//�鿴ȫ�����۵İ�ť
		commentAllBtn = (Button) contentView
				.findViewById(R.id.v_recipe_detail_comment_layout_commentAllBtn);
		//ȫ�����۵ļ���
		commentAllBtn.setOnClickListener(this);
	}

	/**
	 * ��ȡһ�����۵�item��ͼ�����������
	 * 
	 * @param comment
	 * @return
	 */
	private View getCommentItem(RecipeComment comment) {
		View view = inflater.inflate(R.layout.item_recipe_comment, null);
		
		ImageView headImg = (ImageView) view
				.findViewById(R.id.item_recipe_comment_headImg);
		TextView nickNameTv = (TextView) view
				.findViewById(R.id.item_recipe_comment_nickNameTv);
		TextView contentTv = (TextView) view
				.findViewById(R.id.item_recipe_comment_contentTv);
		TextView dateTv = (TextView) view
				.findViewById(R.id.item_recipe_comment_dateTv);
		String headImgUrl = comment.getUserHeadPhoto();
		if (!TextUtils.isEmpty(headImgUrl)) {
			// TODO ��ʾ����ͷ��
			BitmapHelper.getBitmapUtils(context).display(headImg, headImgUrl);
		}
		
		nickNameTv.setText(comment.getUserNickName());
		contentTv.setText(comment.getContent());
		dateTv.setText(TimeFormater.formatTime(comment.getCommentDate()));
		return view;
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent(context, RecipeCommentActivity.class);
		intent.putExtra("recipe_id", recipeId);
		IntentUtil.startActivity(context, intent);
	}

	private int addCount = 0;

	public void addComment(RecipeComment comment) {
		addCount++;
		if (contentView.getVisibility() != View.VISIBLE) {
			// ˵��֮ǰû���κ�����
			initViews();
			container.addView(getCommentItem(comment));
		} else if (container.getChildCount() < 3) {
			container.addView(getCommentItem(comment), 0);
			commentCountTv.setText(String.format(
					context.getString(R.string.togeter_speak),
					container.getChildCount()));
		} else {
			container.removeViewAt(2);
			container.addView(getCommentItem(comment), 0);
		}
		commentCountTv.setText(String.format(
				context.getString(R.string.togeter_speak), addCount
						+ originalCount));
	}
}
