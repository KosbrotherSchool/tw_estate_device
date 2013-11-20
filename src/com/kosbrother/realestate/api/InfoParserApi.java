package com.kosbrother.realestate.api;

public class InfoParserApi
{

	public static String parseHouseAge(String builtDate)
	{
		// 1020222
		String houseAge = "";
		try
		{
			String date = builtDate.substring(5, 7);
			String month = builtDate.substring(3, 5);
			String year = builtDate.substring(0, 3);

			if (year.substring(0, 1).equals("0"))
			{
				year = year.substring(1, 3);
			}

			houseAge = year + "/" + month + "/" + date;
		} catch (Exception e)
		{

		}
		return houseAge;
	}

	public static String parseBuyDate(String buyDate)
	{
		// 10207
		String dd = "";
		try
		{
			String month = buyDate.substring(3, 5);
			String year = buyDate.substring(0, 3);

			if (year.substring(0, 1).equals("0"))
			{
				year = year.substring(1, 3);
			}

			dd = year + "/" + month;
		} catch (Exception e)
		{

		}
		return dd;
	}

	public static String parseBuildingExchangeArea(Double area)
	{
		String dd = "";

		try
		{
			Double area_square_feet = area * 0.3025;
			dd = Double.toString(area_square_feet);
			dd = dd.substring(0, dd.indexOf(".") + 2) + "坪";
		} catch (Exception e)
		{

		}

		return dd;
	}

	public static String parseEstateType(String type)
	{
		// 10207
		String dd = "";

		try
		{
			dd = type.substring(0, type.indexOf("("));
		} catch (Exception e)
		{

		}

		return dd;
	}

	public static String parseTotalBuyMoney(int totalMoney)
	{
		String dd = "";

		try
		{
			String money = Integer.toString(totalMoney);
			int length = money.length();
			dd = money.substring(0, length - 4) + "萬元";
		} catch (Exception e)
		{

		}

		return dd;
	}

	public static String parsePerSquareFeetMoney(double squareMoney)
	{
		String dd = "";

		try
		{
			Double feetMoney = squareMoney * 3.3058;

			dd = Double.toString(feetMoney);
			String s_10k = dd.substring(0, dd.indexOf(".") - 4);
			String s_k = dd.substring(dd.indexOf(".") - 4, dd.indexOf(".") - 3);
			dd = s_10k + "." + s_k + "萬/坪";
		} catch (Exception e)
		{

		}

		return dd;
	}

	public static String parsePerSquareFeetMoney_maker(double squareMoney)
	{
		String dd = "";

		try
		{
			Double feetMoney = squareMoney * 3.3058;

			dd = Double.toString(feetMoney);
			String s_10k = dd.substring(0, dd.indexOf(".") - 4);
			String s_k = dd.substring(dd.indexOf(".") - 4, dd.indexOf(".") - 3);
			dd = "$" + s_10k + "." + s_k;
		} catch (Exception e)
		{

		}

		return dd;
	}

}
