package xwh.player.music.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import xwh.player.music.R;

/**
 * Created by xwh on 2019/6/3.
 */
public class RecommendFragment extends BaseFragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_recommend, null);
			initView();
		}
		return mView;
	}

	private void initView(){

	}

}
