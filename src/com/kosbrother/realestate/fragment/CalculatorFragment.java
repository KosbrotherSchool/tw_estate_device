package com.kosbrother.realestate.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosbrother.imageloader.ImageLoader;
import com.kosbrother.realestate.CalculateResultActivity;
import com.kosbrother.realestate.CalculatorActivity;
import com.kosbrother.realestate.Datas;
import com.kosbrother.realestate.MainActivity;
import com.kosbrother.realestate.R;
import com.kosbrother.realestate.api.InfoParserApi;

public class CalculatorFragment extends Fragment implements OnClickListener
{
	int mNum;
	private TextView textView;
	private Button calculate_btn;
	private EditText loan_money_ed;
	private EditText loan_period_ed;
	private EditText p1_rate_ed;
	private EditText p1_period_ed;


	/**
	 * Create a new instance of CountingFragment, providing "num" as an
	 * argument.
	 */

	public CalculatorFragment()
	{

	}

	public static CalculatorFragment newInstance(int num)
	{
		CalculatorFragment f = new CalculatorFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putInt("num", num);
		f.setArguments(args);

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
		View v = inflater.inflate(R.layout.calculator, null);

		loan_money_ed = (EditText) v.findViewById(R.id.loan_money_ed);
		loan_period_ed = (EditText) v.findViewById(R.id.loan_period_ed);

		p1_rate_ed = (EditText) v.findViewById(R.id.p1_rate_ed);
		p1_period_ed = (EditText) v.findViewById(R.id.p1_period_ed);

		calculate_btn = (Button) v.findViewById(R.id.calculate_btn);
		calculate_btn.setOnClickListener(this);
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
		if (v == calculate_btn)
		{
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

			double loan_money = Double.parseDouble(loan_money_ed
					.getEditableText().toString());
			double loan_period = Double.parseDouble(loan_period_ed
					.getEditableText().toString());
			double p1_rate = Double.parseDouble(p1_rate_ed.getEditableText()
					.toString());
			double p1_period = Double.parseDouble(p1_period_ed
					.getEditableText().toString());

			Intent intent = new Intent(getActivity(),
					CalculateResultActivity.class);
//			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			Bundle bundle = new Bundle();
			bundle.putDouble("loan_money", loan_money);
			bundle.putDouble("loan_period", loan_period);
			bundle.putDouble("p1_rate", p1_rate);
			bundle.putDouble("p1_period", p1_period);

			intent.putExtras(bundle);
			startActivity(intent);

		}
	}

}
