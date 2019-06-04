package xwh.player.music;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayout.Tab;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import xwh.player.music.adapter.HomeViewPagerAdapter;
import xwh.player.music.fragment.RecommendFragment;
import xwh.player.music.fragment.SongListFragment;

public class MainActivity extends AppCompatActivity {
	private Unbinder mUnbinder;
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
		setContentView(R.layout.activity_main);
		mUnbinder = ButterKnife.bind(this);
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


	@Override
	protected void onDestroy() {
		super.onDestroy();
		mUnbinder.unbind();
	}
}
