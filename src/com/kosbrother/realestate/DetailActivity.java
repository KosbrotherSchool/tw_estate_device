package com.kosbrother.realestate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.kosbrother.realestate.data.DatabaseHelper;
import com.kosbrother.realestate.fragment.DetailFragment;

public class DetailActivity extends SherlockFragmentActivity
{
	int NUM_ITEMS;
	MyAdapter mAdapter;
	ViewPager mPager;
	private DatabaseHelper databaseHelper = null;

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
//		Button button = (Button) findViewById(R.id.goto_first);
//		button.setOnClickListener(new OnClickListener()
//		{
//			public void onClick(View v)
//			{
//				mPager.setCurrentItem(0);
//			}
//		});
//		button = (Button) findViewById(R.id.goto_last);
//		button.setOnClickListener(new OnClickListener()
//		{
//			public void onClick(View v)
//			{
//				mPager.setCurrentItem(NUM_ITEMS - 1);
//			}
//		});
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
			return DetailFragment.newInstance(position, DetailActivity.this);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		/*
		 * You'll need this in your class to release the helper when done.
		 */
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}
	
	/**
	 * You'll need this in your class to get the helper from the manager once per class.
	 */
	public DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		}
		return databaseHelper;
	}
	

}
