package com.kosbrother.realestate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.kosbrother.realestate.data.DatabaseHelper;
import com.kosbrother.realestate.fragment.DetailFragment;

public class DetailActivity extends SherlockFragmentActivity
{
	int NUM_ITEMS;
	MyAdapter mAdapter;
	ViewPager mPager;
	private DatabaseHelper databaseHelper = null;
	private ActionBar mActionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pager);
		Bundle bundle = getIntent().getExtras();
		int position = bundle.getInt("ItemPosition");

		NUM_ITEMS = Datas.mEstates.size();

		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mPager.setCurrentItem(position);

		mActionBar = getSupportActionBar();

		// enable ActionBar app icon to behave as action to toggle nav drawer
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
		mActionBar.setTitle("第" + Integer.toString(position + 1) + "/"
				+ Integer.toString(NUM_ITEMS) + "筆資料");

		mPager.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int current_position)
			{
				// TODO Auto-generated method stub
				Log.i("DetailActivity", "current_position = " + current_position);
				mActionBar.setTitle("第" + Integer.toString(current_position + 1) + "/"
						+ Integer.toString(NUM_ITEMS) + "筆資料");
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0)
			{
				// TODO Auto-generated method stub

			}
		});
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
	protected void onDestroy()
	{
		super.onDestroy();

		/*
		 * You'll need this in your class to release the helper when done.
		 */
		if (databaseHelper != null)
		{
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
	}

	/**
	 * You'll need this in your class to get the helper from the manager once
	 * per class.
	 */
	public DatabaseHelper getHelper()
	{
		if (databaseHelper == null)
		{
			databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		}
		return databaseHelper;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			finish();
			return true;
		case R.id.menu_up:
			if (mPager.getCurrentItem()>0)
			{
				mPager.setCurrentItem(mPager.getCurrentItem()-1);
			}else{
				mPager.setCurrentItem(NUM_ITEMS-1);
			}	
			return true;
		case R.id.menu_down:
			if (mPager.getCurrentItem() < NUM_ITEMS-1)
			{
				mPager.setCurrentItem(mPager.getCurrentItem()+1);
			}else {
				mPager.setCurrentItem(0);
			}
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		getSupportMenuInflater().inflate(R.menu.detail, menu);

		return super.onCreateOptionsMenu(menu);
	}

}
