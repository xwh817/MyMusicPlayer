package xwh.player.music;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    protected BaseActivity mContext;
    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(getLayoutRes());
        /*boolean bindView = setContentView();    // 子页面是否需要ButterKnife绑定
        if (bindView) {
            mUnbinder = ButterKnife.bind(this);
        }*/

        mUnbinder = ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        mContext = null;
    }

/*
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }*/

    /**
     * 设置界面内容
     * @return 是否需要ButterKnife进行绑定。
     */
    //protected abstract boolean setContentView();
    protected abstract int getLayoutRes();

    /**
     * 初始化界面
     */
    protected abstract void initView();

}
