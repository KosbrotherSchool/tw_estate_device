package com.kosbrother.realestate.entity;

public class PreSale {
	
	String estate_town;
	String estate_type;
	String estate_address;
	
	double ground_exchange_area;
	
	String ground_usage;
	
	String date_buy;
	String content_buy;
	
	String buy_layer;
	int    building_total_layer;
	String building_type;
	String main_purpose;
	String main_material;
	String date_built;
	
	double building_exchange_area;
	int building_room; //房
	int building_sitting_room; //廳
	int building_rest_room; //浴室
	
	String is_guarding;
	int buy_total_price;
	double buy_per_square_feet;
	
	String parking_type;
	double parking_exchange_area;
	int parking_total_price;
	
	double x_lat;
	double y_long;
	
	public PreSale(String estate_town, String estate_type, String estate_address,
			double ground_exchange_area, String ground_usage, String date_buy,
			String content_buy, String buy_layer, int building_total_layer,
			String building_type, String main_purpose, String main_material,
			String date_built, double building_exchange_area, int building_room,
			int building_sitting_room, int building_rest_room, String is_guarding,
			int buy_total_price, double buy_per_square_feet, String parking_type,
			double parking_exchange_area, int parking_total_price, 
			double x_lat, double y_long){
		
		this.estate_town = estate_town;
		this.estate_type = estate_type;
		this.estate_address = estate_address;
		
		this.ground_exchange_area = ground_exchange_area;
		this.ground_usage = ground_usage;	
		
		this.date_buy = date_buy;
		this.content_buy = content_buy;
		this.buy_layer = buy_layer;
		this.building_total_layer = building_total_layer;
		this.building_type = building_type;
		this.main_purpose = main_purpose;
		this.main_material = main_material;
		this.date_built = date_built;
		this.building_exchange_area = building_exchange_area;
		this.building_room = building_room;
		this.building_sitting_room = building_sitting_room;
		this.building_rest_room = building_rest_room;
		this.is_guarding = is_guarding;
		
		this.buy_total_price = buy_total_price;
		this.buy_per_square_feet = buy_per_square_feet;
		
		this.parking_type = parking_type;
		this.parking_exchange_area = parking_exchange_area;
		this.parking_total_price = parking_total_price;
		
		this.x_lat = x_lat;
		this.y_long = y_long;
	}
	
}
