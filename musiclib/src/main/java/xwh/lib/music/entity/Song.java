package xwh.lib.music.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

@Entity(nameInDb="History")
public class Song implements Serializable {
    public static final long serialVersionUID = 1L;
    @Id
    private long id;
    private String name;
    private String artist;
    private int album;
    private int duration;
    private String cover;
    private String url;
    @Property(nameInDb = "create_time")
    private long lastPlayTime;
    @Generated(hash = 387141696)
    public Song(long id, String name, String artist, int album, int duration,
            String cover, String url, long lastPlayTime) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.cover = cover;
        this.url = url;
        this.lastPlayTime = lastPlayTime;
    }
    @Generated(hash = 87031450)
    public Song() {
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getArtist() {
        return this.artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public int getAlbum() {
        return this.album;
    }
    public String getCover(int imgSize) {
        return cover+ "?param="+imgSize+"y"+imgSize;    // 根据控件大小加载图片。
    }
    public void setAlbum(int album) {
        this.album = album;
    }
    public int getDuration() {
        return this.duration;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public String getCover() {
        return this.cover;
    }
    public void setCover(String cover) {
        this.cover = cover;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public long getLastPlayTime() {
        return this.lastPlayTime;
    }
    public void setLastPlayTime(long lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }
}
