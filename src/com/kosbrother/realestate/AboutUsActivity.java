package com.kosbrother.realestate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Choreographer.FrameCallback;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.kosbrother.realestate.fragment.CalculatorFragment;
import com.kosbrother.realestate.fragment.DetailFragment;

public class AboutUsActivity extends SherlockActivity
{

	private TextView contactTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);

		contactTextView = (TextView) findViewById(R.id.text_contact_us);

		contactTextView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
	            emailIntent.setType("plain/text");
	            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "kosbrotherschool@gmail.com" });
	            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "聯絡我們 from 實價登錄");
	            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
	            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			}
		});

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
