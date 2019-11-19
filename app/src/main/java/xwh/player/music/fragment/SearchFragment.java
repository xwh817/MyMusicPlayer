package xwh.player.music.fragment;

import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import xwh.lib.base.utils.NetworkUtil;
import xwh.lib.music.api.music163.SongAPI;
import xwh.lib.speech.OnlineAsrManager;
import xwh.lib.view.KeyboardUtils;
import xwh.lib.view.RecordingAnimView;
import xwh.lib.view.WaveView;
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


	@BindView(R.id.image_tab_speech)
	ImageView mImageSpeech;
	@BindView(R.id.waveView)
	WaveView mWaveView;
	@BindView(R.id.recordingAnimView)
	RecordingAnimView mRecordingAnimView;
	private Animation anim;
	private boolean isPressed;
	private boolean isAnimRunning;

	protected Toast mToast;
	private SimpleSongListAdapter mAdapter;
	private OnlineAsrManager mAsrManager;

	@Override
	protected int getLayoutRes() {
		return R.layout.fragment_search;
	}

	@Override
	protected void initView() {
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


		mView.findViewById(R.id.ll_tab_speech).setOnTouchListener(new View.OnTouchListener() {
			private long downTime = -1;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				switch (action) {
					case MotionEvent.ACTION_DOWN:
						if (downTime == -1) {
							downTime = System.currentTimeMillis();
						}
						isPressed = true;
						startSpeech();
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						if (isPressed || isAnimRunning) {
							isPressed = false;
							if (NetworkUtil.isNetworkAvailable(mContext)) {
								long now = System.currentTimeMillis();
								if (now - downTime < 500) {
									showToast(getString(R.string.speech_start));
								}
								stopSpeech();
							}
							downTime = -1;
						}
						break;
					default:
						break;
				}

				return true;
			}
		});
	}

	private void startSpeech() {
		startAnim();
		mAsrManager.start();
	}

	private void stopSpeech() {
		mAsrManager.stop();
		stopAnim();
	}

	private void onSearch() {
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
		startSpeech();
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


	public void showToast(final String text) {
		if (mToast == null) {
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
			mToast.setGravity(Gravity.BOTTOM, 0, 200);
			//int padding = DensityUtil.dip2px(mContext, 6);
			//mToast.getView().setPadding(padding * 2, padding, padding * 2, padding);
			//mToast.getView().setBackgroundResource(R.drawable.bg_black_toast);
		} else {
			mToast.setText(text);
			mToast.setDuration(text.length() > 20 ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		}
		mToast.show();


	}

	private void startAnim() {
		if (isAnimRunning) {
			return;
		}

		if (anim == null) {
			anim = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.8f);
			anim.setDuration(500);
			anim.setFillAfter(true);
			anim.setInterpolator(new AnticipateOvershootInterpolator());

			anim.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					isAnimRunning = true;
					mImageSpeech.setImageResource(R.drawable.bg_tab_speech);
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if (isPressed || isAnimRunning) {
						mWaveView.setVisibility(View.VISIBLE);
						mWaveView.start();
						mRecordingAnimView.setVisibility(View.VISIBLE);
						mRecordingAnimView.start();
					}
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}
			});
		}

		if (Thread.currentThread() == Looper.getMainLooper().getThread()) { // 是否在主线程
			mImageSpeech.startAnimation(anim);
		} else {
			mImageSpeech.post(() -> mImageSpeech.startAnimation(anim));
		}

	}

	private void stopAnim() {
		if (Thread.currentThread() == Looper.getMainLooper().getThread()) { // 是否在主线程
			isAnimRunning = false;
			mImageSpeech.clearAnimation();
			mImageSpeech.setImageResource(R.drawable.ic_tab_speech);
			mWaveView.stop();
			mWaveView.setVisibility(View.INVISIBLE);
			mRecordingAnimView.stop();
			mRecordingAnimView.setVisibility(View.INVISIBLE);
		} else {
			mWaveView.post(() -> stopAnim());
		}
	}


}
