package com.kosbrother.realestate.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.model.LatLng;
import com.kosbrother.realestate.entity.RealEstate;

import android.R.integer;
import android.util.Log;

public class EstateApi
{

	final static String HOST = "http://106.186.31.71";
	public static final String TAG = "ESTATE_API";
	public static final boolean DEBUG = true;

	public static ArrayList<RealEstate> getAroundAllByAreas(LatLng[] latLngs, boolean isShowSale,
			boolean isShowRent, boolean isShowPreSale, int salePerSquareMin, int salePerSquareMax,
			int saleTotalMin, int saleTotalMax, double saleAreaMin, double saleAreaMax)
	{
		String message = getMessageFromServer("GET", "/api/v1/real_estate/around_all_by_areas?"
				+ "center_x="
				+ latLngs[0].latitude
				+ "&center_y="
				+ latLngs[0].longitude
				+ "&spot1_x="
				+ latLngs[1].latitude
				+ "&spot1_y="
				+ latLngs[1].longitude
				+ "&spot2_x="
				+ latLngs[2].latitude
				+ "&spot2_y="
				+ latLngs[2].longitude
				+ "&spot3_x="
				+ latLngs[3].latitude
				+ "&spot3_y="
				+ latLngs[3].longitude
				+ "&spot4_x="
				+ latLngs[4].latitude
				+ "&spot4_y="
				+ latLngs[4].longitude
				+ "&spot5_x="
				+ latLngs[5].latitude
				+ "&spot5_y="
				+ latLngs[5].longitude
				+ "&spot6_x="
				+ latLngs[6].latitude
				+ "&spot6_y="
				+ latLngs[6].longitude
				+ "&spot7_x="
				+ latLngs[7].latitude
				+ "&spot7_y="
				+ latLngs[7].longitude
				+ "&spot8_x="
				+ latLngs[8].latitude
				+ "&spot8_y="
				+ latLngs[8].longitude
				+ "&isShowSale="
				+ isShowSale
				+ "&isShowRent="
				+ isShowRent
				+ "&isShowPreSale="
				+ isShowPreSale
				+ "&salePerSquareMin="
				+ salePerSquareMin
				+ "&salePerSquareMax="
				+ salePerSquareMax
				+ "&saleTotalMin="
				+ saleTotalMin
				* 3.3058
				+ "&saleTotalMax="
				+ saleTotalMax
				* 3.3058 + "&saleAreaMin=" + saleAreaMin + "&saleAreaMax=" + saleAreaMax, null,
				null);
		ArrayList<RealEstate> realEstates = new ArrayList<RealEstate>();
		if (message == null)
		{
			return null;
		} else
		{
			return parseEstateMessage(message, realEstates);
		}
	}

	public static RealEstate getEsatateById(int estate_id)
	{
		String message = getMessageFromServer("GET", "/api/v1/real_estate/get_estate_item?"
				+ "estate_id=" + estate_id, null, null);
		RealEstate estateItem = null;
		if (message == null)
		{
			return null;
		} else
		{
			return parseEstateItemMessage(message, estateItem);
		}
	}

	// public static ArrayList<RealEstate> getAroundAll(double x, double y)
	// {
	// String message = getMessageFromServer("GET",
	// "/api/v1/real_estate/around_all?" + "x=" + x
	// + "&y=" + y, null, null);
	// ArrayList<RealEstate> realEstates = new ArrayList<RealEstate>();
	// if (message == null)
	// {
	// return null;
	// } else
	// {
	// return parseEstateMessage(message, realEstates);
	// }
	// }

	// public static ArrayList<RealEstate> getAroundEstate(double x, double y)
	// {
	// String message = getMessageFromServer("GET",
	// "/api/v1/real_estate/estate_around?" + "x="
	// + x + "&y=" + y, null, null);
	// ArrayList<RealEstate> realEstates = new ArrayList<RealEstate>();
	// if (message == null)
	// {
	// return null;
	// } else
	// {
	// return parseEstateMessage(message, realEstates);
	// }
	// }
	//
	// public static ArrayList<RealEstate> getAroundPreSale(double x, double y)
	// {
	// String message = getMessageFromServer("GET",
	// "/api/v1/real_estate/presale_around?" + "x="
	// + x + "&y=" + y, null, null);
	// ArrayList<RealEstate> realEstates = new ArrayList<RealEstate>();
	// if (message == null)
	// {
	// return null;
	// } else
	// {
	// return parseEstateMessage(message, realEstates);
	// }
	// }
	//
	// public static ArrayList<RealEstate> getAroundRent(double x, double y)
	// {
	// String message = getMessageFromServer("GET",
	// "/api/v1/real_estate/rent_around?" + "x=" + x
	// + "&y=" + y, null, null);
	// ArrayList<RealEstate> realEstates = new ArrayList<RealEstate>();
	// if (message == null)
	// {
	// return null;
	// } else
	// {
	// return parseEstateMessage(message, realEstates);
	// }
	// }
	//
	// public static ArrayList<RealEstate> getAroundEstateAndPresale(double x,
	// double y)
	// {
	// String message = getMessageFromServer("GET",
	// "/api/v1/real_estate/estate_and_pre_sale_around?" + "x=" + x + "&y=" + y,
	// null,
	// null);
	// ArrayList<RealEstate> realEstates = new ArrayList<RealEstate>();
	// if (message == null)
	// {
	// return null;
	// } else
	// {
	// return parseEstateMessage(message, realEstates);
	// }
	// }
	//
	// public static ArrayList<RealEstate> getAroundEstateAndRent(double x,
	// double y)
	// {
	// String message = getMessageFromServer("GET",
	// "/api/v1/real_estate/estate_and_rent_around?"
	// + "x=" + x + "&y=" + y, null, null);
	// ArrayList<RealEstate> realEstates = new ArrayList<RealEstate>();
	// if (message == null)
	// {
	// return null;
	// } else
	// {
	// return parseEstateMessage(message, realEstates);
	// }
	// }
	//
	// public static ArrayList<RealEstate> getAroundPresaleAndRent(double x,
	// double y)
	// {
	// String message = getMessageFromServer("GET",
	// "/api/v1/real_estate/pre_sale_and_rent_around?" + "x=" + x + "&y=" + y,
	// null, null);
	// ArrayList<RealEstate> realEstates = new ArrayList<RealEstate>();
	// if (message == null)
	// {
	// return null;
	// } else
	// {
	// return parseEstateMessage(message, realEstates);
	// }
	// }

	private static RealEstate parseEstateItemMessage(String message, RealEstate estateItem)
	{

		try
		{

			JSONObject jObject;
			jObject = new JSONObject(message.toString());

			int id = 0;
			try
			{
				id = jObject.getInt("id");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int estate_group = 1;
			try
			{
				estate_group = jObject.getInt("estate_group");

			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String estate_town = "";
			try
			{
				estate_town = jObject.getString("estate_town");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String estate_type = "";
			try
			{
				estate_type = jObject.getString("estate_type");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String estate_address = "";
			try
			{
				estate_address = jObject.getString("address");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			double ground_exchange_area = 0;
			try
			{
				ground_exchange_area = jObject.getDouble("ground_exchange_area");
			} catch (Exception e)
			{

			}

			double ground_rent_area = 0;
			try
			{
				ground_rent_area = jObject.getDouble("ground_rent_area");
			} catch (Exception e)
			{

			}

			String ground_usage = "";
			try
			{
				ground_usage = jObject.getString("ground_usage");

			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String date_buy = "";
			try
			{
				date_buy = jObject.getString("date_buy");
			} catch (Exception e)
			{

			}

			String content_buy = "";
			try
			{
				content_buy = jObject.getString("content_buy");
			} catch (Exception e)
			{

			}

			String date_rent = "";
			try
			{
				date_rent = jObject.getString("date_rent");
			} catch (Exception e)
			{

			}

			String content_rent = "";
			try
			{
				content_rent = jObject.getString("content_rent");
			} catch (Exception e)
			{

			}

			String rent_layer = "";
			try
			{
				rent_layer = jObject.getString("rent_layer");
			} catch (Exception e)
			{

			}

			String buy_layer = "";
			try
			{
				buy_layer = jObject.getString("buy_layer");
			} catch (Exception e)
			{

			}

			int building_total_layer = 0;
			try
			{
				building_total_layer = jObject.getInt("building_total_layer");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String building_type = "";
			try
			{
				building_type = jObject.getString("building_type");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String main_purpose = "";
			try
			{
				main_purpose = jObject.getString("main_purpose");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String main_material = "";
			try
			{
				main_material = jObject.getString("main_material");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String date_built = "";
			try
			{
				date_built = jObject.getString("date_built");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			double building_exchange_area = 0;
			try
			{
				building_exchange_area = jObject.getDouble("building_exchange_area");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int building_room = 0;
			try
			{
				building_room = jObject.getInt("building_room");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int building_sitting_room = 0;
			try
			{
				building_sitting_room = jObject.getInt("building_sitting_room");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			int building_rest_room = 0;
			try
			{
				building_rest_room = jObject.getInt("building_rest_room");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String is_guarding = "";
			try
			{
				is_guarding = jObject.getString("is_guarding");
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			String is_having_furniture = "";
			try
			{
				is_having_furniture = jObject.getString("is_having_furniture");
			} catch (Exception e)
			{

			}

			int buy_total_price = 0;
			try
			{
				buy_total_price = jObject.getInt("buy_total_price");
			} catch (Exception e)
			{

			}

			double buy_per_square_feet = 0;
			try
			{
				buy_per_square_feet = jObject.getDouble("buy_per_square_feet");
			} catch (Exception e)
			{

			}

			int rent_total_price = 0;
			try
			{
				rent_total_price = jObject.getInt("rent_total_price");
			} catch (Exception e)
			{

			}

			double rent_per_square_feet = 0;
			try
			{
				rent_per_square_feet = jObject.getDouble("rent_per_square_feet");
			} catch (Exception e)
			{

			}

			String parking_type = "";
			try
			{
				parking_type = jObject.getString("parking_type");
			} catch (Exception e)
			{

			}

			double parking_exchange_area = 0;
			try
			{
				parking_exchange_area = jObject.getDouble("parking_exchange_area");
			} catch (Exception e)
			{

			}

			int parking_total_price = 0;
			try
			{
				parking_total_price = jObject.getInt("parking_total_price");
			} catch (Exception e)
			{

			}

			double x_lat = jObject.getDouble("x_lat");
			double y_long = jObject.getDouble("y_long");

			estateItem = new RealEstate(id, estate_group, estate_town, estate_type, estate_address,
					ground_exchange_area, ground_rent_area, ground_usage, date_buy, content_buy,
					date_rent, content_rent, rent_layer, buy_layer, building_total_layer,
					building_type, main_purpose, main_material, date_built, building_exchange_area,
					building_room, building_sitting_room, building_rest_room, is_guarding,
					is_having_furniture, buy_total_price, buy_per_square_feet, rent_total_price,
					rent_per_square_feet, parking_type, parking_exchange_area, parking_total_price,
					x_lat, y_long);

		} catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}

		Log.i(TAG, "End Parse Json:" + System.currentTimeMillis());
		return estateItem;
	}

	private static ArrayList<RealEstate> parseEstateMessage(String message,
			ArrayList<RealEstate> realEstates)
	{

		Log.i(TAG, "Start Parse Json:" + System.currentTimeMillis());
		try
		{

			JSONArray jArray;
			jArray = new JSONArray(message.toString());
			for (int i = 0; i < jArray.length(); i++)
			{

				int id = 0;
				try
				{
					id = jArray.getJSONObject(i).getInt("id");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int estate_group = 1;
				try
				{
					estate_group = jArray.getJSONObject(i).getInt("estate_group");

				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String estate_town = "";
				try
				{
					estate_town = jArray.getJSONObject(i).getString("estate_town");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String estate_type = "";
				try
				{
					estate_type = jArray.getJSONObject(i).getString("estate_type");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String estate_address = "";
				try
				{
					estate_address = jArray.getJSONObject(i).getString("address");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				double ground_exchange_area = 0;
				try
				{
					ground_exchange_area = jArray.getJSONObject(i)
							.getDouble("ground_exchange_area");
				} catch (Exception e)
				{

				}

				double ground_rent_area = 0;
				try
				{
					ground_rent_area = jArray.getJSONObject(i).getDouble("ground_rent_area");
				} catch (Exception e)
				{

				}

				String ground_usage = "";
				try
				{
					ground_usage = jArray.getJSONObject(i).getString("ground_usage");

				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String date_buy = "";
				try
				{
					date_buy = jArray.getJSONObject(i).getString("date_buy");
				} catch (Exception e)
				{

				}

				String content_buy = "";
				try
				{
					content_buy = jArray.getJSONObject(i).getString("content_buy");
				} catch (Exception e)
				{

				}

				String date_rent = "";
				try
				{
					date_rent = jArray.getJSONObject(i).getString("date_rent");
				} catch (Exception e)
				{

				}

				String content_rent = "";
				try
				{
					content_rent = jArray.getJSONObject(i).getString("content_rent");
				} catch (Exception e)
				{

				}

				String rent_layer = "";
				try
				{
					rent_layer = jArray.getJSONObject(i).getString("rent_layer");
				} catch (Exception e)
				{

				}

				String buy_layer = "";
				try
				{
					buy_layer = jArray.getJSONObject(i).getString("buy_layer");
				} catch (Exception e)
				{

				}

				int building_total_layer = 0;
				try
				{
					building_total_layer = jArray.getJSONObject(i).getInt("building_total_layer");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String building_type = "";
				try
				{
					building_type = jArray.getJSONObject(i).getString("building_type");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String main_purpose = "";
				try
				{
					main_purpose = jArray.getJSONObject(i).getString("main_purpose");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String main_material = "";
				try
				{
					main_material = jArray.getJSONObject(i).getString("main_material");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String date_built = "";
				try
				{
					date_built = jArray.getJSONObject(i).getString("date_built");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				double building_exchange_area = 0;
				try
				{
					building_exchange_area = jArray.getJSONObject(i).getDouble(
							"building_exchange_area");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int building_room = 0;
				try
				{
					building_room = jArray.getJSONObject(i).getInt("building_room");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int building_sitting_room = 0;
				try
				{
					building_sitting_room = jArray.getJSONObject(i).getInt("building_sitting_room");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				int building_rest_room = 0;
				try
				{
					building_rest_room = jArray.getJSONObject(i).getInt("building_rest_room");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String is_guarding = "";
				try
				{
					is_guarding = jArray.getJSONObject(i).getString("is_guarding");
				} catch (Exception e)
				{
					// TODO: handle exception
				}

				String is_having_furniture = "";
				try
				{
					is_having_furniture = jArray.getJSONObject(i).getString("is_having_furniture");
				} catch (Exception e)
				{

				}

				int buy_total_price = 0;
				try
				{
					buy_total_price = jArray.getJSONObject(i).getInt("buy_total_price");
				} catch (Exception e)
				{

				}

				double buy_per_square_feet = 0;
				try
				{
					buy_per_square_feet = jArray.getJSONObject(i).getDouble("buy_per_square_feet");
				} catch (Exception e)
				{

				}

				int rent_total_price = 0;
				try
				{
					rent_total_price = jArray.getJSONObject(i).getInt("rent_total_price");
				} catch (Exception e)
				{

				}

				double rent_per_square_feet = 0;
				try
				{
					rent_per_square_feet = jArray.getJSONObject(i)
							.getDouble("rent_per_square_feet");
				} catch (Exception e)
				{

				}

				String parking_type = "";
				try
				{
					parking_type = jArray.getJSONObject(i).getString("parking_type");
				} catch (Exception e)
				{

				}

				double parking_exchange_area = 0;
				try
				{
					parking_exchange_area = jArray.getJSONObject(i).getDouble(
							"parking_exchange_area");
				} catch (Exception e)
				{

				}

				int parking_total_price = 0;
				try
				{
					parking_total_price = jArray.getJSONObject(i).getInt("parking_total_price");
				} catch (Exception e)
				{

				}

				double x_lat = jArray.getJSONObject(i).getDouble("x_lat");
				double y_long = jArray.getJSONObject(i).getDouble("y_long");

				RealEstate newEstate = new RealEstate(id, estate_group, estate_town, estate_type,
						estate_address, ground_exchange_area, ground_rent_area, ground_usage,
						date_buy, content_buy, date_rent, content_rent, rent_layer, buy_layer,
						building_total_layer, building_type, main_purpose, main_material,
						date_built, building_exchange_area, building_room, building_sitting_room,
						building_rest_room, is_guarding, is_having_furniture, buy_total_price,
						buy_per_square_feet, rent_total_price, rent_per_square_feet, parking_type,
						parking_exchange_area, parking_total_price, x_lat, y_long);
				realEstates.add(newEstate);

			}

		} catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}

		Log.i(TAG, "End Parse Json:" + System.currentTimeMillis());
		return realEstates;

	}

	public static String getMessageFromServer(String requestMethod, String apiPath,
			JSONObject json, String apiUrl)
	{
		Log.i(TAG, "Start Load from server:" + System.currentTimeMillis());
		URL url;
		try
		{
			if (apiUrl != null)
				url = new URL(apiUrl);
			else
				url = new URL(HOST + apiPath);

			if (DEBUG)
				Log.d(TAG, "URL: " + url);

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(requestMethod);

			connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			if (requestMethod.equalsIgnoreCase("POST"))
				connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.connect();

			if (requestMethod.equalsIgnoreCase("POST"))
			{
				OutputStream outputStream;

				outputStream = connection.getOutputStream();
				if (DEBUG)
					Log.d("post message", json.toString());

				outputStream.write(json.toString().getBytes());
				outputStream.flush();
				outputStream.close();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			StringBuilder lines = new StringBuilder();
			;
			String tempStr;

			while ((tempStr = reader.readLine()) != null)
			{
				lines = lines.append(tempStr);
			}
			if (DEBUG)
				Log.d("MOVIE_API", lines.toString());

			reader.close();
			connection.disconnect();

			Log.i(TAG, "End Load from server:" + System.currentTimeMillis());

			return lines.toString();
		} catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
