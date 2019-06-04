package xwh.player.music.adapter;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

/**
 * Created by xwh on 2019/6/3.
 */
public class HomeViewPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments;

	public HomeViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.mFragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}


}
