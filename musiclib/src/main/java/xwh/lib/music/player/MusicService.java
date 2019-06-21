package xwh.lib.music.player;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * Created by xwh on 2019/6/17.
 */
public class MusicService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return new MusicBinder();
	}


	//该方法包含关于歌曲的操作
	public class MusicBinder extends Binder {

		//判断是否处于播放状态
		public boolean isPlaying(){
			return MusicManager.getInstance().isPlaying();
		}

		//播放或暂停歌曲
		public void play() {
			MusicManager.getInstance().play();
		}

		//返回歌曲的长度，单位为毫秒
		public long getDuration(){
			return MusicManager.getInstance().getDuration();
		}

	}

}