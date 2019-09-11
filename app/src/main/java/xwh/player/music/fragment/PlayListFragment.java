package xwh.player.music.fragment;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import xwh.lib.music.api.music163.SongAPI;
import xwh.lib.view.GridDecoration;
import xwh.lib.view.ViewUtils;
import xwh.player.music.R;
import xwh.player.music.adapter.PlayListAdapter;

/**
 * Created by xwh on 2019/9/9.
 */
public class PlayListFragment extends BaseFragment {

	@BindView(R.id.recyclerView)
	RecyclerView mRecyclerView;

	private PlayListAdapter mAdapter;

	@Override
	protected int getLayoutRes() {
		return R.layout.fragment_song_list;
	}

	@Override
	protected void initView() {
		mAdapter = new PlayListAdapter(mContext);
		mRecyclerView.setAdapter(mAdapter);
		int columns = 2;
		int dividerWidth = ViewUtils.dip2px(mContext, 12);
		mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, columns));
		mRecyclerView.addItemDecoration(new GridDecoration(dividerWidth, dividerWidth, columns));
		initData();
	}

	private void initData() {
		SongAPI.getInstance().getPlayList(lists -> mAdapter.setData(lists));
	}

}
