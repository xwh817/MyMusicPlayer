package xwh.player.music.fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import xwh.lib.music.api.music163.SongAPI;
import xwh.player.music.R;
import xwh.player.music.adapter.SongListAdapter;

/**
 * Created by xwh on 2019/6/3.
 */
public class SongListFragment extends BaseFragment {

	@BindView(R.id.recyclerView)
	RecyclerView mRecyclerView;

	private SongListAdapter mAdapter;

	@Override
	protected int getLayoutRes() {
		return R.layout.fragment_song_list;
	}

	protected void initView(){
		mAdapter = new SongListAdapter(mContext);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
		initData();
	}

	private void initData() {
		SongAPI.getInstance().getTopList(0, songs -> mAdapter.setData(songs));
	}

}
