package xwh.lib.music.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import xwh.lib.music.entity.DaoMaster;
import xwh.lib.music.entity.DaoSession;

public class DaoManager {

    /**
     * 初始化GreenDao
     */
    public static void initGreenDao(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "MyMusicPlayer.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    private static DaoSession daoSession;
    public static DaoSession getDaoSession() {
        return daoSession;
    }



}
