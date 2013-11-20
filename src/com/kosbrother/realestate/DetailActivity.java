package com.kosbrother.realestate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.kosbrother.realestate.fragment.DetailFragment;

public class DetailActivity extends SherlockFragmentActivity
{
	int NUM_ITEMS;

	MyAdapter mAdapter;

	ViewPager mPager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pager);
		NUM_ITEMS = Datas.mEstates.size();

		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		// // Watch for button clicks.
		// Button button = (Button)findViewById(R.id.goto_first);
		// button.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// mPager.setCurrentItem(0);
		// }
		// });
		// button = (Button)findViewById(R.id.goto_last);
		// button.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// mPager.setCurrentItem(NUM_ITEMS-1);
		// }
		// });
	}

	public class MyAdapter extends FragmentStatePagerAdapter
	{
		public MyAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public int getCount()
		{
			return NUM_ITEMS;
		}

		@Override
		public Fragment getItem(int position)
		{
			return DetailFragment.newInstance(position);
		}
	}

}
