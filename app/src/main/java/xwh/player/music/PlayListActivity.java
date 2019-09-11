package xwh.player.music;

import android.content.Context;
import android.content.Intent;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import xwh.lib.music.api.music163.SongAPI;
import xwh.lib.music.entity.PlayList;
import xwh.player.music.adapter.SongListDetailAdapter;

public class PlayListActivity extends BaseActivity {

	@BindView(R.id.recyclerView)
	RecyclerView mRecyclerView;

	private PlayList mPlayList;
	private SongListDetailAdapter mAdapter;

	public static Intent obtainIntent(Context context, PlayList playList) {
		Intent intent = new Intent(context, PlayListActivity.class);
		intent.putExtra("playList", playList);
		return intent;
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_play_list;
	}

	@Override
	protected void initView(){
		mPlayList = (PlayList) getIntent().getSerializableExtra("playList");
		mAdapter = new SongListDetailAdapter(mContext, mPlayList);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
		initData();
	}

	private void initData() {
		SongAPI.getInstance().getPlayListDetail(mPlayList, lists -> mAdapter.setData(mPlayList));

	}

}
