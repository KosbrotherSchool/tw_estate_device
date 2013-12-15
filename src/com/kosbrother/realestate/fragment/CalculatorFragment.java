package com.kosbrother.realestate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kosbrother.realestate.R;

//http://pip.moi.gov.tw/Net/C-Loan/C2.aspx
// 每月應付本息金額之平均攤還率＝{[(1＋月利率)^月數]×月利率}÷{[(1＋月利率)^月數]－1}
// (公式中：月利率 ＝ 年利率／12 ； 月數=貸款年限 ｘ 12)
// 平均每月應攤付本息金額＝貸款本金×每月應付本息金額之平均攤還率
// 每月應攤還本金與利息試算：
// 平均每月應攤付本息金額＝貸款本金×每月應付本息金額之平均攤還率＝每月應還本金金額＋
// 每月應付利息金額
// 每月應付利息金額＝本金餘額×月利率
// 每月應還本金金額＝平均每月應攤付本息金額－每月應付利息金額
// 寬限期應繳利息＝尚未清償本金×月利率

public class CalculatorFragment extends Fragment implements OnClickListener
{

	private Button btn_calclate;
	private EditText editLoanMoney;
	private EditText editLoanRate;
	private EditText editLoanPeriod;
	private TextView textReturnMonthMoney;
	private TextView textReturnTotalRateMoney;
	private TextView textReturnTotalMoney;
	private InputMethodManager imm;
	
	/**
	 * Create a new instance of CountingFragment, providing "num" as an
	 * argument.
	 */

	public CalculatorFragment()
	{

	}

	public static CalculatorFragment newInstance()
	{
		CalculatorFragment f = new CalculatorFragment();
		
		
		return f;
	}

	/**
	 * The Fragment's UI is just a simple text view showing its instance number.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.calculator, null);

		editLoanMoney = (EditText) v.findViewById(R.id.edit_loan_money);
		editLoanRate = (EditText) v.findViewById(R.id.edit_loan_rate);
		editLoanPeriod = (EditText) v.findViewById(R.id.edit_loan_period);

		btn_calclate = (Button) v.findViewById(R.id.btn_calculate);
		btn_calclate.setOnClickListener(this);

		textReturnMonthMoney = (TextView) v.findViewById(R.id.text_return_month_money);
		textReturnTotalRateMoney = (TextView) v.findViewById(R.id.text_return_total_rate_money);
		textReturnTotalMoney = (TextView) v.findViewById(R.id.text_return_total_money);
		
		imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
	    
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onClick(View v)
	{
		if (v == btn_calclate)
		{
			int loanMoney;
			double loanRate;
			double loanPeriod;
			int moneyPerMonth;
			int moneyTotalRate;
			int moneyTotal;
			
			imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
			
			if (editLoanMoney.getText().toString().equals(""))
			{
				Toast.makeText(getActivity(), "請輸入貸款金額", Toast.LENGTH_SHORT).show();
			} else
			{
				loanMoney = Integer.parseInt(editLoanMoney.getText().toString()) * 10000;
				loanRate = Double.parseDouble(editLoanRate.getText().toString()) * 0.01;
				loanPeriod = Double.parseDouble(editLoanPeriod.getText().toString());

				moneyPerMonth = (int) (calculate(loanMoney, loanRate, loanPeriod));
				moneyTotal = (int) (moneyPerMonth * 12 * loanPeriod);
				moneyTotalRate = moneyTotal - loanMoney;

				textReturnMonthMoney.setText(Integer.toString(moneyPerMonth));
				textReturnTotalRateMoney.setText(Integer.toString(moneyTotalRate));
				textReturnTotalMoney.setText(Integer.toString(moneyTotal));

			}

		}
	}

	private double calculate(double loan_money, double loan_rate, double loan_period)
	{
		double rate = Math.pow((1 + loan_rate / 12), loan_period * 12) * (loan_rate / 12)
				/ (Math.pow((1 + loan_rate / 12), loan_period * 12) - 1);
		double money = loan_money * rate;

		Log.i("MainActivity", "rate =" + rate + " money =" + money);
		return money;
	}

}
