package xwh.player.music;

import android.app.Application;
import android.content.Context;

import xwh.lib.music.dao.DaoManager;

public class MusicApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // 生成Traceview的日志文件，记录每个方法的执行时间
        //Debug.startMethodTracing();
        //Debug.startMethodTracing("/data/data/xwh.player.music/launcher.trace");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initLibs();

        //Debug.stopMethodTracing();
    }

    private void initLibs() {

        // 初始化数据库
        DaoManager.initGreenDao(this);

        /*// 模拟卡顿
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }

}
