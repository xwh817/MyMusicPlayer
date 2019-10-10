package xwh.lib.music.player;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;

/**
 * Created by xwh on 2019/6/26.
 */
public class MusicPlayer {
	private static final String TAG = "MusicPlayer";
	private static MediaPlayer mPlayer;
	private int errorCount;
	private MusicListener mListener;

	private void init() {
		mPlayer = new MediaPlayer();
		mPlayer.setOnPreparedListener(mp -> {
			Log.d(TAG, "onPrepared:" + mp.getDuration());
			play();

			if (mp.getDuration() > 0 && mp.getDuration() < 60 * 60 * 1000) { // 可能播放失败之后也回调，而且返回一个很大的值1675999624 1676085640
				errorCount = 0;
			}

			if (mListener != null) {
				mListener.onPrepared(mp.getDuration());
			}
		});
		mPlayer.setOnCompletionListener(mp -> {
			Log.d(TAG, "onCompletion:" + mp.getDuration());
			// 播放完成后自动下一首
			if (mp.getDuration() > 0 && mp.getDuration() < 60 * 60 * 1000) { // 可能播放失败之后也回调，而且返回一个很大的值1675999624 1676085640
				//next();
				if (mListener != null) {
					mListener.onCompletion();
				}
			}
		});
		mPlayer.setOnErrorListener((MediaPlayer mp, int what, int extra) -> {
			Log.e(TAG, "onError:" + what + ", " + extra + ", " + errorCount);
			if (what == 1 && extra == -2147483648) {
				errorCount++;
				if (errorCount >= 3) { // 未知系统错误。1, -2147483648 MediaPlayer.MEDIA_ERROR_SYSTEM
					errorCount = 0;
					return true;
				}
				/*if (!mPlayer.datasou.startsWith("http")) { // 本地歌曲出错，再重试一遍
					mp.reset();
					try {
						mPlayer.setDataSource(path);
					} catch (IOException e) {
						e.printStackTrace();
					}
					mPlayer.prepareAsync();
					return true;
				}*/
			}

			if (mListener != null) {
				mListener.onError();
			}

			mp.reset();
			return false;
		});
	}

	public void start(final String path) {
		try {
			if (mPlayer == null) {
				init();
			} else {
				mPlayer.reset();
			}

			Log.d(TAG, "start play: " + path);

			mPlayer.setDataSource(path);
			mPlayer.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void play(){
		if (mPlayer != null) {
			volumeSmoothUp();
			if (mListener != null) {
				mListener.onPlay();
			}
		}
	}

	public void pause(){
		if (mPlayer != null) {
			if (mListener != null) {
				mListener.onPause();
			}
			volumeSmoothDown();
		}
	}

	/**
	 * 声音渐强
	 */
	public void volumeSmoothUp() {
		long duration = 1000;   // 计时器时长

		mPlayer.setVolume(0, 0);    // 声音渐强效果。
		mPlayer.start();

		new CountDownTimer(duration, duration / 10) {
			float volume;
			@Override
			public void onTick(long millisUntilFinished) {
				volume = 1f - millisUntilFinished * 1f / duration;
				mPlayer.setVolume(volume, volume);
			}

			@Override
			public void onFinish() {
				mPlayer.setVolume(1f, 1f);  // 恢复原始值
			}
		}.start();
	}

	/**
	 * 声音渐弱
	 */
	public void volumeSmoothDown() {
		long duration = 1000;   // 计时器时长
		new CountDownTimer(duration, duration / 10) {
			@Override
			public void onTick(long millisUntilFinished) {
				float volume = millisUntilFinished * 1f / duration;
				mPlayer.setVolume(volume, volume);
			}

			@Override
			public void onFinish() {
				mPlayer.pause();
				mPlayer.setVolume(1f, 1f);  // 恢复原始值
			}
		}.start();
	}

	public void seekTo(int position) {
		mPlayer.seekTo(position);
	}

	public boolean isPlaying() {
		boolean isPlaying = false;
		try {
			isPlaying = mPlayer != null && mPlayer.isPlaying();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isPlaying;
	}

	public long getPosition() {
		return mPlayer == null ? 0 : mPlayer.getCurrentPosition();
	}

	public void setMusicListener(MusicListener listener) {
		this.mListener = listener;
	}

	public void release() {
		if (mPlayer != null) {
			mPlayer.reset();
			mPlayer.release();
			//mPlayer = null;
			mListener = null;
		}
	}

}
