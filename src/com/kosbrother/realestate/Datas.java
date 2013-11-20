package com.kosbrother.realestate;

import java.util.ArrayList;
import java.util.Comparator;

import com.kosbrother.realestate.entity.RealEstate;

public class Datas
{

	public static ArrayList<RealEstate> mEstates = new ArrayList<RealEstate>();

	public static class BuyPerSquareComparator implements
			Comparator<RealEstate>
	{

		private int myOrder;

		public BuyPerSquareComparator(int order)
		{
			myOrder = order;
		}

		@Override
		public int compare(RealEstate es1, RealEstate es2)
		{
			if (myOrder == 0)
			{
				return Double.compare(es1.buy_per_square_feet,
						es2.buy_per_square_feet);
			} else
			{
				return -Double.compare(es1.buy_per_square_feet,
						es2.buy_per_square_feet);
			}
		}
	}

	public static class BuyTotalPriceComparator implements
			Comparator<RealEstate>
	{

		private int myOrder;

		public BuyTotalPriceComparator(int order)
		{
			myOrder = order;
		}

		@Override
		public int compare(RealEstate es1, RealEstate es2)
		{
			if (myOrder == 0)
			{
				return Double.compare(es1.buy_total_price, es2.buy_total_price);
			} else
			{
				return -Double
						.compare(es1.buy_total_price, es2.buy_total_price);
			}
		}
	}

	public static class BuildingExchangeAreaComparator implements
			Comparator<RealEstate>
	{

		private int myOrder;

		public BuildingExchangeAreaComparator(int order)
		{
			myOrder = order;
		}

		@Override
		public int compare(RealEstate es1, RealEstate es2)
		{
			if (myOrder == 0)
			{
				return Double.compare(es1.building_exchange_area,
						es2.building_exchange_area);
			} else
			{
				return -Double.compare(es1.building_exchange_area,
						es2.building_exchange_area);
			}
		}
	}

	public static class BuiltDateComparator implements Comparator<RealEstate>
	{

		@Override
		public int compare(RealEstate es1, RealEstate es2)
		{
			return -Double.compare(Integer.valueOf(es1.date_buy),
					Integer.valueOf(es2.date_buy));

		}
	}
}
