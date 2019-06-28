package xwh.lib.music.player;

/**
 * Created by xwh on 2019/6/26.
 */
public interface MusicListener {
	void onPrepared(long duration);
	void onPlay();
	void onPause();
	void onCompletion();
	void onError();
}
