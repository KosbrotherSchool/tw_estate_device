package com.kosbrother.realestate.entity;

public class RealEstate {
	
	public int id;
	public int estate_group;
	
	public String estate_town;
	public String estate_type;
	public String estate_address;
	
	public double ground_exchange_area;
	public double ground_rent_area;
	
	public String ground_usage;
	
	public String date_buy;
	public String content_buy;
	public String date_rent;
	public String content_rent;
	
	public String rent_layer;
	public String buy_layer;
	public int    building_total_layer;
	public String building_type;
	public String main_purpose;
	public String main_material;
	public String date_built;
	
	public double building_exchange_area;
	public int building_room; //房
	public int building_sitting_room; //廳
	public int building_rest_room; //浴室
	
	public String is_guarding;
	public String is_having_furniture;
	
	public int buy_total_price;
	public double buy_per_square_feet;
	
	public int rent_total_price;
	public double rent_per_square_feet;
	
	public String parking_type;
	public double parking_exchange_area;
	public int parking_total_price;
	
	public double x_lat;
	public double y_long;
	
	public RealEstate(int id, int estate_group, String estate_town, String estate_type, String estate_address,
			double ground_exchange_area, double ground_rent_area, String ground_usage,
			String date_buy, String content_buy, String date_rent, String content_rent,
			String rent_layer, String buy_layer, int building_total_layer,
			String building_type, String main_purpose, String main_material, String date_built, 
			double building_exchange_area, int building_room, int building_sitting_room, int building_rest_room, 
			String is_guarding, String is_having_furniture,
			int buy_total_price, double buy_per_square_feet, int rent_total_price, double rent_per_square_feet,
			String parking_type, double parking_exchange_area, int parking_total_price, 
			double x_lat, double y_long){
		
		this.id = id;
		this.estate_group = estate_group;
		
		this.estate_town = estate_town;
		this.estate_type = estate_type;
		this.estate_address = estate_address;
		
		this.ground_exchange_area = ground_exchange_area;
		this.ground_rent_area = ground_rent_area;
		this.ground_usage = ground_usage;	
		
		this.date_buy = date_buy;
		this.content_buy = content_buy;
		this.date_rent =  date_rent;
		this.content_rent = content_rent;
		
		this.rent_layer = rent_layer;
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
		this.is_having_furniture = is_having_furniture;
		
		this.buy_total_price = buy_total_price;
		this.buy_per_square_feet = buy_per_square_feet;
		this.rent_total_price = rent_total_price;
		this.rent_per_square_feet = rent_per_square_feet;
		
		this.parking_type = parking_type;
		this.parking_exchange_area = parking_exchange_area;
		this.parking_total_price = parking_total_price;
		
		this.x_lat = x_lat;
		this.y_long = y_long;
	}
	
}
