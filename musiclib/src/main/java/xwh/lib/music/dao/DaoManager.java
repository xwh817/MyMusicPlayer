package xwh.lib.music.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.query.QueryBuilder;

import xwh.lib.music.entity.DaoMaster;
import xwh.lib.music.entity.DaoSession;

public class DaoManager {

    private static DaoSession daoSession;

    /**
     * 初始化GreenDao
     */
    public static void initGreenDao(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "MyMusicPlayer.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }


    //是否开启Log
    public void setDebugMode(boolean flag) {
        //MigrationHelper.DEBUG = true;//如果查看数据库更新的Log，请设置为true
        QueryBuilder.LOG_SQL = flag;
        QueryBuilder.LOG_VALUES = flag;

    }

}
