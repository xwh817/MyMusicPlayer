package xwh.lib.music.player;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.util.Log;

import java.io.IOException;

import xwh.lib.music.entity.Song;

/**
 * Created by xwh on 2019/5/10.
 */
public class MusicManager {
	private static final String TAG = "Music";
	private static MusicManager instance;
	private static MediaPlayer mPlayer;
	private int currentIndex;
	private Song currentSong;
	private int errorCount;

	public static synchronized MusicManager getInstance() {
		if (instance == null) {
			instance = new MusicManager();
		}
		return instance;
	}

	public void start(final String path) {
		try {
			if (mPlayer == null) {
				mPlayer = new MediaPlayer();
				mPlayer.setOnPreparedListener(mp -> {
					Log.d(TAG, "onPrepared:" + mp.getDuration());
					mPlayer.start();

					if (mp.getDuration() > 0 && mp.getDuration() < 60 * 60 * 1000) { // 可能播放失败之后也回调，而且返回一个很大的值1675999624 1676085640
						errorCount = 0;
					}
				});
				mPlayer.setOnCompletionListener(mp -> {
						Log.d(TAG, "onCompletion:" + mp.getDuration());
						// 播放完成后自动下一首
						if (mp.getDuration() > 0 && mp.getDuration() < 60 * 60 * 1000) { // 可能播放失败之后也回调，而且返回一个很大的值1675999624 1676085640
							next();
						}
				});
				mPlayer.setOnErrorListener((MediaPlayer mp, int what, int extra) -> {
						Log.e(TAG, "onError:" + what + ", " + extra + ", " + errorCount);
						if (what == 1 && extra == -2147483648) {
							errorCount++;
							if (errorCount >= 3) { // 未知系统错误。1, -2147483648 MediaPlayer.MEDIA_ERROR_SYSTEM
								errorCount = 0;
								//killSelf();
								return true;
							}
							if (!path.startsWith("http")) { // 本地歌曲出错，再重试一遍
								mp.reset();
								try {
									mPlayer.setDataSource(path);
								} catch (IOException e) {
									e.printStackTrace();
								}
								mPlayer.prepareAsync();
								return true;
							}
						}

						mp.reset();
						return false;
				});
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


	public void start(Song song) {
		currentSong = song;
		/*// 先从缓存取
		String localPath = FileCache.getSongFromCache(currentSong.id);

		if (localPath == null) { // 没有缓存，播放网络url
			startCache(currentSong, 5000);
			start(currentSong.url);
		} else {
			if (!DownloadTask.isTaskRunning(currentSong.id)) {  // 下载正在进行，等待完成
				start(localPath);
			}
		}*/

		start(currentSong.url);

		if (mMusicListener != null) {
			mMusicListener.onPlay();
		}
	}


	// 缓存到本地
	private void startCache(final Song song, long delay) {
		/*ThreadPoolManager.getInstance().addTask(new DownloadTask(song, delay) {
			@Override
			protected void onResult(Boolean success) {
				if (!mPlayer.isPlaying() && success && !isPausedByUser) {
					start(song);
				}
			}
		});*/
	}

	// 预缓存下一首
	public void nextCache() {
		/*Song next = getNextSong();
		if (next != null) {
			if (DownloadTask.isTaskRunning(next.id)) {  // 正在缓存
				return;
			}
			if (FileCache.getSongFromCache(next.id) != null) {   // 已经缓存
				return;
			}
			startCache(next, 0);
		}*/
	}

	public void play() {
		play(currentIndex);
	}

	public void play(int index) {
		if (SongList.sSongList.size() > 0) {
			currentIndex = index;
			currentSong = SongList.sSongList.get(currentIndex);
		}
		if (currentSong != null) {
			start(currentSong);
		}
	}

	public void pause() {
		if (mPlayer != null && mPlayer.isPlaying()) {
			pauseOrPlay();
		}
	}

	public boolean pauseOrPlay() {
		final boolean isPlaying;
		if (mPlayer == null) {
			isPlaying = true;
			play();
		} else {
			isPlaying = !mPlayer.isPlaying();

			if (mMusicListener != null) {
				if (isPlaying) {
					mPlayer.setVolume(0, 0);
					mPlayer.start();
					mMusicListener.onPlay();
				} else {
					mMusicListener.onPause();
				}
			}

			// 渐强减弱
			final long duration = isPlaying ? 1200 : 1000;
			long interval = duration / 10;
			new CountDownTimer(duration, interval) {
				@Override
				public void onTick(long millisUntilFinished) {
					float volume;
					if (isPlaying) {
						volume = 1f - millisUntilFinished * 1f / duration;
					} else {
						volume = millisUntilFinished * 1f / duration;
					}
					mPlayer.setVolume(volume, volume);
					//Log.d(TAG, "volume:" + volume);
				}

				@Override
				public void onFinish() {
					if (!isPlaying) {
						mPlayer.pause();
					}
					mPlayer.setVolume(1f, 1f);
				}
			}.start();
		}

		return isPlaying;
	}

	public long getDuration() {
		Song song = getCurrentSong();
		return song == null ? 0 : song.duration;
	}

	public void stop() {
		if (mPlayer != null) {
			mPlayer.reset();
			mPlayer.release();
			mPlayer = null;

			if (mMusicListener != null) {
				mMusicListener.onPause();
			}

		}
	}


	public int getCurrentIndex() {
		return currentIndex;
	}

	public Song getCurrentSong() {
		if (currentSong == null && SongList.sSongList.size() > 0) {
			currentSong = SongList.sSongList.get(currentIndex);
		}
		return currentSong;
	}

	public void prev() {
		if (SongList.sSongList.size() > 0) {
			currentIndex--;
			if (currentIndex < 0) {
				currentIndex = SongList.sSongList.size() - 1;
			}

			play();
		}
	}

	public void next() {
		if (SongList.sSongList.size() > 0) {
			currentIndex++;
			if (currentIndex >= SongList.sSongList.size()) {
				currentIndex = 0;
			}

			play();

		}
	}

	public Song getNextSong() {
		Song next = null;
		if (SongList.sSongList.size() > 0) {
			int index = currentIndex + 1;
			if (index >= SongList.sSongList.size()) {
				index = 0;
			}
			next = SongList.sSongList.get(index);
		}
		return next;
	}

	public MediaPlayer getPlayer() {
		return mPlayer;
	}

	public boolean isPlaying() {
		return mPlayer != null && mPlayer.isPlaying();
	}

	private MusicListener mMusicListener;

	public void setMusicListener(MusicListener listener) {
		this.mMusicListener = listener;
	}

	public interface MusicListener {
		void onPlay();

		void onPause();
	}

}
