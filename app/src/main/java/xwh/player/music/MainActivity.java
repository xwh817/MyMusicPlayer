package xwh.player.music;

import android.animation.ArgbEvaluator;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.Tab;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import xwh.lib.music.player.MusicService;
import xwh.player.music.adapter.HomeViewPagerAdapter;
import xwh.player.music.constant.TagConstant;
import xwh.player.music.fragment.RecommendFragment;
import xwh.player.music.fragment.SongListFragment;

public class MainActivity extends BaseActivity {
	@BindView(R.id.viewPager)
	ViewPager mViewPager;
	@BindView(R.id.tabLayout)
	TabLayout mTabLayout;
	private HomeViewPagerAdapter mPagerAdapter;

	int[] texts = {
			R.string.tab_recommend,
			R.string.tab_song_list,
			R.string.tab_love,
			R.string.tab_history};
	int[] icons = {
			R.drawable.tab_recommend,
			R.drawable.tab_list,
			R.drawable.tab_love,
			R.drawable.tab_history};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		connectService();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(mServiceConnection);
	}

	@Override
	protected int getLayoutRes() {
		return R.layout.activity_main;
	}

	@Override
	protected void initView() {
		initViewPager();
		initTabLayout();
	}

	private void initViewPager() {
		List<Fragment> fragments = new ArrayList<>();
		fragments.add(new RecommendFragment());
		fragments.add(new SongListFragment());
		fragments.add(new RecommendFragment());
		fragments.add(new RecommendFragment());

		mPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mPagerAdapter);

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			private ArgbEvaluator evaluator;
			private int colorSelected = mContext.getResources().getColor(R.color.colorSelected);
			private int colorUnSelected = mContext.getResources().getColor(R.color.colorUnSelected);
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				Log.d("ViewPager", position + "," + positionOffset + ", " + positionOffsetPixels);

				if (evaluator == null) {
					evaluator = new ArgbEvaluator();
				}

				ViewGroup leftTab = mTabLayout.getTabAt(position).view;
				int colorLeft = (Integer) evaluator.evaluate(positionOffset, colorSelected, colorUnSelected);
				changeTabColor(leftTab, colorLeft);

				if (position + 1 < mTabLayout.getTabCount()) {
					ViewGroup rightTab = mTabLayout.getTabAt(position + 1).view;
					int colorRight = (Integer) evaluator.evaluate(positionOffset, colorUnSelected, colorSelected);
					changeTabColor(rightTab, colorRight);
				}

			}

			@Override
			public void onPageSelected(int position) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	private void initTabLayout() {
		mTabLayout.setTabMode(TabLayout.MODE_FIXED);
		mTabLayout.setupWithViewPager(mViewPager);      // 关联ViewPager
		mTabLayout.setTabIconTint(mTabLayout.getTabTextColors());   // 图标和文字颜色一致
		for (int i = 0; i < texts.length; i++) {
			Tab tab = mTabLayout.getTabAt(i);
			tab.setText(texts[i]).setIcon(icons[i]);     // 不要自己去newTab了，绑定ViewPager之后会自动生成。
			//Tab tab = mTabLayout.newTab().setText(texts[i]).setIcon(icons[i]);
			//mTabLayout.addTab(tab);
		}

	}

	private void changeTabColor(ViewGroup parent, int color) {
		for(int i=0; i<parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);
			if (child instanceof ViewGroup) {
				changeTabColor((ViewGroup)child, color);
			} else if(child instanceof ImageView) {
				((ImageView)child).setColorFilter(color);
			} else if(child instanceof TextView) {
				((TextView)child).setTextColor(color);
			}
		}
	}



	private ServiceConnection mServiceConnection;
	private MusicService.MusicBinder mMusicBinder;

	private class MusicServiceConnection implements ServiceConnection {

		//服务启动完成后会进入到这个方法
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			//获得service中的MyBinder
			mMusicBinder = (MusicService.MusicBinder) service;
			Log.d(TagConstant.MUSIC, "onServiceConnected");
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	}

	private void connectService() {
		Intent intent = new Intent(this, MusicService.class);
		//使用混合的方法开启服务，
		startService(intent);
		mServiceConnection = new MusicServiceConnection();
		bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
	}

}
