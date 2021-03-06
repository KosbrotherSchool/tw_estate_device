package com.kosbrother.realestate;

import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kosbrother.realestate.api.EstateApi;
import com.kosbrother.realestate.api.InfoParserApi;
import com.kosbrother.realestate.entity.RealEstate;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import at.bartinger.list.item.EntryAdapter;
import at.bartinger.list.item.EntryItem;
import at.bartinger.list.item.Item;
import at.bartinger.list.item.SectionItem;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements
			LocationListener,
			GooglePlayServicesClient.ConnectionCallbacks,
			GooglePlayServicesClient.OnConnectionFailedListener {
	 
	private ActionBarHelper mActionBar;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private DrawerLayout mDrawerLayout;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ListView mDrawerListView;
	private EntryAdapter mDrawerAdapter;
	
	private EditText            search;
    private MenuItem            itemSearch;
    private static final int    ID_SEARCH   = 5;
    
//    private LocationManager mLocationManager;
    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;
//    private LocationRequest mLocationRequest;
    
    // Google Map
    private GoogleMap googleMap;
    static final LatLng NKUT = new LatLng(23.979548, 120.696745);
    
    private LatLng currentLatLng;
    
    public static ArrayList<RealEstate> mEstates = new ArrayList<RealEstate>();
    private LayoutInflater inflater;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);    
        
//        // Create a new global location parameters object
//        mLocationRequest = LocationRequest.create();
//
//        /*
//         * Set the update interval
//         */
//        mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);
//
//        // Use high accuracy
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        // Set the interval ceiling to one minute
//        mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);
        
        
        mActionBar = createActionBarHelper();
		mActionBar.init();
        
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListView = (ListView) findViewById(R.id.left_list_view);
		
        mDrawerLayout.setDrawerListener(new DemoDrawerListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close);	
        
		setDrawerLayout();
		
		mLocationClient = new LocationClient(this, this, this);
	
        try {
            // Loading map
            initilizeMap();
 
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
    private void setDrawerLayout() {
		// TODO Auto-generated method stub
    	items.add(new SectionItem("實價登陸搜尋"));
    	items.add(new EntryItem("位置附近",R.drawable.icon_access_location));
    	items.add(new EntryItem("我的最愛",R.drawable.icon_favorite));
    	items.add(new EntryItem("條件塞選",R.drawable.icon_filter));
    	items.add(new SectionItem("房貸計算"));
    	items.add(new EntryItem("房貸計算機",R.drawable.icon_calculator));
    	items.add(new SectionItem("其他"));
    	items.add(new EntryItem("設定",R.drawable.icon_setting));
    	items.add(new EntryItem("關於我們",R.drawable.icon_about));
    	
    	mDrawerAdapter = new EntryAdapter(MainActivity.this, items);
		mDrawerListView.setAdapter(mDrawerAdapter);
		mDrawerListView.setOnItemClickListener((new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				if(!items.get(position).isSection()){
//		    		EntryItem item = (EntryItem)items.get(position);			    					    		
//		    		Intent intent = new Intent(MainActivity.this, PlaylistVideosActivity.class);  
//		    		intent.putExtra("ListTitle", item.title);  
//		    		intent.putExtra("ListId", item.subtitle);  
//		    		startActivity(intent);  
		    	}
			}
		}));
    	
	}

	/**
     * function to load map. If map is not created it will create it for you
     * */
	private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMap();
//            Marker nkut = googleMap.addMarker(new MarkerOptions().position(NKUT).title("南開科技大學").snippet("數位生活創意系"));
//            CameraPosition cameraPosition = new CameraPosition.Builder().target(NKUT).zoom(12).build();
     
//            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            
            
//            mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
            googleMap.getUiSettings().setZoomControlsEnabled(false);
            googleMap.getUiSettings().setCompassEnabled(false);
            
            googleMap.setInfoWindowAdapter(new InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker marker) {
                	View v = inflater.inflate(R.layout.item_window_info, null);
                	int position = Integer.parseInt(marker.getTitle());
                	
                	TextView textAddress = (TextView) v.findViewById(R.id.text_info_address);
                	
                	TextView textHouseAge = (TextView) v.findViewById(R.id.text_info_house_age);
                	TextView textBuyDate = (TextView) v.findViewById(R.id.text_info_buy_date);
                	
                	TextView textGroundArea = (TextView) v.findViewById(R.id.text_info_ground_area);
                	TextView textEstateType = (TextView) v.findViewById(R.id.text_info_estate_type);
                	
                	TextView textTotalPrice = (TextView) v.findViewById(R.id.text_info_total_price);
                	TextView textBuildingType = (TextView) v.findViewById(R.id.text_info_buiding_type);
                	
                	TextView textBuyPerSquareFeet = (TextView) v.findViewById(R.id.text_info_buy_persquare_feet);
                	TextView textBuyLayer = (TextView) v.findViewById(R.id.text_info_buy_layer);
                	
                	TextView textRooms = (TextView) v.findViewById(R.id.text_info_rooms);
                	TextView textIsGuarding = (TextView) v.findViewById(R.id.text_info_is_guarding);
                	
                	textAddress.setText(mEstates.get(position).estate_address);
                	
                	textHouseAge.setText(InfoParserApi.parseHouseAge(mEstates.get(position).date_built));
                	textBuyDate.setText(InfoParserApi.parseBuyDate(mEstates.get(position).date_buy));
                	
                	textGroundArea.setText(InfoParserApi.parseBuildingExchangeArea(mEstates.get(position).building_exchange_area));
                	textEstateType.setText(InfoParserApi.parseEstateType(mEstates.get(position).estate_type));
                	
                	textTotalPrice.setText(InfoParserApi.parseTotalBuyMoney(mEstates.get(position).buy_total_price));
                	textBuildingType.setText(InfoParserApi.parseEstateType(mEstates.get(position).building_type));
                	
                	textBuyPerSquareFeet.setText(InfoParserApi.parsePerSquareFeetMoney(mEstates.get(position).buy_per_square_feet));
                	textBuyLayer.setText(mEstates.get(position).buy_layer + "/" + Integer.toString(mEstates.get(position).building_total_layer));
                	
                	textRooms.setText(
                			Integer.toString(mEstates.get(position).building_room)+"房" +
                			Integer.toString(mEstates.get(position).building_sitting_room)+"廳" +
                			Integer.toString(mEstates.get(position).building_rest_room)+"衛浴"
                			);
                	textIsGuarding.setText(mEstates.get(position).is_guarding);
                	
                    return v;
                }

                @Override
                public View getInfoContents(Marker arg0) {
                    return null;

                }
            });
            
//            Location currentLoc = googleMap.getMyLocation();
//            LatLng currentLatLng = new LatLng (currentLoc.getLatitude(), currentLoc.getLongitude());
//            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(12).build();
//            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
 
    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);	
		
		itemSearch = menu.add(0, ID_SEARCH, 0, getResources().getString(R.string.menu_search)).setIcon(R.drawable.icon_search)
                .setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    private EditText search;

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        search = (EditText) item.getActionView();
                        search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
                        search.setInputType(InputType.TYPE_CLASS_TEXT);
                        search.requestFocus();
                        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                if (actionId == EditorInfo.IME_ACTION_SEARCH || event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("SearchKeyword", v.getText().toString());
//                                    Intent intent = new Intent();
//                                    intent.setClass(MainActivity.this, SearchActivity.class);
//                                    intent.putExtras(bundle);
//                                    startActivity(intent);
//                                    itemSearch.collapseActionView();
                                    return true;
                                }
                                return false;
                            }
                        });
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(null, InputMethodManager.SHOW_IMPLICIT);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // TODO Auto-generated method stub
                        search.setText("");
                        return true;
                    }
                }).setActionView(R.layout.collapsible_edittext);
        itemSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
		
        getMenuInflater().inflate(R.menu.main, menu);
        
		return super.onCreateOptionsMenu(menu);
	}
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }else{
			switch (item.getItemId()) {
			case ID_SEARCH:
				Toast.makeText(MainActivity.this, "search", Toast.LENGTH_SHORT).show();
				break;
			case R.id.menu_list:
				Toast.makeText(MainActivity.this, "list", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ListActivity.class);
//              intent.putExtras(bundle);
				startActivity(intent);
				break;
			}
        }
		return super.onOptionsItemSelected(item);
	}
    
    private ActionBarHelper createActionBarHelper() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return new ActionBarHelperICS();
		} else {
			return new ActionBarHelper();
		}
	}

	private class ActionBarHelper {
		public void init() {}
		public void onDrawerClosed() {}
		public void onDrawerOpened() {}
		public void setTitle(CharSequence title) {}
	}
	
	private class ActionBarHelperICS extends ActionBarHelper {
		private final ActionBar mActionBar;
		private CharSequence mDrawerTitle;
		private CharSequence mTitle;

		ActionBarHelperICS() {
			mActionBar = getActionBar();
		}

		@Override
		public void init() {
			mActionBar.setDisplayHomeAsUpEnabled(true);
			mActionBar.setHomeButtonEnabled(true);
			mTitle = mDrawerTitle = getTitle();
		}

		@Override
		public void onDrawerClosed() {
			super.onDrawerClosed();
			mActionBar.setTitle(mTitle);
		}

		@Override
		public void onDrawerOpened() {
			super.onDrawerOpened();
			mActionBar.setTitle(mDrawerTitle);
		}

		@Override
		public void setTitle(CharSequence title) {
			mTitle = title;
		}
	}
	
	private class DemoDrawerListener implements DrawerLayout.DrawerListener {
		@Override
		public void onDrawerOpened(View drawerView) {
			mDrawerToggle.onDrawerOpened(drawerView);
			mActionBar.onDrawerOpened();
		}

		@Override
		public void onDrawerClosed(View drawerView) {
			mDrawerToggle.onDrawerClosed(drawerView);
			mActionBar.onDrawerClosed();
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerStateChanged(int newState) {
			mDrawerToggle.onDrawerStateChanged(newState);
		}
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
	
	private void getLocation() {

        // If Google Play Services is available
        if (servicesConnected()) {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();
            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            
            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(14).build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            
            new GetEstatesTask().execute();
            // Display the current location in the UI
//            mLatLng.setText(LocationUtils.getLatLng(this, currentLocation));
        }
    }
	
	/**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));

            // Continue
            return true;
        // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
            }
            return false;
        }
    }
	
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /**
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
    
    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
//        mConnectionStatus.setText(R.string.connected);
    		
    	getLocation();
//        if (mUpdatesRequested) {
//            startPeriodicUpdates();
//        }
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
//        mConnectionStatus.setText(R.string.disconnected);
    }
    
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */

            } catch (IntentSender.SendIntentException e) {

                // Log the error
                e.printStackTrace();
            }
        } else {

            // If no resolution is available, display a dialog to the user with the error.
            showErrorDialog(connectionResult.getErrorCode());
        }
    }
    
    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
            errorCode,
            this,
            LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
        }
    }
	
    @Override
    public void onLocationChanged(Location location) {

        // Report to the UI that the location was updated
//        mConnectionStatus.setText(R.string.location_updated);
//
//        // In the UI, set the latitude and longitude to the value received
//        mLatLng.setText(LocationUtils.getLatLng(this, location));
    }
    
    @Override
    public void onStart() {

        super.onStart();

        /*
         * Connect the client. Don't re-start any requests here;
         * instead, wait for onResume()
         */
        mLocationClient.connect();

    }
    
    @Override
    public void onStop() {

//        // If the client is connected
        if (mLocationClient.isConnected()) {
            stopPeriodicUpdates();
        }
//
//        // After disconnect() is called, the client is considered "dead".
        mLocationClient.disconnect();

        super.onStop();
    }
    
//    private void startPeriodicUpdates() {
//
//        mLocationClient.requestLocationUpdates(mLocationRequest, this);
////        mConnectionState.setText(R.string.location_requested);
//    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(this);
//        mConnectionState.setText(R.string.location_updates_stopped);
    }
    
    protected class GetEstatesTask extends AsyncTask {

        @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			mEstates = EstateApi.getAroundAll(currentLatLng.latitude, currentLatLng.longitude);

			return null;
		}
		
		@Override
		protected void onPostExecute(Object result) {
//			super.onPostExecute(result);
			for(int i=0; i<mEstates.size(); i++ ){
				LatLng newLatLng = new LatLng(mEstates.get(i).x_lat,mEstates.get(i).y_long);
				View layout = inflater.inflate(R.layout.item_marker,null);   
				layout.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT     
		           ));   
				ImageView markerView = (ImageView) layout.findViewById(R.id.image_marker);
				TextView  markerText = (TextView) layout.findViewById(R.id.text_marker_price);
				
				//for later marker info window use
				MarkerOptions marker = new MarkerOptions().position(newLatLng).title(Integer.toString(i));
				markerText.setText(InfoParserApi.parsePerSquareFeetMoney_maker(mEstates.get(i).buy_per_square_feet));
				
				if(mEstates.get(i).estate_group==1){
					markerView.setImageResource(R.drawable.marker_red);
					marker.snippet("estate");
				}else if(mEstates.get(i).estate_group==2){
					markerView.setImageResource(R.drawable.marker_green);
					marker.snippet("rent");
//					googleMap.addMarker(new MarkerOptions().position(newLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green)));
				}else if(mEstates.get(i).estate_group==3){
					markerView.setImageResource(R.drawable.marker_presale);
					marker.snippet("pre_sale");
//					googleMap.addMarker(new MarkerOptions().position(newLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_presale)));
				}
				
				Bitmap bm = loadBitmapFromView(layout);
				
				// Changing marker icon
				marker.icon(BitmapDescriptorFactory.fromBitmap(bm));
				
				// adding marker
				googleMap.addMarker(marker);
			}

		}
    }
    
    public static Bitmap loadBitmapFromView(View v) {
    	if (v.getMeasuredHeight() <= 0) {
    	    v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    	    Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
    	    Canvas c = new Canvas(b);
    	    v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    	    v.draw(c);
    	    return b;
    	}
    	
        Bitmap b = Bitmap.createBitmap( v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);                
        Canvas c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }
}