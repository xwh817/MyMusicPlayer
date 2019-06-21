package xwh.player.music.adapter;

import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
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
		Log.d("Fragment", "HomeViewPagerAdapter getItem " + position);
		return mFragments.get(position);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		//super.destroyItem(container, position, object);
		Log.d("Fragment", "HomeViewPagerAdapter destroyItem " + position);
		/**
		 * 这里不使用父类的detach((Fragment)object);
		 * 我们自己已经对数据进行缓存了。不需要释放。
		 * 不然，LeakCanary会弹出内存泄漏的问题。
		 */

	}
}
