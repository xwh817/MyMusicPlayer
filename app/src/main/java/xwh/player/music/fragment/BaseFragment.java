package xwh.player.music.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by xwh on 2019/6/4.
 * 抽象的BaseFragment
 * 通过模板方法，定义了ButterKnife的流程，子类去实现模板方法。
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

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(getLayoutRes(), null);
			mUnbinder = ButterKnife.bind(this, mView);
			initView();
		}
		Log.d("Fragment", "onCreateView: " + mView.hashCode());
		return mView;
	}

	protected abstract int getLayoutRes();

	protected abstract void initView();

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mUnbinder != null) {
			mUnbinder.unbind();
			mUnbinder = null;
		}
		Log.d("Fragment", "onDestroyView: " + mView.hashCode());
	}
}
