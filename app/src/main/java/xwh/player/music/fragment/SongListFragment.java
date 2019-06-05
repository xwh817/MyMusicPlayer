package xwh.player.music.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import xwh.lib.music.api.music163.SongAPI;
import xwh.lib.music.entity.Song;
import xwh.player.music.R;
import xwh.player.music.adapter.SongListAdapter;

/**
 * Created by xwh on 2019/6/3.
 */
public class SongListFragment extends BaseFragment {

	@BindView(R.id.recyclerView)
	RecyclerView mRecyclerView;

	private SongListAdapter mAdapter;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_song_list, null);
			mUnbinder = ButterKnife.bind(this, mView);
			initView();
		}
		initData();
		return mView;
	}

	private void initView(){
		mAdapter = new SongListAdapter(mContext);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
	}

	private void initData() {
		SongAPI.getInstance().getTopList(0, new Consumer<List<Song>>() {
			@Override
			public void accept(List<Song> songs) {
				mAdapter.setData(songs);
			}
		});

	}

}
