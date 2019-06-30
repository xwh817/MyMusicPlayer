package xwh.lib.music.api.music163;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import xwh.lib.music.entity.Song;

/**
 * Created by xwh on 2019/6/4.
 */
public class JsonUtil {

	public static Song getSongFromJson(JSONObject json) {
		Song song = new Song();
		try {
			song.setId(json.getInt("id"));
			song.setName(json.getString("name"));
			song.setDuration(json.optInt("duration"));
			if (song.getDuration() == 0) {
				song.setDuration(json.optInt("dt"));
			}
			JSONObject album = json.optJSONObject("album");
			if (album == null) {
				album = json.optJSONObject("al");
			}
			if (album != null) {
				song.setAlbum(album.optInt("id"));
				if (album.has("picUrl")){
					song.setCover(album.getString("picUrl"));
				} else {
					song.setCover(album.optString("img1v1Url"));
				}
			}
			JSONArray artists = json.optJSONArray("artists");
			if (artists == null) {
				artists = json.optJSONArray("ar");
			}
			if (artists != null) {
				StringBuilder names = new StringBuilder();
				for(int i=0; i<artists.length(); i++) {
					JSONObject aJson = artists.getJSONObject(i);
					names.append(aJson.optString("name", "")).append(" ");
				}
				song.setArtist(names.toString());
			}
			song.setUrl("https://music.163.com/song/media/outer/url?id=" + song.getId() + ".mp3 ");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return song;
	}

	public static List<Song> getSongListFromJson(String str) throws JSONException {
		JSONObject json = new JSONObject(str);
		return getSongListFromJson(json);
	}

	public static List<Song> getSongListFromJson(JSONObject json) {
		List<Song> list = new ArrayList<>();
		try {
			JSONArray array = json.getJSONObject("playlist").getJSONArray("tracks");
			for (int i = 0; i < array.length(); i++) {
				Song song = getSongFromJson(array.getJSONObject(i));
				if (song.getId() > 0) {
					list.add(song);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
