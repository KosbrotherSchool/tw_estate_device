package com.kosbrother.realestate.api;

import java.util.HashMap;

import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.content.SharedPreferences;

public class Setting
{

	public final static String keyPref = "pref";
	public final static String estates_items_notify = "estate_items_notify";
	public final static String centerLat = "last_center_lat";
	public final static String centerLng = "last_center_lng";

	// 0 for not notify, 1 for notify
	public final static int initial_items_notify = 1;

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

	public static void saveLastCenter(double lat, double lng, Context context)
	{
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
//		long latLong = (long) (lat * 1E6);
//		long lngLong = (long) (lng * 1E6);
		sharePreference.edit().putLong(centerLat, Double.doubleToLongBits(lat)).commit();
		sharePreference.edit().putLong(centerLng, Double.doubleToLongBits(lng)).commit();
	}
	
	public static LatLng getLastCenter(Context context){
		SharedPreferences sharePreference = context.getSharedPreferences(
				keyPref, 0);
		long latLong = sharePreference.getLong(centerLat, 0);
		long lngLong = sharePreference.getLong(centerLng, 0);
		LatLng theLatLng = new LatLng(Double.longBitsToDouble(latLong), Double.longBitsToDouble(lngLong));
		return theLatLng;
		
	}

}
