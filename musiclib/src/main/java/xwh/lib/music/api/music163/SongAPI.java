package xwh.lib.music.api.music163;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import xwh.lib.music.api.ISongAPI;
import xwh.lib.music.entity.PlayList;
import xwh.lib.music.entity.Song;

/**
 * Created by xwh on 2019/6/4.
 */
public class SongAPI {
	private static final String mBaseUrl = "http://music.turingmao.com/";
	//private static final String mBaseUrl = "http://172.16.45.58:20195/";
	private Retrofit mRetrofit;

	private static SongAPI mInstance;
	private ISongAPI mISongAPI;

	public static SongAPI getInstance() {
		if (mInstance == null) {
			synchronized (SongAPI.class) {
				if (mInstance == null) {
					mInstance = new SongAPI();
				}
			}
		}
		return mInstance;
	}

	private SongAPI() {
		init();
	}

	private void init() {
		mRetrofit = new Retrofit.Builder()
				.baseUrl(mBaseUrl)
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.addConverterFactory(ScalarsConverterFactory.create())
				.build();
		mISongAPI = mRetrofit.create(ISongAPI.class);
	}

	public void getTopList(int listId, Consumer<List<Song>> consumer) {
		mRetrofit.create(ISongAPI.class)
				.getTopList(listId)
				.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
				.map(str -> JsonUtil.getSongListFromJson(str))
				.subscribe(
						consumer,
						throwable -> {
							consumer.accept(null);
							throwable.printStackTrace();
						});
	}

	public void getPlayList(Consumer<List<PlayList>> consumer) {
		mRetrofit.create(ISongAPI.class)
				.getAllPlayList()
				.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
				.map(str -> PlayList.getAllPlayList(str))
				.subscribe(
						consumer,
						throwable -> {
							consumer.accept(null);
							throwable.printStackTrace();
						});
	}


	public void getPlayListDetail(PlayList playList, Consumer<PlayList> consumer) {
		mRetrofit.create(ISongAPI.class)
				.getPlayListDetail(playList.getId())
				.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
				.map(str -> JsonUtil.getSongListFromJson(str))
				.map(songs -> { playList.setSongs(songs);return playList;})
				.subscribe(
						consumer,
						throwable -> {
							consumer.accept(null);
							throwable.printStackTrace();
						});
	}
}
