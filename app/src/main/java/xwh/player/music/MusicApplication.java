package xwh.player.music;

import android.app.Application;

import xwh.lib.music.dao.DaoManager;

public class MusicApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化数据库
        DaoManager.initGreenDao(this);
    }
}
