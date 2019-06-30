package xwh.lib.music.player;

import xwh.lib.music.dao.HistoryDao;
import xwh.lib.music.entity.Song;

/**
 * Created by xwh on 2019/5/10.
 */
public class MusicManager {
	private static final String TAG = "Music";
	private volatile static MusicManager instance;
	private static MusicPlayer mPlayer;
	private int currentIndex;
	private Song currentSong;
	private int errorCount;

	public static synchronized MusicManager getInstance() {
		if (instance == null) {
			synchronized (MusicManager.class) {
				if (instance == null) {
					instance = new MusicManager();
				}
			}
		}
		return instance;
	}

	private MusicManager() {
		mPlayer = new MusicPlayer();
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

		mPlayer.start(currentSong.getUrl());

		HistoryDao.add(currentSong);
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
		mPlayer.pause();
	}

	public boolean pauseOrPlay() {
		final boolean isPlaying;
		if (mPlayer == null) {
			isPlaying = true;
			play();
		} else {
			isPlaying = !mPlayer.isPlaying();
			if (isPlaying) {
				mPlayer.play();
			} else {
				mPlayer.pause();
			}

		}

		return isPlaying;
	}

	public long getDuration() {
		Song song = getCurrentSong();
		return song == null ? 0 : song.getDuration();
	}

	public long getPosition() {
		return mPlayer.getPosition();
	}


	public void release() {
		mPlayer.release();
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


	public boolean isPlaying() {
		return mPlayer != null && mPlayer.isPlaying();
	}


	public void setMusicListener(MusicListener listener) {
		mPlayer.setMusicListener(listener);
	}


	public void seekTo(int progress) {
		mPlayer.seekTo(progress);
	}
}
