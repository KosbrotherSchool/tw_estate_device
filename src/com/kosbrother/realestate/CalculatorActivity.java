package com.kosbrother.realestate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.Choreographer.FrameCallback;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.kosbrother.realestate.fragment.CalculatorFragment;
import com.kosbrother.realestate.fragment.DetailFragment;

public class CalculatorActivity extends SherlockFragmentActivity
{
	private static final int CONTENT_VIEW_ID = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator);

		FrameLayout frame = new FrameLayout(this);
		frame.setId(CONTENT_VIEW_ID);
		setContentView(frame, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		if (savedInstanceState == null)
		{
			Fragment newFragment = new CalculatorFragment();
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(CONTENT_VIEW_ID, newFragment).commit();
		}
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
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

	
}
