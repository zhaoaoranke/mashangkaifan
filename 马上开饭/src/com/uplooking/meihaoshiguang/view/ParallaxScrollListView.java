package com.uplooking.meihaoshiguang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Scroller;

import com.uplooking.meihaoshiguang.R;

public class ParallaxScrollListView extends ListView {
	/** �����¼���Y **/
	private float mLastY;
	/** ListViewͷ����ͼ����Ҫ�Ŵ��ImageView **/
	private ImageView mImg;
	/** ImageView��ԭʼ�߶� **/
	private int mImgHeight;
	/** ImageView�Ĳ��ֲ��� **/
	private ViewGroup.LayoutParams mImgParams;
	private static final int STATE_NORMAL = 1;// ����״̬
	private static final int STATE_SCALEING = 2;// ��������״̬
	private static final int STATE_SCALE_AUTO = 3;// �Զ�����״̬
	private int state = STATE_NORMAL;

	private Scroller mScroller;

	public ParallaxScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnScrollListener(onScrollListener);
		mScroller = new Scroller(context);
		
		
	}

	OnScrollListener onScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			mFirstVisibleItem = firstVisibleItem;
		}
	};
	private int mFirstVisibleItem;

	/**
	 * 
	 * 2.ACTION_DOWN ����false����������super.onTouchEvent(ev)
	 * ListView���ܹ�������down֮����¼������ٴ���onTouch 3.ACTION_DOWN
	 * ����true����������super.onTouchEvent(ev)
	 * ListView���Թ��������������ֶ�ֹͣ�����ˣ�down��move��up�¼����ᴫ��onTouch���� 4.ACTION_DOWN
	 * ����true,ACTION_MOVE ����false ListView���ܹ�����down��move��up�¼����ᴫ��onTouch����
	 * 5.ACTION_DOWN ����true,ACTION_MOVE ����true
	 * ListView���ܹ�����down��move��up�¼����ᴫ��onTouch���� 6.ACTION_DOWN
	 * ����super.onTouchEvent(ev)��ACTION_MOVE����false
	 * ListView���ܹ�����down��move��up�¼����ᴫ��onTouch���� 7.ACTION_DOWN
	 * ����super.onTouchEvent(ev)��ACTION_MOVE����true
	 * ListView���ܹ�����down��move��up�¼����ᴫ��onTouch����
	 * 
	 * 1.Ĭ��״̬--������super.onTouchEvent(ev)
	 * ListView������������down��move��up�¼����ᴫ��onTouch����
	 * 2.���ACTION_DOWN����true,ListView���Թ����������ڹ��������в���ֹͣ
	 * 3.���ACTION_DOWN����false,ListView���ܹ�������down֮����¼������ٴ���onTouch
	 * ����ACTION_DOWNҪ����super.onTouchEvent(ev)
	 * 4.���ACTION_MOVE����true,��ζ�Ž�ʧȥ�����ж�move�¼��Ĵ���Ҳ�����ܹ���
	 * 
	 * @param ev
	 * @return
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// ���磺ACTION_MASK & ACTION_POINTER_2_DOWN 
		//��0x000000ff& 0��00000105=0x0000005

		// ���Կ�����and����Ľ������С�ڵ���0x000000ff���Ǿ���˵and֮��
		//��������ٸ���ָ�ӽ��������ǻ�ACTION_POINTER_DOWN����ACTION_POINTER_UP
		switch (ev.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			return touchDown(ev);
		case MotionEvent.ACTION_MOVE:
			return touchMove(ev);
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (state == STATE_SCALEING) {
				collapseImg();
				state = STATE_SCALE_AUTO;
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	private boolean touchMove(MotionEvent ev) {
		float offset = ev.getY() - mLastY;
		switch (state) {
		case STATE_NORMAL:
			if (mFirstVisibleItem == 0 && getChildAt(0).getTop() == 0) {
				if (offset > 0) {
					state = STATE_SCALEING;
					mImgParams.height += offset / 2;
					mImg.setLayoutParams(mImgParams);
				}
			}
			break;
		case STATE_SCALEING:
			if (offset > 0) {
				mImgParams.height += offset / 2;
				if (mImgParams.height >= mImgHeight << 1) {
					mImgParams.height = (int) (mImgHeight << 1);
				}
			} else {
				mImgParams.height += offset;
				if (mImgParams.height <= mImgHeight) {
					mImgParams.height = mImgHeight;
					state = STATE_NORMAL;
					return super.onTouchEvent(ev);
				}
			}
			mImg.setLayoutParams(mImgParams);
			mLastY = ev.getY();
			return true;
		}
		mLastY = ev.getY();
		return super.onTouchEvent(ev);
	}

	private boolean touchDown(MotionEvent ev) {
		mLastY = ev.getY();
		switch (state) {
		case STATE_NORMAL:
			if (mImg == null) {
				mImg = (ImageView) findViewById(R.id.v_recipe_detail_head_imageview);
				mImgHeight = mImg.getHeight();
				mImgParams = mImg.getLayoutParams();
			}
			break;
		case STATE_SCALE_AUTO:
			mScroller.forceFinished(true);
			state = STATE_SCALEING;
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * ����ImageView
	 */
	private void collapseImg() {
		mScroller.startScroll(0, mImgParams.height, 0, mImgHeight
				- mImgParams.height, 600);
		invalidate();
	}

	@Override
	public void computeScroll() {
		super.computeScroll();
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				mImgParams.height = mScroller.getCurrY();
				mImg.setLayoutParams(mImgParams);
				invalidate();
			}
		} else if (state == STATE_SCALE_AUTO) {
			state = STATE_NORMAL;
		}
	}
}
