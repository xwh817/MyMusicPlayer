package xwh.player.music;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import jp.wasabeef.glide.transformations.BlurTransformation;
import xwh.lib.base.utils.StringUtil;
import xwh.lib.music.api.music163.SongAPI;
import xwh.lib.music.entity.Song;
import xwh.lib.music.player.MusicListenerAdapter;
import xwh.lib.music.player.MusicManager;

/**
 * Created by xwh on 2019/6/17.
 */
public class PlayerActivity extends BaseActivity {

	@BindView(R.id.player_background)
	View mBackground;
	@BindView(R.id.item_image)
	ImageView mCover;
	@BindView(R.id.item_name)
	TextView mTitle;
	@BindView(R.id.item_artist)
	TextView mArtist;
	@BindView(R.id.text_position)
	TextView mTextPosition;
	@BindView(R.id.text_duration)
	TextView mTextDuration;
	@BindView(R.id.seekBar)
	SeekBar mSeekBar;
	@BindView(R.id.bt_play)
	ImageView mBtPlay;

	private MusicManager mPlayer;
	private Song mSong;
	private Observable mUpdateTask;
	private Disposable mDisposable;
	private Consumer mUpdateConsumer;

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_player;
	}

	public static Intent obtainIntent(Context context, Song song) {
		Intent intent = new Intent(context, PlayerActivity.class);
		intent.putExtra("song", song);
		return intent;
	}

	@Override
	protected void initView() {
		//StatusBarUtil.setActivityTranslucent(this); // 状态栏透明

		mPlayer = MusicManager.getInstance();
		mSong = (Song) getIntent().getSerializableExtra("song");

		mPlayer.setMusicListener(new MusicListenerAdapter() {
			@Override
			public void onPrepared(long duration) {
				mTextDuration.setText(StringUtil.getTimeFormat(duration));
				mSeekBar.setMax((int) duration);
			}

			@Override
			public void onPlay() {
				rotateAnim(mCover, 30000);
				mDisposable = mUpdateTask.subscribe(mUpdateConsumer);
				mBtPlay.setImageResource(R.drawable.player_pause);
			}

			@Override
			public void onPause() {
				mRotateAnim.pause();
				if (mDisposable != null) {
					mDisposable.dispose();
				}
				if (mBtPlay != null) {
					mBtPlay.setImageResource(R.drawable.player_play);
				}
			}
		});

		if (TextUtils.isEmpty(mSong.getCover())) {
			SongAPI.getInstance().getSongDetail(mSong.getId()+"", songs -> {
				if (songs.size() > 0) {
					mSong.setCover(songs.get(0).getCover());
					initCover();
				}
			});
		} else {
			initCover();
		}

		mTitle.setText(mSong.getName());
		mArtist.setText(mSong.getArtist());
		mPlayer.start(mSong);

		mUpdateTask = Observable.interval(1L, TimeUnit.SECONDS, AndroidSchedulers.mainThread());
		mUpdateConsumer = time -> {
			if (mTextPosition != null) {
				long position = mPlayer.getPosition();
				mTextPosition.setText(StringUtil.getTimeFormat(position));
				mSeekBar.setProgress((int) position);
			}
		};

		mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			int progress;

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				this.progress = progress;
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				mPlayer.seekTo(progress);
			}
		});

		mBtPlay.setOnClickListener(v -> mPlayer.pauseOrPlay());
	}

	private void initCover() {

		/*RequestOptions options = new RequestOptions().circleCrop();*/
		Glide.with(this)
				.load(mSong.getCover(300))
				.circleCrop()
				.transform()
				.into(mCover);

		Glide.with(this)
				.load(mSong.getCover(300))
				.transform(new BlurTransformation(40, 2))// 两个参数 radius高斯模糊半径，sampling图片缩放多少倍之后再进行模糊处理。
				.into(new CustomTarget<Drawable>() {
					@Override
					public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
						mBackground.setBackground(resource);
					}

					@Override
					public void onLoadCleared(@Nullable Drawable placeholder) {

					}
				});
	}


	private ObjectAnimator mRotateAnim;

	private void rotateAnim(View target, int duration) {
		if (mRotateAnim == null) {
			mRotateAnim = ObjectAnimator.ofFloat(target, "Rotation", 0f, 360f);
			mRotateAnim.setDuration(duration);
			mRotateAnim.setRepeatCount(-1);
			mRotateAnim.setInterpolator(new LinearInterpolator());
			mRotateAnim.start();
		} else {
			mRotateAnim.resume();
		}
	}

	@OnClick(R.id.item_image)
	void play() {
		mPlayer.pauseOrPlay();
	}

	@Override
	protected void onDestroy() {
		if (mPlayer.isPlaying()) {
			mPlayer.release();
		}

		super.onDestroy();  // ButterKnife unbind时会把之前注入的对象置空。所以要放在后面。
		mPlayer.setMusicListener(null);
	}
}
