package xwh.player.music.fragment;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import xwh.lib.music.api.music163.SongAPI;
import xwh.lib.speech.OnlineAsrManager;
import xwh.lib.view.KeyboardUtils;
import xwh.player.music.R;
import xwh.player.music.adapter.SimpleSongListAdapter;

/**
 * Created by xwh on 2019/10/10.
 */
public class SearchFragment extends BaseFragment {

	@BindView(R.id.search_input)
	EditText mSearchInput;
	@BindView(R.id.recyclerView)
	RecyclerView mRecyclerView;

	private SimpleSongListAdapter mAdapter;
	private OnlineAsrManager mAsrManager;

	@Override
	protected int getLayoutRes() {
		return R.layout.fragment_search;
	}

	@Override
	protected void initView() {
		/*mSearchInput.setOnEditorActionListener((v, actionId, event) -> {
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				onSearch();
			}
			return false;
		});*/
		mAdapter = new SimpleSongListAdapter(mContext);
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
		mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

		initSpeech();
	}

	private void initSpeech() {
		mAsrManager = OnlineAsrManager.getInstance();
		mAsrManager.initPermission(getActivity());
		mAsrManager.initListener(mContext);
		mAsrManager.setOnSpeechResult(text -> {
			mSearchInput.setText(text);
			onSearch();
		});
	}

	private void onSearch(){
		String keywords = mSearchInput.getText().toString();
		if (TextUtils.isEmpty(keywords)) {
			Toast.makeText(mContext, R.string.hint_search, Toast.LENGTH_SHORT).show();
		} else {
			SongAPI.getInstance().search(keywords, songs -> mAdapter.setData(songs));
			KeyboardUtils.hideKeyboard(mContext, mSearchInput);
		}
	}

	@OnClick(R.id.bt_search)
	void onSearchClick(View bt) {
		onSearch();
	}
	@OnEditorAction(R.id.search_input)
	boolean onEditorAction(TextView v, int actionId, KeyEvent key) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
			onSearch();
		}
		return false;
	}


	@OnClick(R.id.bt_speech)
	void onSpeechClick(View bt) {
		mAsrManager.start();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (mView != null) {
			if (!isVisibleToUser) {
				KeyboardUtils.hideKeyboard(mContext, mSearchInput);
			}
		}
	}


}
