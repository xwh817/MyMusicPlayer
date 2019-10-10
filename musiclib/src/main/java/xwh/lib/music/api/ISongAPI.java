package xwh.lib.music.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by xwh on 2019/6/4.
 */
public interface ISongAPI {

	@GET("top/list")
	Observable<String> getTopList(@Query("idx") int listId);

	@GET("top/playlist/highquality")
	Observable<String> getAllPlayList();

	@GET("/playlist/detail")
	Observable<String> getPlayListDetail(@Query("id") int playListId);

	@GET("/search")
	Observable<String> search(@Query("keywords") String keywords);

	@GET("/song/detail")
	Observable<String> getSongDetail(@Query("ids") String ids);


}
