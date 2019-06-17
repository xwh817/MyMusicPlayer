package xwh.player.music.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import xwh.player.music.R;

/**
 * Created by xwh on 2019/6/3.
 */
public class RecommendFragment extends BaseFragment {

	//@BindView(R.id.imageView)
	ImageView mImageView;
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_recommend, null);
			//mUnbinder = ButterKnife.bind(this, mView);
			mImageView = mView.findViewById(R.id.imageView);
			initView();
		}
		Log.d("Fragment", "onCreateView: " + mView.hashCode());
		return mView;
	}

	private void initView(){
		String url = "https://avatars3.githubusercontent.com/u/5926985?s=400&u=a005713b02d92ed3669a32be468823fe0b680c79&v=4";
		Glide.with(this).load(url).into(mImageView);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d("Fragment", "onDestroyView: " + mView.hashCode());
	}
}
