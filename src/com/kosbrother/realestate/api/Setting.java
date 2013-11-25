package com.kosbrother.realestate.api;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;

public class Setting
{

	public final static String keyPref = "pref";
	public final static String estates_items_notify = "estate_items_notify";

	public final static int initial_items_notify = 1; // 0 for not notify, 1 for
														// notify

	private static final HashMap<String, Integer> initMap = new HashMap<String, Integer>()
	{
		private static final long serialVersionUID = 1L;
		{
			put(estates_items_notify, initial_items_notify);
		}
	};

	public static int getSetting(String settingKey, Context context)
	{
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
		int settingValue = sharePreference.getInt(settingKey,
				initMap.get(settingKey));
		return settingValue;
	}

	public static void saveSetting(String settingKey, int settingValue,
			Context context)
	{
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
		sharePreference.edit().putInt(settingKey, settingValue).commit();
	}

}
