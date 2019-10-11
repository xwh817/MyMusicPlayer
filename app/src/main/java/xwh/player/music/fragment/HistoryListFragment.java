package xwh.player.music.fragment;

import android.util.Log;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import xwh.lib.music.dao.HistoryDao;
import xwh.lib.music.entity.Song;
import xwh.player.music.R;
import xwh.player.music.adapter.SimpleSongListAdapter;
import xwh.player.music.constant.Tags;

/**
 * Created by xwh on 2019/6/30.
 */
public class HistoryListFragment extends BaseFragment {

	@BindView(R.id.recyclerView)
	RecyclerView mRecyclerView;

	private SimpleSongListAdapter mAdapter;
	private boolean needRefresh = true;

	@Override
	protected int getLayoutRes() {
		return R.layout.fragment_song_list;
	}

	protected void initView(){
		mAdapter = new SimpleSongListAdapter(mContext);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
		initData();
	}

	private void initData() {
        needRefresh = false;
		mAdapter.setData(HistoryDao.getAll());
	}

	@Override
	protected boolean isEventBusEnable() {
		return true;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void update(Song song) {
		needRefresh = true;
	}

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser && needRefresh && mAdapter!= null) {
            initData();
        }
        Log.d(Tags.FRAGMENT, "setUserVisibleHint: " + isVisibleToUser);
    }
}
