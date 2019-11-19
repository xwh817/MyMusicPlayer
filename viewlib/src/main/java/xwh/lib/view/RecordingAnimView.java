package xwh.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

import androidx.annotation.Nullable;

/**
 * Created by xwh on 2018/5/31.
 */
public class RecordingAnimView extends View {

	private int mColor = Color.WHITE;
	private Paint mPaint;
	private float mPaintWidth;
	private int[] items;
	private long mRefreshTime = 100;
	private boolean mIsRunning = false;
	private Random mRandom;
	private float floatRate = 0.5f;     // 波动的程度

	public RecordingAnimView(Context context) {
		this(context, null);
	}

	public RecordingAnimView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RecordingAnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}


	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(mColor);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setStrokeCap(Paint.Cap.ROUND);

		float scale = getContext().getResources().getDisplayMetrics().density;
		mPaintWidth = scale * 4;
		mPaint.setStrokeWidth(mPaintWidth);

		int max = getHeight();
		items = new int[]{max / 4, max / 2, max, max / 2, max / 4};

		mRandom = new Random();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int width = getWidth();
		int height = getHeight();

		if (items == null && height > 0) {
			init();
		}

		int itemWidth = width / items.length;
		int centerY = height / 2;
		float start = itemWidth / 2;
		for (int i = 0; i < items.length; i++) {
			int item = items[i];

			int fValue = (int) (item * floatRate);
			if (fValue > 0) {
				int rdm = mRandom.nextInt(fValue) - fValue / 2;
				item += rdm;
			}

			canvas.drawLine(start, centerY - item / 2, start, centerY + item / 2, mPaint);

			start += itemWidth;
		}


		if (mIsRunning) {
			postInvalidateDelayed(mRefreshTime);
		}
	}


	/**
	 * 开始动画
	 */
	public void start() {
		if (!mIsRunning) {       // 防止重复执行
			mIsRunning = true;
			invalidate();
		}
	}

	/**
	 * 停止动画
	 */
	public void stop() {
		mIsRunning = false;
	}

	public void setFloatRate(float floatRate) {
		if (floatRate < 0) {
			floatRate = 0;
		} else if (floatRate > 1.0f) {
			floatRate = 1.0f;
		}
		this.floatRate = floatRate;
	}

}
