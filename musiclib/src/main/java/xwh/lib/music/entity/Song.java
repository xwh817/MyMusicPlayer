package xwh.lib.music.entity;

import java.io.Serializable;

/**
 * Created by xwh on 2019/6/4.
 */
public class Song implements Serializable {
	public int id;
	public String name;
	public String artist;
	public int album;
	public int duration;
	public String cover;
	public String url;

	public String getCover(int imgSize) {
		return cover+ "?param="+imgSize+"y"+imgSize;    // 根据控件大小加载图片。
	}

	@Override
	public String toString() {
		return this.name;
	}
}
