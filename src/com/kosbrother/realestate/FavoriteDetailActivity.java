package com.kosbrother.realestate;

import java.sql.SQLException;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pager);
		// NUM_ITEMS = Datas.mEstates.size();
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

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
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

}
