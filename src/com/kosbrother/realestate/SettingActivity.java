package com.kosbrother.realestate;

import android.os.Bundle;
import android.widget.CheckBox;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.kosbrother.realestate.api.Setting;

public class SettingActivity extends SherlockActivity
{

	private CheckBox checkBox_estate_notifyBox;
	private int isEstateNotify;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		isEstateNotify = Setting.getSetting(Setting.estates_items_notify,
				SettingActivity.this);

		setViews();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

	}

	private void setViews()
	{
		checkBox_estate_notifyBox = (CheckBox) findViewById(R.id.checkbox_estate_notify);
		setCheckBoxState(checkBox_estate_notifyBox, isEstateNotify);
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
		saveCheckBoxSetting(Setting.estates_items_notify,
				checkBox_estate_notifyBox);
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

}
