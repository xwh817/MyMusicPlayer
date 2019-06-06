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
}