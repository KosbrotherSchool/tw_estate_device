package com.kosbrother.realestate.fragment;

import java.text.NumberFormat;
import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kosbrother.realestate.R;

public class CalculateResultFragment extends Fragment
{
	int mNum;
	private TextView textView;
	private Button calculate_btn;
	private TextView loan_money_show_tv;
	private TextView loan_period_show_tv;
	private TextView p1_rate_show_tv;
	private TextView p1_period_show_tv;

	private TextView p1_money_tv;

	/**
	 * Create a new instance of CountingFragment, providing "num" as an
	 * argument.
	 */

	public CalculateResultFragment()
	{

	}

	public static CalculateResultFragment newInstance(int num)
	{
		CalculateResultFragment f = new CalculateResultFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);

		return f;
	}

	public static CalculateResultFragment newInstance(Bundle bundle)
	{
		CalculateResultFragment f = new CalculateResultFragment();

		f.setArguments(bundle);

		return f;
	}

	/**
	 * When creating, retrieve this instance's number from its arguments.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// mNum = getArguments() != null ? getArguments().getInt("num") : 1;

	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.calculate_result, null);

		loan_money_show_tv = (TextView) v.findViewById(R.id.loan_money_show_tv);
		loan_period_show_tv = (TextView) v
				.findViewById(R.id.loan_period_show_tv);

		p1_rate_show_tv = (TextView) v.findViewById(R.id.p1_rate_show_tv);
		p1_period_show_tv = (TextView) v.findViewById(R.id.p1_period_show_tv);

		p1_money_tv = (TextView) v.findViewById(R.id.p1_money_tv);

		Bundle bundle = getArguments();
		double loan_money = -1;
		double loan_period = -1;
//		double grace_period = -1;
		double p1_rate = -1;
		double p1_period = -1;
//		double p2_rate = -1;
//		double p2_period = -1;
//		double p3_rate = -1;
//		double p3_period = -1;

		if (bundle != null)
		{
			loan_money = bundle.getDouble("loan_money");
			loan_period = bundle.getDouble("loan_period");
//			grace_period = bundle.getDouble("grace_period");
			p1_rate = bundle.getDouble("p1_rate");
			p1_period = bundle.getDouble("p1_period");
//			p2_rate = bundle.getDouble("p2_rate");
//			p2_period = bundle.getDouble("p2_period");
//			p3_rate = bundle.getDouble("p3_rate");
//			p3_period = bundle.getDouble("p3_period");

		}

		Locale locale = new Locale("zh", "TW");
		NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
		fmt.setMaximumFractionDigits(0);
		fmt.setParseIntegerOnly(true);

		loan_money_show_tv.setText(fmt.format(loan_money));
		loan_period_show_tv.setText(String.valueOf(loan_period));
		p1_rate_show_tv.setText(String.valueOf(p1_rate));
		p1_period_show_tv.setText(String.valueOf(p1_period));

		// http://pip.moi.gov.tw/Net/C-Loan/C2.aspx
		// 每月應付本息金額之平均攤還率＝{[(1＋月利率)^月數]×月利率}÷{[(1＋月利率)^月數]－1}
		// (公式中：月利率 ＝ 年利率／12 ； 月數=貸款年限 ｘ 12)
		// 平均每月應攤付本息金額＝貸款本金×每月應付本息金額之平均攤還率
		// 每月應攤還本金與利息試算：
		// 平均每月應攤付本息金額＝貸款本金×每月應付本息金額之平均攤還率＝每月應還本金金額＋
		// 每月應付利息金額
		// 每月應付利息金額＝本金餘額×月利率
		// 每月應還本金金額＝平均每月應攤付本息金額－每月應付利息金額
		// 寬限期應繳利息＝尚未清償本金×月利率

		p1_rate = p1_rate * 0.01;
//		p2_rate = p2_rate * 0.01;
//		p3_rate = p3_rate * 0.01;

		// double rate = Math.pow((1 + 0.02/12), 60) * (0.02/12) / (Math.pow((1
		// + 0.02/12), 60) - 1);
		// double money = 700000 * rate;
		//
		// Log.i("MainActivity", "rate =" + rate + " money =" + money);

//		double rate = Math.pow((1 + p1_rate / 12), loan_period * 12)
//				* (p1_rate / 12)
//				/ (Math.pow((1 + p1_rate / 12), loan_period * 12) - 1);
//		double money = loan_money * rate;
//
//		Log.i("MainActivity", "rate =" + rate + " money =" + money);

		double loan_m = loan_money;

		double money1 = calculate(loan_money, p1_rate, loan_period);

		p1_money_tv.setText(fmt.format(money1));
		return v;
	}

	private double calculate(double m, double r, double p)
	{
		double rate = Math.pow((1 + r / 12), p * 12) * (r / 12)
				/ (Math.pow((1 + r / 12), p * 12) - 1);
		double money = m * rate;

		Log.i("MainActivity", "rate =" + rate + " money =" + money);
		return money;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

	}

}
