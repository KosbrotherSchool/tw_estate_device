package com.kosbrother.realestate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kosbrother.realestate.api.Setting;

public class SettingActivity extends SherlockActivity
{

	private CheckBox checkBox_estate_notifyBox;
	private int isEstateNotify;
	private TextView textStarUs;
	private TextView textFeedBack;
	private TextView textShareApp;
	
	private RelativeLayout adBannerLayout;
	private AdView adMobAdView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		isEstateNotify = Setting.getSetting(Setting.estates_items_notify, SettingActivity.this);

		setViews();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
		CallAds();
	}

	private void setViews()
	{
		checkBox_estate_notifyBox = (CheckBox) findViewById(R.id.checkbox_estate_notify);
		setCheckBoxState(checkBox_estate_notifyBox, isEstateNotify);

		textStarUs = (TextView) findViewById(R.id.text_star_us);
		textFeedBack = (TextView) findViewById(R.id.text_feedback);
		textShareApp = (TextView) findViewById(R.id.text_share_app);

		textStarUs.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.kosbrother.realestate"));
				startActivity(intent);
			}
		});

		textFeedBack.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
				emailIntent.setType("plain/text");
				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
						new String[] { "kosbrotherschool@gmail.com" });
				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "意見回饋 from 實價登錄");
				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
			}
		});

		textShareApp.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_TEXT, "Zillion 實價登錄,"
						+ "https://play.google.com/store/apps/details?id=com.kosbrother.realestate");
				startActivity(Intent.createChooser(intent, "Share..."));
			}
		});

	}

	private void setCheckBoxState(CheckBox checkBox, int isCheck)
	{
		if (isCheck == 1)
		{
			checkBox.setChecked(true);
		} else
		{
			checkBox.setChecked(false);
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
	public void onBackPressed()
	{
		super.onBackPressed();
		saveCheckBoxSetting(Setting.estates_items_notify, checkBox_estate_notifyBox);
	}

	private void saveCheckBoxSetting(String key, CheckBox checkBox)
	{
		if (checkBox.isChecked())
		{
			Setting.saveSetting(key, 1, SettingActivity.this);
		} else
		{
			Setting.saveSetting(key, 0, SettingActivity.this);
		}

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
	
	private void CallAds()
	{

		adBannerLayout = (RelativeLayout) findViewById(R.id.adLayout);
		final AdRequest adReq = new AdRequest.Builder().build();

		// 12-18 17:01:12.438: I/Ads(8252): Use
		// AdRequest.Builder.addTestDevice("A25819A64B56C65500038B8A9E7C19DD")
		// to get test ads on this device.

		adMobAdView = new AdView(SettingActivity.this);
		adMobAdView.setAdSize(AdSize.SMART_BANNER);
		adMobAdView.setAdUnitId(Constants.MEDIATION_KEY);

		adMobAdView.loadAd(adReq);
		adMobAdView.setAdListener(new AdListener()
		{
			@Override
			public void onAdLoaded() {
				adBannerLayout.setVisibility(View.VISIBLE);
				if (adBannerLayout.getChildAt(0)!=null)
				{
					adBannerLayout.removeViewAt(0);
				}
				adBannerLayout.addView(adMobAdView);
			}
			
			public void onAdFailedToLoad(int errorCode) {
				adBannerLayout.setVisibility(View.GONE);
			}
			
		});	
	}

}
