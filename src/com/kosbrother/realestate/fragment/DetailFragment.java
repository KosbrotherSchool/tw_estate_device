package com.kosbrother.realestate.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosbrother.imageloader.ImageLoader;
import com.kosbrother.realestate.Datas;
import com.kosbrother.realestate.R;
import com.kosbrother.realestate.api.InfoParserApi;

public class DetailFragment extends Fragment {
    int mNum;
    public ImageLoader  imageLoader;
    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    public static DetailFragment newInstance(int num) {
    	DetailFragment f = new DetailFragment();

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        imageLoader = new ImageLoader(getActivity(), 100);
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_pager, container, false);
        
        ImageView image = (ImageView) v.findViewById(R.id.imageview_detail);
        
        // trade detail
        TextView text_address = (TextView) v.findViewById(R.id.text_detail_address);
        TextView text_date = (TextView) v.findViewById(R.id.text_detail_date);
        TextView text_estate_type = (TextView) v.findViewById(R.id.text_detail_estate_type);
        TextView text_content_buy = (TextView) v.findViewById(R.id.text_detail_content_buy);
        TextView text_ground_exchange_area = (TextView) v.findViewById(R.id.text_detail_ground_exchange_area);
        TextView text_building_exchange_area = (TextView) v.findViewById(R.id.text_detail_building_exchange_area);
        TextView text_buy_per_square_feet = (TextView) v.findViewById(R.id.text_detail_buy_per_square_feet);
        TextView text_buy_total_price = (TextView) v.findViewById(R.id.text_detail_buy_total_price);
        
        // ground detail
        TextView text_estate_town = (TextView) v.findViewById(R.id.text_detail_estate_town);
        TextView text_ground_usage = (TextView) v.findViewById(R.id.text_detail_ground_usage);
        
        // building detail
        TextView text_date_built = (TextView) v.findViewById(R.id.text_detail_date_built);
        TextView text_main_purpose = (TextView) v.findViewById(R.id.text_detail_main_purpose);
        TextView text_building_type = (TextView) v.findViewById(R.id.text_detail_building_type);
        TextView text_main_material = (TextView) v.findViewById(R.id.text_detail_main_material);
        TextView text_buy_layer_building_layer = (TextView) v.findViewById(R.id.text_detail_buy_layer_building_layer);
        TextView text_building_rooms = (TextView) v.findViewById(R.id.text_detail_building_rooms);
        TextView text_is_guarding = (TextView) v.findViewById(R.id.text_detail_is_guarding);
        
        // parking detail
        TextView text_parking_type = (TextView) v.findViewById(R.id.text_detail_parking_type);
        TextView text_parking_exchange_area = (TextView) v.findViewById(R.id.text_detail_parking_exchange_area);
        TextView text_parking_total_price = (TextView) v.findViewById(R.id.text_detail_parking_total_price);
        
        text_address.setText(Datas.mEstates.get(mNum).estate_address);
        text_date.setText(InfoParserApi.parseBuyDate(Datas.mEstates.get(mNum).date_buy));
        text_estate_type.setText(Datas.mEstates.get(mNum).estate_type);
        text_content_buy.setText(Datas.mEstates.get(mNum).content_buy);
        text_ground_exchange_area.setText(InfoParserApi.parseBuildingExchangeArea(Datas.mEstates.get(mNum).ground_exchange_area));
        text_building_exchange_area.setText(InfoParserApi.parseBuildingExchangeArea(Datas.mEstates.get(mNum).building_exchange_area));
        text_buy_per_square_feet.setText(InfoParserApi.parsePerSquareFeetMoney(Datas.mEstates.get(mNum).buy_per_square_feet));
        text_buy_total_price.setText(InfoParserApi.parseTotalBuyMoney(Datas.mEstates.get(mNum).buy_total_price));
        
        text_estate_town.setText(Datas.mEstates.get(mNum).estate_town);
        text_ground_usage.setText(Datas.mEstates.get(mNum).ground_usage);
        
        text_date_built.setText(Datas.mEstates.get(mNum).date_built);
        text_main_purpose.setText(Datas.mEstates.get(mNum).main_purpose);
        text_building_type.setText(Datas.mEstates.get(mNum).building_type);
        text_main_material.setText(Datas.mEstates.get(mNum).main_material);
        text_buy_layer_building_layer.setText(Datas.mEstates.get(mNum).buy_layer + "/" + Integer.toString(Datas.mEstates.get(mNum).building_total_layer));
        text_building_rooms.setText(
    			Integer.toString(Datas.mEstates.get(mNum).building_room)+"房" +
    			Integer.toString(Datas.mEstates.get(mNum).building_sitting_room)+"廳" +
    			Integer.toString(Datas.mEstates.get(mNum).building_rest_room)+"衛浴"
    	);
        text_is_guarding.setText(Datas.mEstates.get(mNum).is_guarding);
        
        text_parking_type.setText(Datas.mEstates.get(mNum).parking_type);
        text_parking_exchange_area.setText(InfoParserApi.parseBuildingExchangeArea(Datas.mEstates.get(mNum).parking_exchange_area));
        text_parking_total_price.setText(InfoParserApi.parseTotalBuyMoney(Datas.mEstates.get(mNum).parking_total_price));
        
        // set image
        String x_lat = Double.toString(Datas.mEstates.get(mNum).x_lat);
        String y_long = Double.toString(Datas.mEstates.get(mNum).y_long);
        String url = "http://maps.google.com/maps/api/staticmap?center="+x_lat+","+ y_long+"&zoom=17&markers=color:red%7Clabel:%7C"+x_lat+","+ y_long+"&size=400x150&language=zh-TW&sensor=false";
        imageLoader.DisplayImage(url, image);
        
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
    }

}

