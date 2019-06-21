package xwh.player.music;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
import xwh.lib.music.entity.Song;
import xwh.lib.music.player.MusicManager;
import xwh.lib.music.player.MusicManager.MusicListener;
import xwh.lib.view.StatusBarUtil;

/**
 * Created by xwh on 2019/6/17.
 */
public class PlayerActivity extends BaseActivity{

	@BindView(R.id.player_background) View mBackground;
	@BindView(R.id.item_image) ImageView mCover;
	@BindView(R.id.item_name) TextView mTitle;
	@BindView(R.id.item_artist) TextView mArtist;

	private Song mSong;

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
		StatusBarUtil.setActivityTranslucent(this); // 状态栏透明

		mSong = (Song) getIntent().getSerializableExtra("song");

		MusicManager.getInstance().setMusicListener(new MusicListener() {
			@Override
			public void onPlay() {
				rotateAnim(mCover, 30000);
			}

			@Override
			public void onPause() {
				mRotateAnim.pause();
			}
		});

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

		mTitle.setText(mSong.name);
		mArtist.setText(mSong.artist);
		MusicManager.getInstance().start(mSong);

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

	@OnClick(R.id.item_image) void play() {
		MusicManager.getInstance().pauseOrPlay();
	}

	@Override
	protected void onStop() {
		super.onStop();
		MusicManager.getInstance().stop();
	}
}
