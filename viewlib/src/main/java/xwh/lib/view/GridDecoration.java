package xwh.lib.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Adds interior dividers to a {@link RecyclerView} with a
 * {@link androidx.recyclerview.widget.GridLayoutManager}.
 */
public class GridDecoration extends RecyclerView.ItemDecoration {

	private int mNumColumns;
	private int mVSpace;
	private int mHSpace;

	public GridDecoration(int horizontalSpace, int verticalSpace, int numColumns) {
		mNumColumns = numColumns;
		mHSpace = horizontalSpace;
		mVSpace = verticalSpace;
	}

	/**
	 * Determines the size and location of offsets between items in the parent
	 * {@link RecyclerView}.
	 *
	 * @param outRect The {@link Rect} of offsets to be added around the child view
	 * @param view    The child view to be decorated with an offset
	 * @param parent  The {@code RecyclerView} onto which dividers are being added
	 * @param state   The current {@link RecyclerView.State}
	 *                of the {@code RecyclerView}
	 */
	@Override
	public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
		//super.getItemOffsets(outRect, view, parent, state);
		int position = parent.getChildAdapterPosition(view);
		outRect.left = position % mNumColumns == 0 ? mHSpace : mHSpace / 2;     // 只有第一列有左边距
		outRect.right = position % mNumColumns == mNumColumns - 1 ? mHSpace : mHSpace / 2;

		outRect.top = position < mNumColumns ? mVSpace : 0;     // 只有第一行有上边距
		outRect.bottom = mVSpace;
	}

}
