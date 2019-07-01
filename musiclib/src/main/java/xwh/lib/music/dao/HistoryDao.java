package xwh.lib.music.dao;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import xwh.lib.music.entity.Song;
import xwh.lib.music.entity.SongDao;
import xwh.lib.music.event.HistoryEvent;

public class HistoryDao {

    public static void add(Song song) {
        song.setLastPlayTime(System.currentTimeMillis());
        DaoManager.getDaoSession().getSongDao().insertOrReplace(song);
        EventBus.getDefault().post(song);   // EventBus通知更新
    }


    public static void delete(Song song) {
        DaoManager.getDaoSession().getSongDao().delete(song);
    }


    public static void deleteAll() {
        DaoManager.getDaoSession().getSongDao().deleteAll();
    }


    public static List<Song> getAll() {
        QueryBuilder<Song> queryBuilder = DaoManager.getDaoSession().queryBuilder(Song.class)
            .orderDesc(SongDao.Properties.LastPlayTime) // 获取字段名
            .limit(100);
        return queryBuilder.list();
    }

}
