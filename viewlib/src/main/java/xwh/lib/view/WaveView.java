package xwh.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


/**
 * 水波纹扩散动画效果
 */
public class WaveView extends View {

    private int mColor = Color.DKGRAY;

    private int mInitRadius =50;    // 第一个初始圆圈的半径
    private int mLastStartRadius = mInitRadius;  // 上一帧起始圆圈半径
    private int mItemMargin = 80;   // 每圈之间的间隔
    private int mItemStep = 4;      // 每一帧变化
    private int mMaxRadius = 300;
    private int mCircleCount;
    private long mRefreshTime = 80;
    private boolean mIsRunning = false;


    private Paint mPaint;

    public WaveView(Context context) {
        this(context, null);
    }

    public WaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaveView, defStyleAttr, 0);
        mColor = a.getColor(R.styleable.WaveView_wave_color, mColor);
        mItemMargin = a.getDimensionPixelOffset(R.styleable.WaveView_wave_item_margin, mItemMargin);
        mInitRadius = a.getDimensionPixelOffset(R.styleable.WaveView_wave_init_radius, mInitRadius);
        mItemStep = a.getDimensionPixelOffset(R.styleable.WaveView_wave_speed, mItemStep);
        a.recycle();

        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
	    //mPaint.setStrokeWidth(2f);
    }

    public void setRefreshTime(long length) {
        mRefreshTime = length;
    }

    /**
     * 获取View的宽高在构造方法中拿不到的，getWidth()，getHeight()都会为零
     * @param hasWindowFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        mMaxRadius = getWidth() > getHeight() ? getHeight() / 2 : getWidth() / 2;
        invalidate();
    }
    /**
     * 防止window是去焦点时，也就是应用在后台时，停止View的绘制
     */
    @Override
    public void invalidate() {
        if (hasWindowFocus()) {
            super.invalidate();
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // 绘制扩散圆
        int radius = mLastStartRadius;
        int count = 0;
        while(radius <= mMaxRadius && count <= mCircleCount) {
            // 设置透明度
            int alpha = (int) (255 * (1.0f - radius*1.0f/mMaxRadius));
            if (alpha > 0) {
                mPaint.setAlpha(alpha);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2,  radius, mPaint);
            }

            radius += mItemMargin;
            count++;
        }

        mLastStartRadius += mItemStep;
        if (mLastStartRadius - mInitRadius >= mItemMargin){      // 上一帧超过了一个间隔周期，开始新的周期
            mLastStartRadius = mInitRadius;

            mCircleCount ++;
        }

        if (mIsRunning) {
            postInvalidateDelayed(mRefreshTime);
        }
    }

    /**
     * 开始扩散
     */
    public void start() {
        if (!mIsRunning) {       // 防止重复执行
            mIsRunning = true;
            mLastStartRadius = mInitRadius;
            mCircleCount = 0;
            invalidate();
        }
    }

    /**
     * 停止扩散
     */
    public void stop() {
        mIsRunning = false;
    }

    /**
     * 是否扩散中
     */
    public boolean isRunning() {
        return mIsRunning;
    }

    /**
     * 设置波浪圆颜色
     */
    public void setColor(int colorId) {
        mColor = colorId;
    }

    /**
     * 设置波浪圆之间间距
     */
    public void setItemMargin(int itemMargin) {
        mItemMargin = itemMargin;
    }

    /**
     * 设置中心圆半径
     */
    public void setMaxRadius(int maxRadius) {
        mMaxRadius = maxRadius;
    }

    public void setInitRadius(int initRadius) {
        mInitRadius = initRadius;
    }

}