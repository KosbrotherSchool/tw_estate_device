package com.kosbrother.realestate.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosbrother.imageloader.ImageLoader;
import com.kosbrother.realestate.Datas;
import com.kosbrother.realestate.R;
import com.kosbrother.realestate.api.InfoParserApi;

public class CalculatorFragment extends Fragment implements OnClickListener
{
	int mNum;
	private TextView textView;
	private Button button;

	/**
	 * Create a new instance of CountingFragment, providing "num" as an
	 * argument.
	 */
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
		textView = (TextView) v.findViewById(R.id.text);
		button = (Button) v.findViewById(R.id.button);
		button.setOnClickListener(this);
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
		if (v == button)
		{
			textView.setText("aaaa");
		}
	}

}
