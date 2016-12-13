package com.uplooking.meihaoshiguang.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.uplooking.meihaoshiguang.tools.DensityUtil;
import com.uplooking.meihaoshiguang.R;

public class IndicatorView extends View {
	/** ��ͨԲ����ɫ **/

	private int normalIndicatorColor = 0xffffffff;
	/** ������Բ����ɫ **/
	private int slidingIndictorColor = 0xffff0000;
	private Integer circleWidth = 8;
	private Integer circleMargin = 8;
	private Integer topMargin = 6;
	private Integer bottomMargin = 6;
	private int indicatorsCount;//ԭ�����
	private Paint paint;
	//�ߴ���ȡ
	//getDimension()���ؽ����20.5f��
	//getDimensionPixelSize()���ؽ������21��
	//getDimensionPixelOffset()���ؽ������20��
	public IndicatorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//��ȡ�Զ�������
		TypedArray typedArray = context.obtainStyledAttributes(attrs,
				R.styleable.IndicatorView);
		normalIndicatorColor = typedArray.getColor(
				R.styleable.IndicatorView_normalCircleColor,
				normalIndicatorColor);
		slidingIndictorColor = typedArray.getColor(
				R.styleable.IndicatorView_slideCircleColor,
				slidingIndictorColor);
		topMargin = typedArray.getDimensionPixelOffset(
				R.styleable.IndicatorView_topMargin, getPx(topMargin));
		bottomMargin = typedArray.getDimensionPixelOffset(
				R.styleable.IndicatorView_bottomMargin, getPx(bottomMargin));
		circleWidth = typedArray.getDimensionPixelOffset(
				R.styleable.IndicatorView_circleWidth, getPx(circleWidth));
		circleMargin = typedArray.getDimensionPixelOffset(
				R.styleable.IndicatorView_circleMargin, getPx(circleMargin));
		typedArray.recycle();

		paint = new Paint();
		paint.setAntiAlias(true);
	}

	private int getPx(int dimension) {
		return DensityUtil.dip2px(getContext(), dimension);
	}

	/**
	 * ����ָʾ���ĸ���
	 * 
	 * @param indicatorsCount
	 */
	public void setIndicatorsCount(int indicatorsCount) {
		this.indicatorsCount = indicatorsCount;
		LayoutParams params = getLayoutParams();
		int height = topMargin + bottomMargin + circleWidth;
		int width = indicatorsCount * (circleWidth + circleMargin);
		if (params == null) {
			params = new LayoutParams(width, height);
		} else {
			params.width = width;
			params.height = height;
		}
		setLayoutParams(params);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor(normalIndicatorColor);
		for (int i = 0; i < indicatorsCount; i++) {
			//40,50,
			canvas.drawCircle(circleWidth / 2 + i
					* (circleMargin + circleWidth),
					topMargin + circleWidth / 2, circleWidth / 2, paint);
		}
		paint.setColor(slidingIndictorColor);
		canvas.drawCircle(slidingCircleX + circleWidth / 2, circleWidth / 2
				+ topMargin, circleWidth / 2, paint);
		if (slidingCircleX > getWidth() - circleWidth) {
			canvas.drawCircle(slidingCircleX + circleWidth / 2 - getWidth(),
					circleWidth / 2 + topMargin, circleWidth / 2, paint);
		}
	}

	/** ������Բ��x������� **/
	private float slidingCircleX;
	
	public void move(int scrollX, int pagerWidth) {
		slidingCircleX = (scrollX * 1f * (circleWidth + circleMargin) / pagerWidth)
				% getWidth();
		if (slidingCircleX < 0) {
			slidingCircleX += getWidth();
		}
		//ˢ�¿ؼ�
		//����⣬���ǵ���onDraw
		invalidate();
	}
}


