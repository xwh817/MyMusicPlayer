package xwh.lib.music.dao;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import xwh.lib.music.entity.Song;
import xwh.lib.music.event.HistoryEvent;

public class HistoryDao {

    public static void add(Song song) {
        DaoManager.getDaoSession().getSongDao().insertOrReplace(song);

        EventBus.getDefault().post(song);
    }


    public static void delete(Song song) {
        DaoManager.getDaoSession().getSongDao().delete(song);
    }


    public static void deleteAll() {
        DaoManager.getDaoSession().getSongDao().deleteAll();
    }


    public static List<Song> getAll() {
        return DaoManager.getDaoSession().getSongDao().loadAll();
    }

}
