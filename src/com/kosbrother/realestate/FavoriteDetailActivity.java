package com.kosbrother.realestate;

import java.sql.SQLException;
import java.util.ArrayList;

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
import com.google.analytics.tracking.android.EasyTracker;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.kosbrother.realestate.data.DatabaseHelper;
import com.kosbrother.realestate.data.OrmRealEstate;
import com.kosbrother.realestate.fragment.DetailFavoriteFragment;

public class FavoriteDetailActivity extends SherlockFragmentActivity
{
	int NUM_ITEMS = 0;
	MyAdapter mAdapter;
	ViewPager mPager;
	private DatabaseHelper databaseHelper = null;
	public ArrayList<OrmRealEstate> lists = null;
	private ActionBar mActionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pager);
		Bundle bundle = getIntent().getExtras();
		int position = bundle.getInt("ItemPosition");
		
		try
		{
			Dao<OrmRealEstate, Integer> estatesDao = getHelper().getOrmEsateDao();
			lists = new ArrayList<OrmRealEstate>(estatesDao.queryForAll());
			NUM_ITEMS = lists.size();
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			return DetailFavoriteFragment.newInstance(position, FavoriteDetailActivity.this);
		}
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
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		getSupportMenuInflater().inflate(R.menu.detail, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		// The rest of your onStart() code.
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop()
	{
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}
	
}
