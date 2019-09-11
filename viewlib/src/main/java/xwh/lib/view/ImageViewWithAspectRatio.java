package xwh.lib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Created by xwh on 2019/9/10.
 * 指定宽高比
 */
public class ImageViewWithAspectRatio extends AppCompatImageView {
	public ImageViewWithAspectRatio(Context context) {
		this(context, null);
	}

	public ImageViewWithAspectRatio(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ImageViewWithAspectRatio(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		if (attrs != null) {
			initAttributes(attrs);
		}
	}

	// 宽高比
	private float mRatio;

	public void setRatio(float ratio) {
		mRatio = ratio;
	}


	private void initAttributes(AttributeSet attrs) {
		Context context = getContext();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageViewWithAspectRatio);
		this.mRatio = a.getFloat(R.styleable.ImageViewWithAspectRatio_ratio, 1.0f);

		a.recycle();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mRatio > 0) {
			// 获取宽度
			int width = MeasureSpec.getSize(widthMeasureSpec);
			// 设置当前控件宽高
			setMeasuredDimension(width, (int) (width / mRatio));
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}

	}

}
