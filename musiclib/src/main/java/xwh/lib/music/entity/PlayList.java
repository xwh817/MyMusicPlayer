package xwh.lib.music.entity;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xwh on 2019/9/9.
 * 歌单
 */
public class PlayList implements Serializable {
	private int id;
	private String name;
	private String coverImgUrl;
	private String description;
	private List<Song> songs;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCoverImgUrl(){
		return getCoverImgUrl(320);
	}
	public String getCoverImgUrl(int imgSize) {
		return coverImgUrl+ "?param="+imgSize+"y"+imgSize;
	}

	public List<Song> getSongs() {
		return songs;
	}

	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}

	public String getDescription() {
		return description;
	}

	public static PlayList getPlayListFromJson(JSONObject json) {
		PlayList playList = new PlayList();
		playList.id = json.optInt("id");
		playList.name = json.optString("name");
		playList.coverImgUrl = json.optString("coverImgUrl");
		playList.description = json.optString("description");
		return playList;
	}

	public static List<PlayList> getAllPlayList(String strJson) {
		List<PlayList> allList = new ArrayList<>();
		try {
			JSONObject json = new JSONObject(strJson);
			JSONArray playlists = json.getJSONArray("playlists");
			for(int i=0; i<playlists.length(); i++) {
				JSONObject item = playlists.getJSONObject(i);
				PlayList playList = getPlayListFromJson(item);
				if (playList.id > 0 && !TextUtils.isEmpty(playList.name)) {
					allList.add(playList);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return allList;
	}

}
