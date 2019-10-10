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

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import xwh.lib.music.player.MusicService;
import xwh.player.music.adapter.HomeViewPagerAdapter;
import xwh.player.music.constant.Tags;
import xwh.player.music.fragment.HistoryListFragment;
import xwh.player.music.fragment.PlayListFragment;
import xwh.player.music.fragment.SearchFragment;
import xwh.player.music.fragment.SongListFragment;

public class MainActivity extends BaseActivity {
	@BindView(R.id.viewPager)
	ViewPager mViewPager;
	@BindView(R.id.tabLayout)
	TabLayout mTabLayout;
	private HomeViewPagerAdapter mPagerAdapter;

	private int colorSelected;
	private int colorUnSelected;

	private static final int DEFAULT_INDEX = 0;	// 默认选中的页面index

	int[] texts = {
			R.string.tab_recommend,
			R.string.tab_song_list,
			R.string.tab_search,
			R.string.tab_history};
	int[] icons = {
			R.drawable.tab_recommend,
			R.drawable.tab_list,
			R.drawable.tab_search,
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
		colorSelected = ContextCompat.getColor(this, R.color.colorSelected);
		colorUnSelected = ContextCompat.getColor(this, R.color.colorUnSelected);
		initViewPager();
		initTabLayout();
	}

	private void initViewPager() {
		List<Fragment> fragments = new ArrayList<>();
		fragments.add(new PlayListFragment());
		fragments.add(new SongListFragment());
		fragments.add(new SearchFragment());
		fragments.add(new HistoryListFragment());

		mPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager(), fragments);
		mViewPager.setAdapter(mPagerAdapter);

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			private ArgbEvaluator evaluator;
			private boolean isDragging = false;
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				Log.d("ViewPager", position + "," + positionOffset + ", " + positionOffsetPixels);

				// 如果是拖动状态，才去处理滑动渐变效果
				if (isDragging) {
					if (evaluator == null) {
						evaluator = new ArgbEvaluator();
					}

					Tab leftTab = mTabLayout.getTabAt(position);
					int colorLeft = (Integer) evaluator.evaluate(positionOffset, colorSelected, colorUnSelected);
					setTabColor(leftTab, colorLeft);

					if (position + 1 < mTabLayout.getTabCount()) {
						Tab rightTab = mTabLayout.getTabAt(position + 1);
						int colorRight = (Integer) evaluator.evaluate(positionOffset, colorUnSelected, colorSelected);
						setTabColor(rightTab, colorRight);
					}
				}

			}

			@Override
			public void onPageSelected(int position) {
				Log.d("ViewPager", "onPageSelected: " + position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
				if (state == ViewPager.SCROLL_STATE_DRAGGING) {
					isDragging = true;
				} else if (isDragging && state == ViewPager.SCROLL_STATE_IDLE) {
					isDragging = false;
				}

				Log.d("ViewPager", "onPageScrollStateChanged: " + state);
			}
		});
	}

	private void initTabLayout() {
		mTabLayout.setTabMode(TabLayout.MODE_FIXED);
		mTabLayout.setupWithViewPager(mViewPager);      // 关联ViewPager
		//mTabLayout.setTabIconTint(mTabLayout.getTabTextColors());   // 图标和文字颜色一致
		for (int i = 0; i < texts.length; i++) {
			Tab tab = mTabLayout.getTabAt(i);
			tab.setText(texts[i]).setIcon(icons[i]);     // 不要自己去newTab了，绑定ViewPager之后会自动生成。

			// 初始颜色
			setTabColor(tab, i==DEFAULT_INDEX ? colorSelected : colorUnSelected);
		}

		mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(Tab tab) {
				setTabColor(tab, colorSelected);
			}

			@Override
			public void onTabUnselected(Tab tab) {
				setTabColor(tab, colorUnSelected);
			}

			@Override
			public void onTabReselected(Tab tab) {

			}
		});

	}


	private void setTabColor(Tab tab, int color) {
		this.setViewGroupColor(tab.view, color);
	}

	/**
	 * 设置Tab颜色，这里使用了递归去寻找ViewGroup下面的TextView和ImageView，去修改其颜色。
	 * @param parent
	 * @param color
	 */
	private void setViewGroupColor(ViewGroup parent, int color) {
		for(int i=0; i<parent.getChildCount(); i++) {
			View child = parent.getChildAt(i);
			if (child instanceof ViewGroup) {
				setViewGroupColor((ViewGroup)child, color);
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
			Log.d(Tags.MUSIC, "onServiceConnected");
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
