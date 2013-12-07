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
import com.kosbrother.realestate.fragment.CalculateResultFragment;
import com.kosbrother.realestate.fragment.DetailFragment;

public class CalculateResultActivity extends SherlockFragmentActivity
{
	private static final int CONTENT_VIEW_ID = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculate_result);

		FrameLayout frame = new FrameLayout(this);
		frame.setId(CONTENT_VIEW_ID);
		setContentView(frame, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));

		if (savedInstanceState == null)
		{
			// 建立Fragment
			Fragment newFragment = null;
			if (getIntent() != null && getIntent().getExtras() != null)
			{
				Bundle bundle = getIntent().getExtras();
				newFragment = CalculateResultFragment.newInstance(bundle);
			} else
			{
				newFragment = new CalculateResultFragment();
			}
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			ft.add(CONTENT_VIEW_ID, newFragment).commit();
		}
		// 設定Home鍵的箭頭
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("房貸利息試算");

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case android.R.id.home:
			// 若按下Fragment的Home鍵就結束Activity
			finish();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

}
