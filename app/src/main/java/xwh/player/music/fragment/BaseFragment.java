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

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import xwh.player.music.constant.Tags;

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
		Log.d(Tags.FRAGMENT, "onAttach: " + this.hashCode());
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.mContext = null;
		Log.d(Tags.FRAGMENT, "onDetach: " + this.hashCode());
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(Tags.FRAGMENT, "onCreate: " + this.hashCode());
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(getLayoutRes(), null);
			mUnbinder = ButterKnife.bind(this, mView);
			initView();
		}

		if (isEventBusEnable()) {
			EventBus.getDefault().register(this);
		}

		Log.d(Tags.FRAGMENT, "onCreateView: " + this.hashCode());
		return mView;
	}

	protected abstract int getLayoutRes();

	protected abstract void initView();

	protected boolean isEventBusEnable(){
		return false;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mUnbinder != null) {
			mUnbinder.unbind();
			mUnbinder = null;
		}

		if (isEventBusEnable()) {
			EventBus.getDefault().register(this);
		}

		Log.d(Tags.FRAGMENT, "onDestroyView: " + this.hashCode());
	}
}
