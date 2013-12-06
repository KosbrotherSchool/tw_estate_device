package com.kosbrother.realestate;

import java.sql.SQLException;
import java.util.ArrayList;

import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.kosbrother.realestate.adapter.ListOrmEstateAdapter;
import com.kosbrother.realestate.data.DatabaseHelper;
import com.kosbrother.realestate.data.OrmRealEstate;

public class FavoriteActivity extends SherlockFragmentActivity
{

	private ListOrmEstateAdapter mAdapter;
	private ListView mainListView;
	private DatabaseHelper databaseHelper = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		mainListView = (ListView) findViewById(R.id.list_estates);
		
		try
		{
			Dao<OrmRealEstate, Integer> estatesDao = getHelper().getOrmEsateDao();
			ArrayList<OrmRealEstate> lists = new ArrayList<OrmRealEstate>(estatesDao.queryForAll()) ;
			
			mAdapter = new ListOrmEstateAdapter(FavoriteActivity.this, lists);
			mainListView.setAdapter(mAdapter);
		} catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		

	}

	@Override
	protected void onResume()
	{
		super.onResume();

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