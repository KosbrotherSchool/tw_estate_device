package com.kosbrother.realestate.fragment;

import java.sql.SQLException;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.kosbrother.imageloader.ImageLoader;
import com.kosbrother.realestate.Datas;
import com.kosbrother.realestate.DetailActivity;
import com.kosbrother.realestate.MainActivity;
import com.kosbrother.realestate.R;
import com.kosbrother.realestate.api.EstateApi;
import com.kosbrother.realestate.api.InfoParserApi;
import com.kosbrother.realestate.data.OrmRealEstate;
import com.kosbrother.realestate.entity.RealEstate;

public class DetailFragment extends Fragment
{
	int mNum;
	private ImageLoader imageLoader;
	private static DetailActivity mActivity;
	private RealEstate theEstate;

	/**
	 * Create a new instance of CountingFragment, providing "num" as an
	 * argument.
	 */
	public static DetailFragment newInstance(int num, DetailActivity theDetailActivity)
	{
		DetailFragment f = new DetailFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);
		mActivity = theDetailActivity;

		return f;
	}

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mNum = getArguments() != null ? getArguments().getInt("num") : 1;
		imageLoader = new ImageLoader(getActivity(), 100);
		new GetEstatesTask().execute();
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */

	private ImageView image;

	// trade detail
	private TextView text_address;
	private TextView text_date;
	private TextView text_estate_type;
	private TextView text_content_buy;
	private TextView text_ground_exchange_area;
	private TextView text_building_exchange_area;
	private TextView text_buy_per_square_feet;
	private TextView text_buy_total_price;

	// ground detail
	private TextView text_estate_town;
	private TextView text_ground_usage;

	// building detail
	private TextView text_date_built;
	private TextView text_main_purpose;
	private TextView text_building_type;
	private TextView text_main_material;
	private TextView text_buy_layer_building_layer;
	private TextView text_building_rooms;
	private TextView text_is_guarding;

	// parking detail
	private TextView text_parking_type;
	private TextView text_parking_exchange_area;
	private TextView text_parking_total_price;

	private ProgressDialog mProgressDialog;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.fragment_detail_pager, container, false);

		image = (ImageView) v.findViewById(R.id.imageview_detail);

		// trade detail
		text_address = (TextView) v.findViewById(R.id.text_detail_address);
		text_date = (TextView) v.findViewById(R.id.text_detail_date);
		text_estate_type = (TextView) v.findViewById(R.id.text_detail_estate_type);
		text_content_buy = (TextView) v.findViewById(R.id.text_detail_content_buy);
		text_ground_exchange_area = (TextView) v
				.findViewById(R.id.text_detail_ground_exchange_area);
		text_building_exchange_area = (TextView) v
				.findViewById(R.id.text_detail_building_exchange_area);
		text_buy_per_square_feet = (TextView) v.findViewById(R.id.text_detail_buy_per_square_feet);
		text_buy_total_price = (TextView) v.findViewById(R.id.text_detail_buy_total_price);

		// ground detail
		text_estate_town = (TextView) v.findViewById(R.id.text_detail_estate_town);
		text_ground_usage = (TextView) v.findViewById(R.id.text_detail_ground_usage);

		// building detail
		text_date_built = (TextView) v.findViewById(R.id.text_detail_date_built);
		text_main_purpose = (TextView) v.findViewById(R.id.text_detail_main_purpose);
		text_building_type = (TextView) v.findViewById(R.id.text_detail_building_type);
		text_main_material = (TextView) v.findViewById(R.id.text_detail_main_material);
		text_buy_layer_building_layer = (TextView) v
				.findViewById(R.id.text_detail_buy_layer_building_layer);
		text_building_rooms = (TextView) v.findViewById(R.id.text_detail_building_rooms);
		text_is_guarding = (TextView) v.findViewById(R.id.text_detail_is_guarding);

		// parking detail
		text_parking_type = (TextView) v.findViewById(R.id.text_detail_parking_type);
		text_parking_exchange_area = (TextView) v
				.findViewById(R.id.text_detail_parking_exchange_area);
		text_parking_total_price = (TextView) v.findViewById(R.id.text_detail_parking_total_price);

		CheckBox checkBoxFavorite = (CheckBox) v.findViewById(R.id.checkbox_favorite);
		checkBoxFavorite.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					// add to favorite (database)
					try
					{
						Dao<OrmRealEstate, Integer> estateDao = mActivity.getHelper()
								.getOrmEsateDao();
						OrmRealEstate newEstate = new OrmRealEstate();
						newEstate.estate_id = theEstate.estate_id;
						newEstate.estate_group = theEstate.estate_group;
						newEstate.estate_town = theEstate.estate_town;
						newEstate.estate_type = theEstate.estate_type;
						newEstate.estate_address = theEstate.estate_address;

						newEstate.ground_exchange_area = theEstate.ground_exchange_area;
						newEstate.ground_rent_area = theEstate.ground_rent_area;
						newEstate.ground_usage = theEstate.ground_usage;
						newEstate.date_buy = theEstate.date_buy;
						newEstate.content_buy = theEstate.content_buy;
						newEstate.date_rent = theEstate.date_rent;
						newEstate.content_rent = theEstate.content_rent;

						newEstate.rent_layer = theEstate.rent_layer;
						newEstate.buy_layer = theEstate.buy_layer;
						newEstate.building_total_layer = theEstate.building_total_layer;
						newEstate.building_type = theEstate.building_type;
						newEstate.main_purpose = theEstate.main_purpose;
						newEstate.main_material = theEstate.main_material;
						newEstate.date_built = theEstate.date_built;

						newEstate.building_exchange_area = theEstate.building_exchange_area;
						newEstate.building_room = theEstate.building_room;
						newEstate.building_sitting_room = theEstate.building_sitting_room;
						newEstate.building_rest_room = theEstate.building_rest_room;

						newEstate.is_guarding = theEstate.is_guarding;
						newEstate.is_having_furniture = theEstate.is_having_furniture;

						newEstate.buy_total_price = theEstate.buy_total_price;
						newEstate.buy_per_square_feet = theEstate.buy_per_square_feet;

						newEstate.parking_type = theEstate.parking_type;
						newEstate.parking_exchange_area = theEstate.parking_exchange_area;
						newEstate.parking_total_price = theEstate.parking_total_price;

						newEstate.x_lat = theEstate.x_lat;
						newEstate.y_long = theEstate.y_long;
						estateDao.create(newEstate);
						// List<OrmRealEstate> estatesList =
						// estateDao.queryForAll();
						Toast.makeText(mActivity, "加入我的最愛!", Toast.LENGTH_SHORT).show();
					} catch (SQLException e)
					{

						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else
				{
					// remove from favorite
					try
					{
						Dao<OrmRealEstate, Integer> estateDao = mActivity.getHelper()
								.getOrmEsateDao();
						DeleteBuilder<OrmRealEstate, Integer> deleteBuilder = estateDao
								.deleteBuilder();
						deleteBuilder.where().eq(OrmRealEstate.Column_Estate_ID_NAME,
								Datas.mEstates.get(mNum).estate_id);
						deleteBuilder.delete();
						Toast.makeText(mActivity, "從我的最愛移除!", Toast.LENGTH_SHORT).show();
					} catch (SQLException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});

		// set image
		String x_lat = Double.toString(Datas.mEstates.get(mNum).x_lat);
		String y_long = Double.toString(Datas.mEstates.get(mNum).y_long);
		String url = "http://maps.google.com/maps/api/staticmap?center=" + x_lat + "," + y_long
				+ "&zoom=17&markers=color:red%7Clabel:%7C" + x_lat + "," + y_long
				+ "&size=400x150&language=zh-TW&sensor=false";
		imageLoader.DisplayImage(url, image);

		return v;
	}

	private class GetEstatesTask extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{
			// show progress dialog
//			mProgressDialog = ProgressDialog.show(mActivity, null, "資料傳遞中");
//			mProgressDialog.setCancelable(true);
		}

		@Override
		protected Void doInBackground(Void... arg)
		{
			// TODO Auto-generated method stub
			theEstate = EstateApi.getEsatateById(Datas.mEstates.get(mNum).estate_id);
			return null;
		}

		@Override
		protected void onPostExecute(Void arg)
		{
			super.onPostExecute(null);
//			if (mProgressDialog.isShowing())
//			{
//				mProgressDialog.dismiss();
//			}
			
			text_address.setText(theEstate.estate_address);
			text_date.setText(InfoParserApi.parseBuyDate(theEstate.date_buy));
			text_estate_type.setText(theEstate.estate_type);
			text_content_buy.setText(theEstate.content_buy);
			text_ground_exchange_area.setText(InfoParserApi
					.parseBuildingExchangeArea(theEstate.ground_exchange_area));
			text_building_exchange_area.setText(InfoParserApi
					.parseBuildingExchangeArea(theEstate.building_exchange_area));
			text_buy_per_square_feet.setText(InfoParserApi
					.parsePerSquareFeetMoney(theEstate.buy_per_square_feet));
			text_buy_total_price.setText(InfoParserApi
					.parseTotalBuyMoney(theEstate.buy_total_price));

			text_estate_town.setText(theEstate.estate_town);
			text_ground_usage.setText(theEstate.ground_usage);

			text_date_built.setText(theEstate.date_built);
			text_main_purpose.setText(theEstate.main_purpose);
			text_building_type.setText(theEstate.building_type);
			text_main_material.setText(theEstate.main_material);
			text_buy_layer_building_layer.setText(theEstate.buy_layer + "/"
					+ Integer.toString(theEstate.building_total_layer));
			text_building_rooms.setText(Integer.toString(theEstate.building_room) + "房"
					+ Integer.toString(theEstate.building_sitting_room) + "廳"
					+ Integer.toString(theEstate.building_rest_room) + "衛浴");
			text_is_guarding.setText(theEstate.is_guarding);

			text_parking_type.setText(theEstate.parking_type);
			text_parking_exchange_area.setText(InfoParserApi
					.parseBuildingExchangeArea(theEstate.parking_exchange_area));
			text_parking_total_price.setText(InfoParserApi
					.parseTotalBuyMoney(theEstate.parking_total_price));

		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

	}

}
