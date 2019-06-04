package xwh.player.music.fragment;

import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;
import butterknife.Unbinder;

/**
 * Created by xwh on 2019/6/4.
 */
public abstract class BaseFragment extends Fragment {
	protected Context mContext;
	protected View mView;
	protected Unbinder mUnbinder;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		this.mContext = context;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.mContext = null;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mUnbinder != null) {
			mUnbinder.unbind();
			mUnbinder = null;
		}
	}
}
