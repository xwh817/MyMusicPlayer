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
			song.id = json.getInt("id");
			song.name = json.getString("name");
			song.duration = json.optInt("duration");
			if (song.duration == 0) {
				song.duration = json.optInt("dt");
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
				song.artist = names.toString();
			}
			song.url = "https://music.163.com/song/media/outer/url?id=" + song.id + ".mp3 ";
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
				if (song.id > 0) {
					list.add(song);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
