package com.kosbrother.realestate.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosbrother.imageloader.ImageLoader;
import com.kosbrother.realestate.Datas;
import com.kosbrother.realestate.DetailActivity;
import com.kosbrother.realestate.R;
import com.kosbrother.realestate.api.InfoParserApi;
import com.kosbrother.realestate.entity.RealEstate;

public class ListEstateAdapter extends BaseAdapter
{

	private final Activity activity;
	private final ArrayList<RealEstate> data;
	private static LayoutInflater inflater = null;
	private ImageLoader imageLoader;

	// private String mChannelTitle;

	public ListEstateAdapter(Activity a, ArrayList<RealEstate> d)
	{
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity, 100);
	}

	public int getCount()
	{
		return data.size();
	}

	public Object getItem(int position)
	{
		return position;
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_item_estate, null);

		TextView textAddress = (TextView) vi.findViewById(R.id.text_info_address);

		TextView textHouseAge = (TextView) vi.findViewById(R.id.text_info_house_age);
		TextView textBuyDate = (TextView) vi.findViewById(R.id.text_info_buy_date);

		TextView textGroundArea = (TextView) vi.findViewById(R.id.text_info_ground_area);
		TextView textEstateType = (TextView) vi.findViewById(R.id.text_info_estate_type);

		TextView textTotalPrice = (TextView) vi.findViewById(R.id.text_info_total_price);
		TextView textBuildingType = (TextView) vi.findViewById(R.id.text_info_buiding_type);

		TextView textBuyPerSquareFeet = (TextView) vi
				.findViewById(R.id.text_info_buy_persquare_feet);
		TextView textBuyLayer = (TextView) vi.findViewById(R.id.text_info_buy_layer);

		TextView textRooms = (TextView) vi.findViewById(R.id.text_info_rooms);
		TextView textIsGuarding = (TextView) vi.findViewById(R.id.text_info_is_guarding);

		ImageView imageView = (ImageView) vi.findViewById(R.id.image_list);
		String x_lat = Double.toString(data.get(position).x_lat);
		String y_long = Double.toString(data.get(position).y_long);
		String url = "http://maps.google.com/maps/api/staticmap?center=" + x_lat + "," + y_long
				+ "&zoom=17&markers=color:red%7Clabel:%7C" + x_lat + "," + y_long
				+ "&size=400x150&language=zh-TW&sensor=false";
		imageLoader.DisplayImage(url, imageView);

		textAddress.setText(data.get(position).estate_address);

		textHouseAge.setText(InfoParserApi.parseHouseAge(data.get(position).date_built));
		textBuyDate.setText(InfoParserApi.parseBuyDate(data.get(position).date_buy));

		textGroundArea
				.setText(InfoParserApi.parseBuildingExchangeArea(data.get(position).building_exchange_area));
		textEstateType.setText(InfoParserApi.parseEstateType(data.get(position).estate_type));

		textTotalPrice
				.setText(InfoParserApi.parseTotalBuyMoney(data.get(position).buy_total_price));
		textBuildingType.setText(InfoParserApi.parseEstateType(data.get(position).building_type));

		textBuyPerSquareFeet
				.setText(InfoParserApi.parsePerSquareFeetMoney(data.get(position).buy_per_square_feet));
		textBuyLayer.setText(data.get(position).buy_layer + "/"
				+ Integer.toString(data.get(position).building_total_layer));

		textRooms.setText(Integer.toString(data.get(position).building_room) + "房"
				+ Integer.toString(data.get(position).building_sitting_room) + "廳"
				+ Integer.toString(data.get(position).building_rest_room) + "衛浴");
		textIsGuarding.setText(data.get(position).is_guarding);

		vi.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				Intent intent = new Intent(activity, DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("ItemPosition", position);
				intent.putExtras(bundle);
				activity.startActivity(intent);

			}
		});

		return vi;
	}
}