package com.kosbrother.realestate;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.text.style.SuperscriptSpan;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;
import at.bartinger.list.item.EntryAdapter;
import at.bartinger.list.item.EntryItem;
import at.bartinger.list.item.Item;
import at.bartinger.list.item.SectionItem;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kosbrother.realestate.api.EstateApi;
import com.kosbrother.realestate.api.InfoParserApi;
import com.kosbrother.realestate.api.Setting;
import com.kosbrother.realestate.fragment.TouchableWrapper.UpdateMapActionDown;
import com.kosbrother.realestate.fragment.TouchableWrapper.UpdateMapAfterUserInterection;
import com.kosbrother.realestate.fragment.TransparentSupportMapFragment;

public class MainActivity extends SherlockFragmentActivity implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, UpdateMapAfterUserInterection,
		UpdateMapActionDown
{

	// private ActionBarHelper mActionBar;
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ListView mDrawerListView;
	private EntryAdapter mDrawerAdapter;

	private MenuItem itemSearch;
	private static final int ID_SEARCH = 5;

	// private LocationManager mLocationManager;
	// Stores the current instantiation of the location client in this object
	private LocationClient mLocationClient;
	// private LocationRequest mLocationRequest;

	// Google Map
	private GoogleMap googleMap;

	// private LatLng currentLatLng;

	private LayoutInflater inflater;
	private ImageButton btnFocusButton;
	private ImageButton btnLayerButton;
	private ImageButton btnFilterButton;
	private int currentMapTypePosition = 0;
	private LinearLayout leftDrawer;

	// private TransparentSupportMapFragment mMapFragment;
	private boolean isRunAsync = true;
	private List<Integer> markerIntArray = new ArrayList<Integer>();
	// private MapWrapperLayout mWrapperLayout;

	private int screenWidth;
	private int screenHeight;

	private ArrayList<Marker> mMarkers = new ArrayList<Marker>();
	private Marker currentMarker;

	private ProgressDialog mProgressDialog;

	private RelativeLayout adBannerLayout;
	private AdView adMobAdView;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_layout);
		// mWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_wrapper);

		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		screenHeight = getWindowManager().getDefaultDisplay().getHeight();

		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListView = (ListView) findViewById(R.id.left_list_view);
		btnFocusButton = (ImageButton) findViewById(R.id.image_btn_focus);
		btnLayerButton = (ImageButton) findViewById(R.id.image_btn_layers);
		btnFilterButton = (ImageButton) findViewById(R.id.image_btn_filter);
		leftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		// mMapFragment = (TransparentSupportMapFragment)
		// getSupportFragmentManager()
		// .findFragmentById(R.id.map);

		btnFocusButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!mLocationClient.isConnected())
				{
					mLocationClient.connect();
				} else
				{
					Location currentLocation = mLocationClient.getLastLocation();
					if (currentLocation != null)
					{
						Constants.currentLatLng = new LatLng(currentLocation.getLatitude(),
								currentLocation.getLongitude());
					} else
					{
						LatLng newLatLng = Setting.getLastCenter(MainActivity.this);
						if (newLatLng.latitude != 0.0)
						{
							Constants.currentLatLng = newLatLng;
						} else
						{
							Constants.currentLatLng = new LatLng(25.0478, 121.5172);
						}
					}

					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(Constants.currentLatLng).zoom(14).build();
					googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				}
			}
		});

		btnLayerButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				// Set the dialog title
				builder.setTitle("順序排列").setSingleChoiceItems(R.array.map_type,
						currentMapTypePosition, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int position)
							{
								setMapTypeByPosition(position);
								currentMapTypePosition = position;
								dialog.cancel();
							}

							private void setMapTypeByPosition(int position)
							{
								switch (position)
								{
								case 0:
									googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
									break;
								case 1:
									googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
									break;
								case 2:
									googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
									break;
								default:
									break;
								}

							}
						});
				builder.show();

			}
		});

		btnFilterButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showFilterDialog();
			}
		});

		// mDrawerLayout.setDrawerListener(new DemoDrawerListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer,
				R.string.drawer_open, R.string.drawer_close)
		{
			public void onDrawerClosed(View view)
			{
				// getSupportActionBar().setTitle(mTitle);
				supportInvalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView)
			{
				// getSupportActionBar().setTitle(mDrawerTitle);
				supportInvalidateOptionsMenu(); // creates call to
												// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		setDrawerLayout();

		mLocationClient = new LocationClient(this, this, this);

		try
		{
			// Loading map
			initilizeMap();
			// mWrapperLayout.init(googleMap, getPixelsFromDp(this, 39 + 20));
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		CallAds();
	}

	/**
	 * function to load map. If map is not created it will create it for you
	 * */
	private void initilizeMap()
	{
		if (googleMap == null)
		{
			googleMap = ((TransparentSupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();

			// mLocationManager = (LocationManager)
			// getSystemService(LOCATION_SERVICE);
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setMyLocationButtonEnabled(false);
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.getUiSettings().setCompassEnabled(false);

			if (googleMap == null)
			{
				Toast.makeText(getApplicationContext(), "Sorry! unable to create maps",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		// initilizeMap();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		itemSearch = menu.add(0, ID_SEARCH, 0, getResources().getString(R.string.menu_search))
				.setIcon(R.drawable.icon_search_white)
				.setOnActionExpandListener(new MenuItem.OnActionExpandListener()
				{
					private EditText search;

					@Override
					public boolean onMenuItemActionExpand(MenuItem item)
					{
						search = (EditText) item.getActionView();
						search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
						search.setInputType(InputType.TYPE_CLASS_TEXT);
						search.requestFocus();
						search.setOnEditorActionListener(new TextView.OnEditorActionListener()
						{
							@Override
							public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
							{
								if (actionId == EditorInfo.IME_ACTION_SEARCH
										|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
								{
									String inputString = v.getText().toString();
									Geocoder geocoder = new Geocoder(MainActivity.this);
									List<Address> addresses = null;
									Address address = null;
									InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
									try
									{
										addresses = geocoder.getFromLocationName(inputString, 1);
									} catch (Exception e)
									{
										Log.e("MainActivity", e.toString());
									}
									if (addresses == null || addresses.isEmpty())
									{
										Toast.makeText(MainActivity.this, "無此地點",
												Toast.LENGTH_SHORT).show();
									} else
									{
										address = addresses.get(0);
										double geoLat = address.getLatitude();
										double geoLong = address.getLongitude();
										Constants.currentLatLng = new LatLng(geoLat, geoLong);
										googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
												new LatLng(Constants.currentLatLng.latitude,
														Constants.currentLatLng.longitude), 16.0f));
									}
									return true;
								}
								return false;
							}
						});
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT);
						return true;
					}

					@Override
					public boolean onMenuItemActionCollapse(MenuItem item)
					{

						search.setText("");
						return true;
					}
				}).setActionView(R.layout.collapsible_edittext);
		itemSearch.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS
				| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

		getSupportMenuInflater().inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item)
	{
		if (mDrawerToggle.onOptionsItemSelected(getMenuItem(item)))
		{
			return true;
		} else
		{
			switch (item.getItemId())
			{
			case ID_SEARCH:
				// Toast.makeText(MainActivity.this, "search",
				// Toast.LENGTH_SHORT).show();
				break;
			case R.id.menu_list:
				// Toast.makeText(MainActivity.this, "list",
				// Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ListActivity.class);
				// intent.putExtras(bundle);
				startActivity(intent);
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private android.view.MenuItem getMenuItem(final MenuItem item)
	{
		return new android.view.MenuItem()
		{
			@Override
			public int getItemId()
			{
				return item.getItemId();
			}

			public boolean isEnabled()
			{
				return true;
			}

			@Override
			public boolean collapseActionView()
			{
				return false;
			}

			@Override
			public boolean expandActionView()
			{
				return false;
			}

			@Override
			public ActionProvider getActionProvider()
			{
				return null;
			}

			@Override
			public View getActionView()
			{
				return null;
			}

			@Override
			public char getAlphabeticShortcut()
			{
				return 0;
			}

			@Override
			public int getGroupId()
			{
				return 0;
			}

			@Override
			public Drawable getIcon()
			{
				return null;
			}

			@Override
			public Intent getIntent()
			{
				return null;
			}

			@Override
			public ContextMenuInfo getMenuInfo()
			{
				return null;
			}

			@Override
			public char getNumericShortcut()
			{
				return 0;
			}

			@Override
			public int getOrder()
			{
				return 0;
			}

			@Override
			public SubMenu getSubMenu()
			{
				return null;
			}

			@Override
			public CharSequence getTitle()
			{
				return null;
			}

			@Override
			public CharSequence getTitleCondensed()
			{
				return null;
			}

			@Override
			public boolean hasSubMenu()
			{
				return false;
			}

			@Override
			public boolean isActionViewExpanded()
			{
				return false;
			}

			@Override
			public boolean isCheckable()
			{
				return false;
			}

			@Override
			public boolean isChecked()
			{

				return false;
			}

			@Override
			public boolean isVisible()
			{
				return false;
			}

			@Override
			public android.view.MenuItem setActionProvider(ActionProvider actionProvider)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(View view)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(int resId)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setAlphabeticShortcut(char alphaChar)
			{

				return null;
			}

			@Override
			public android.view.MenuItem setCheckable(boolean checkable)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setChecked(boolean checked)
			{

				return null;
			}

			@Override
			public android.view.MenuItem setEnabled(boolean enabled)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(Drawable icon)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(int iconRes)
			{

				return null;
			}

			@Override
			public android.view.MenuItem setIntent(Intent intent)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setNumericShortcut(char numericChar)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setOnActionExpandListener(OnActionExpandListener listener)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setOnMenuItemClickListener(
					OnMenuItemClickListener menuItemClickListener)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setShortcut(char numericChar, char alphaChar)
			{
				return null;
			}

			@Override
			public void setShowAsAction(int actionEnum)
			{

			}

			@Override
			public android.view.MenuItem setShowAsActionFlags(int actionEnum)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(CharSequence title)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(int title)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setTitleCondensed(CharSequence title)
			{
				return null;
			}

			@Override
			public android.view.MenuItem setVisible(boolean visible)
			{
				return null;
			}
		};
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	private void getLocation()
	{

		// If Google Play Services is available
		if (servicesConnected())
		{

			// Get the current location
			Location currentLocation = mLocationClient.getLastLocation();
			if (currentLocation != null)
			{
				Constants.currentLatLng = new LatLng(currentLocation.getLatitude(),
						currentLocation.getLongitude());
			} else
			{
				LatLng newLatLng = Setting.getLastCenter(MainActivity.this);
				if (newLatLng.latitude != 0.0)
				{
					Constants.currentLatLng = newLatLng;
				} else
				{
					Constants.currentLatLng = new LatLng(25.0478, 121.5172);
				}
			}

			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
					Constants.currentLatLng.latitude, Constants.currentLatLng.longitude), 16.0f));

			googleMap.setOnMarkerClickListener(new OnMarkerClickListener()
			{

				@Override
				public boolean onMarkerClick(Marker marker)
				{
					isRunAsync = false;

					// marker.showInfoWindow();
					Projection projection = googleMap.getProjection();
					LatLng markerLocation = marker.getPosition();
					Point screenPosition = projection.toScreenLocation(markerLocation);

					// check around houses and make a highlight array
					// 1. same address
					// 2. very near
					markerIntArray.clear();
					int markerPosition = Integer.parseInt(marker.getTitle());
					markerIntArray.add(markerPosition);

					String addressString = Datas.mEstates.get(markerPosition).estate_address;
					for (int i = 0; i < Datas.mEstates.size(); i++)
					{
						if (Datas.mEstates.get(i).estate_address.equals(addressString)
								&& i != markerPosition)
						{
							markerIntArray.add(i);
						}
					}
					Log.i("MainActivty", "markerIntArray size = " + markerIntArray.size());

					showPopupWindow(screenPosition.x, screenPosition.y);

					for (Marker item : mMarkers)
					{
						if (item.getTitle().equals(marker.getTitle()))
						{
							currentMarker = item;
							View layout = inflater.inflate(R.layout.item_marker, null);
							layout.setLayoutParams(new LinearLayout.LayoutParams(
									LinearLayout.LayoutParams.WRAP_CONTENT,
									LinearLayout.LayoutParams.WRAP_CONTENT));
							ImageView markerView = (ImageView) layout
									.findViewById(R.id.image_marker);
							TextView markerText = (TextView) layout
									.findViewById(R.id.text_marker_price);
							markerView.setImageResource(R.drawable.icon_selected);
							markerText.setText(InfoParserApi
									.parsePerSquareFeetMoney_maker(Datas.mEstates
											.get(markerPosition).buy_per_square_feet));
							Bitmap bm = loadBitmapFromView(layout);
							// Changing marker icon
							item.setIcon(BitmapDescriptorFactory.fromBitmap(bm));
						}
					}

					return true;
				}
			});

			googleMap.setOnCameraChangeListener(new OnCameraChangeListener()
			{

				@Override
				public void onCameraChange(CameraPosition position)
				{
					if (isRunAsync)
					{
						// Toast.makeText(MainActivity.this,
						// "Map is Not Touched", Toast.LENGTH_SHORT)
						// .show();

						double lat = position.target.latitude;
						double lng = position.target.longitude;

						LatLng bottomLeft = googleMap.getProjection().getVisibleRegion().nearLeft;
						LatLng bottomRight = googleMap.getProjection().getVisibleRegion().nearRight;
						LatLng topLeft = googleMap.getProjection().getVisibleRegion().farLeft;
						LatLng topRight = googleMap.getProjection().getVisibleRegion().farRight;

						LatLng spot1 = new LatLng(topLeft.latitude * 3 / 4 + lat * 1 / 4,
								topLeft.longitude * 3 / 4 + lng * 1 / 4);
						LatLng topCenter = new LatLng(topLeft.latitude * 1 / 2 + topRight.latitude
								* 1 / 2, topLeft.longitude * 1 / 2 + topRight.longitude * 1 / 2);
						LatLng spot2 = new LatLng(topCenter.latitude * 3 / 4 + lat * 1 / 4,
								topCenter.longitude * 3 / 4 + lng * 1 / 4);
						LatLng spot3 = new LatLng(topRight.latitude * 3 / 4 + lat * 1 / 4,
								topRight.longitude * 3 / 4 + lng * 1 / 4);
						LatLng leftCenter = new LatLng(topLeft.latitude * 1 / 2
								+ bottomLeft.latitude * 1 / 2, topLeft.longitude * 1 / 2
								+ bottomLeft.longitude * 1 / 2);
						LatLng spot4 = new LatLng(leftCenter.latitude * 3 / 4 + lat * 1 / 4,
								leftCenter.longitude * 3 / 4 + lng * 1 / 4);
						LatLng RightCenter = new LatLng(topRight.latitude * 1 / 2
								+ bottomRight.latitude * 1 / 2, topRight.longitude * 1 / 2
								+ bottomRight.longitude * 1 / 2);
						LatLng spot5 = new LatLng(RightCenter.latitude * 3 / 4 + lat * 1 / 4,
								RightCenter.longitude * 3 / 4 + lng * 1 / 4);
						LatLng spot6 = new LatLng(bottomLeft.latitude * 3 / 4 + lat * 1 / 4,
								bottomLeft.longitude * 3 / 4 + lng * 1 / 4);
						LatLng BottomCenter = new LatLng(bottomLeft.latitude * 1 / 2
								+ bottomRight.latitude * 1 / 2, bottomLeft.longitude * 1 / 2
								+ bottomRight.longitude * 1 / 2);
						LatLng spot7 = new LatLng(BottomCenter.latitude * 3 / 4 + lat * 1 / 4,
								BottomCenter.longitude * 3 / 4 + lng * 1 / 4);
						LatLng spot8 = new LatLng(bottomRight.latitude * 3 / 4 + lat * 1 / 4,
								bottomRight.longitude * 3 / 4 + lng * 1 / 4);

						LatLng[] latLngs = { position.target, spot1, spot2, spot3, spot4, spot5,
								spot6, spot7, spot8 };

						new GetEstatesTask().execute(latLngs);
					}
				}
			});

		}
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
		// LatLng centerLatLng = googleMap.getCameraPosition().target;
		// Setting.saveLastCenter(centerLatLng.latitude, centerLatLng.longitude,
		// MainActivity.this);
		finish();
		Datas.mEstates.clear();
	}

	/**
	 * Verify that Google Play services is available before making a request.
	 * 
	 * @return true if Google Play services is available, otherwise false
	 */
	private boolean servicesConnected()
	{

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode)
		{
			// In debug mode, log the status
			Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else
		{
			// Display an error dialog
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
			if (dialog != null)
			{
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
			}
			return false;
		}
	}

	public static class ErrorDialogFragment extends DialogFragment
	{

		// Global field to contain the error dialog
		private Dialog mDialog;

		/**
		 * Default constructor. Sets the dialog field to null
		 */
		public ErrorDialogFragment()
		{
			super();
			mDialog = null;
		}

		/**
		 * Set the dialog to display
		 * 
		 * @param dialog
		 *            An error dialog
		 */
		public void setDialog(Dialog dialog)
		{
			mDialog = dialog;
		}

		/*
		 * This method must return a Dialog to the DialogFragment.
		 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState)
		{
			return mDialog;
		}
	}

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle bundle)
	{
		// mConnectionStatus.setText(R.string.connected);

		if (Datas.mEstates == null || Datas.mEstates.size() == 0)
			getLocation();
		// if (mUpdatesRequested) {
		// startPeriodicUpdates();
		// }
	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected()
	{
		// mConnectionStatus.setText(R.string.disconnected);
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult)
	{

		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution())
		{
			try
			{
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e)
			{
				// Log the error
				e.printStackTrace();
			}
		} else
		{
			// If no resolution is available, display a dialog to the user with
			// the error.
			showErrorDialog(connectionResult.getErrorCode());
		}
	}

	private void showErrorDialog(int errorCode)
	{

		// Get the error dialog from Google Play services
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this,
				LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

		// If Google Play services can provide an error dialog
		if (errorDialog != null)
		{

			// Create a new DialogFragment in which to show the error dialog
			ErrorDialogFragment errorFragment = new ErrorDialogFragment();

			// Set the dialog in the DialogFragment
			errorFragment.setDialog(errorDialog);

			// Show the error dialog in the DialogFragment
			errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
		}
	}

	@Override
	public void onLocationChanged(Location location)
	{

		// Report to the UI that the location was updated
		// mConnectionStatus.setText(R.string.location_updated);
		//
		// // In the UI, set the latitude and longitude to the value received
		// mLatLng.setText(LocationUtils.getLatLng(this, location));
	}

	@Override
	public void onStart()
	{

		super.onStart();

		EasyTracker.getInstance(this).activityStart(this); // Add this method.

		/*
		 * Connect the client. Don't re-start any requests here; instead, wait
		 * for onResume()
		 */
		if (!mLocationClient.isConnected())
		{
			mLocationClient.connect();
		}

	}

	@Override
	public void onStop()
	{

		// // If the client is connected
		if (mLocationClient.isConnected())
		{
			stopPeriodicUpdates();
		}
		//
		// // After disconnect() is called, the client is considered "dead".
		mLocationClient.disconnect();

		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

	// private void startPeriodicUpdates() {
	//
	// mLocationClient.requestLocationUpdates(mLocationRequest, this);
	// // mConnectionState.setText(R.string.location_requested);
	// }

	/**
	 * In response to a request to stop updates, send a request to Location
	 * Services
	 */
	private void stopPeriodicUpdates()
	{
		mLocationClient.removeLocationUpdates(this);
		// mConnectionState.setText(R.string.location_updates_stopped);
	}

	protected class GetEstatesTask extends AsyncTask<LatLng, Void, Void>
	{

		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			mProgressDialog = ProgressDialog.show(MainActivity.this, null, "資料傳遞中");
			mProgressDialog.setCancelable(true);
		}

		@Override
		protected Void doInBackground(LatLng... latLngs)
		{
			try
			{
				Datas.mEstates.clear();
			} catch (Exception e)
			{
				// TODO: handle exception
			}

			Datas.mEstates = EstateApi.getAroundAllByAreas(latLngs, Constants.isSaledMarkerShow,
					Constants.isRentMarkerShow, Constants.isPreSaleMarkerShow,
					Constants.salePerSquareMin, Constants.salePerSquareMax, Constants.saleTotalMin,
					Constants.saleTotalMax, Constants.saleAreaMin, Constants.saleAreaMax);

			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			googleMap.clear();
			if (mProgressDialog.isShowing())
			{
				mProgressDialog.dismiss();
			}
			// super.onPostExecute(result);
			if (Datas.mEstates != null && Datas.mEstates.size() != 0)
			{
				mMarkers.clear();

				// Toast.makeText(MainActivity.this, "附近有 "+
				// Integer.toString(Datas.mEstates.size()) +" 筆資料" ,
				// Toast.LENGTH_SHORT).show();

				for (int i = 0; i < Datas.mEstates.size(); i++)
				{
					LatLng newLatLng = new LatLng(Datas.mEstates.get(i).x_lat,
							Datas.mEstates.get(i).y_long);
					View layout = inflater.inflate(R.layout.item_marker, null);
					layout.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
					ImageView markerView = (ImageView) layout.findViewById(R.id.image_marker);
					TextView markerText = (TextView) layout.findViewById(R.id.text_marker_price);

					// for later marker info window use
					MarkerOptions marker = new MarkerOptions().position(newLatLng).title(
							Integer.toString(i));
					markerText.setText(InfoParserApi.parsePerSquareFeetMoney_maker(Datas.mEstates
							.get(i).buy_per_square_feet));

					if (Datas.mEstates.get(i).estate_group == 1)
					{
						markerView.setImageResource(R.drawable.marker_sale);
						marker.snippet("estate");
					} else if (Datas.mEstates.get(i).estate_group == 3)
					{
						markerView.setImageResource(R.drawable.marker_rent);
						marker.snippet("rent");
					} else if (Datas.mEstates.get(i).estate_group == 2)
					{
						markerView.setImageResource(R.drawable.marker_presell);
						marker.snippet("pre_sale");
					}

					Bitmap bm = loadBitmapFromView(layout);

					// Changing marker icon
					marker.icon(BitmapDescriptorFactory.fromBitmap(bm));

					// adding marker
					Marker theMarker = googleMap.addMarker(marker);
					mMarkers.add(theMarker);
				}
			} else
			{
				Toast.makeText(MainActivity.this, "No Data!!", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static Bitmap loadBitmapFromView(View v)
	{
		if (v.getMeasuredHeight() <= 0)
		{
			v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(),
					Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b);
			v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
			v.draw(c);
			return b;
		}

		Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height,
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
		v.draw(c);
		return b;
	}

	@Override
	public void onUpdateMapAfterUserInterection()
	{
		isRunAsync = true;
	}

	@Override
	public void onUpdateMapActionDown()
	{
		isRunAsync = false;
	}

	public static int getPixelsFromDp(Context context, float dp)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	public void showPopupWindow(int x, int y)
	{
		final int positionArraySize = markerIntArray.size();

		LinearLayout v = (LinearLayout) inflater.inflate(R.layout.item_window_info, null);
		final LinearLayout layoutInfo = (LinearLayout) v.findViewById(R.id.linear_window_info);
		final TextView textPosition = (TextView) v.findViewById(R.id.text_info_position);
		textPosition.setText(Integer.toString(0));

		final TextView textNumTotalNum = (TextView) v.findViewById(R.id.text_num_total_num);
		textNumTotalNum.setText("1/" + Integer.toString(positionArraySize));

		ImageButton buttonBack = (ImageButton) v.findViewById(R.id.button_info_back);
		ImageButton buttonForward = (ImageButton) v.findViewById(R.id.button_info_forward);

		if (positionArraySize == 1)
		{
			buttonBack.setVisibility(View.GONE);
			buttonForward.setVisibility(View.GONE);
		} else
		{
			buttonBack.setVisibility(View.VISIBLE);
			buttonForward.setVisibility(View.VISIBLE);
		}

		final TextView textAddress = (TextView) v.findViewById(R.id.text_info_address);

		final TextView textHouseAge = (TextView) v.findViewById(R.id.text_info_house_age);
		final TextView textBuyDate = (TextView) v.findViewById(R.id.text_info_buy_date);

		final TextView textGroundArea = (TextView) v.findViewById(R.id.text_info_ground_area);
		final TextView textEstateType = (TextView) v.findViewById(R.id.text_info_estate_type);

		final TextView textTotalPrice = (TextView) v.findViewById(R.id.text_info_total_price);
		final TextView textBuildingType = (TextView) v.findViewById(R.id.text_info_buiding_type);

		final TextView textBuyPerSquareFeet = (TextView) v
				.findViewById(R.id.text_info_buy_persquare_feet);
		final TextView textBuyLayer = (TextView) v.findViewById(R.id.text_info_buy_layer);

		final TextView textRooms = (TextView) v.findViewById(R.id.text_info_rooms);
		final TextView textIsGuarding = (TextView) v.findViewById(R.id.text_info_is_guarding);

		textAddress.setText(Datas.mEstates.get(markerIntArray.get(0)).estate_address);

		textHouseAge
				.setText(InfoParserApi.parseHouseAge(Datas.mEstates.get(markerIntArray.get(0)).date_built));
		textBuyDate
				.setText(InfoParserApi.parseBuyDate(Datas.mEstates.get(markerIntArray.get(0)).date_buy));

		textGroundArea.setText(InfoParserApi.parseBuildingExchangeArea(Datas.mEstates
				.get(markerIntArray.get(0)).building_exchange_area));
		textEstateType.setText(InfoParserApi.parseEstateType(Datas.mEstates.get(markerIntArray
				.get(0)).estate_type));

		textTotalPrice.setText(InfoParserApi.parseTotalBuyMoney(Datas.mEstates.get(markerIntArray
				.get(0)).buy_total_price));
		textBuildingType.setText(InfoParserApi.parseEstateType(Datas.mEstates.get(markerIntArray
				.get(0)).building_type));

		textBuyPerSquareFeet.setText(InfoParserApi.parsePerSquareFeetMoney(Datas.mEstates
				.get(markerIntArray.get(0)).buy_per_square_feet));
		textBuyLayer.setText(Datas.mEstates.get(markerIntArray.get(0)).buy_layer + "/"
				+ Integer.toString(Datas.mEstates.get(markerIntArray.get(0)).building_total_layer));

		textRooms.setText(Integer.toString(Datas.mEstates.get(markerIntArray.get(0)).building_room)
				+ "房"
				+ Integer.toString(Datas.mEstates.get(markerIntArray.get(0)).building_sitting_room)
				+ "廳"
				+ Integer.toString(Datas.mEstates.get(markerIntArray.get(0)).building_rest_room)
				+ "衛浴");
		textIsGuarding.setText(Datas.mEstates.get(markerIntArray.get(0)).is_guarding);

		layoutInfo.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TextView positionText = (TextView)
				// v.findViewById(R.id.text_info_position);
				int num = Integer.parseInt(textPosition.getText().toString());
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, DetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt("ItemPosition", markerIntArray.get(num));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		buttonBack.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// Toast.makeText(MainActivity.this, "back",
				// Toast.LENGTH_SHORT).show();
				int currentNum = Integer.parseInt(textPosition.getText().toString());

				// Restore last marker image
				int lastMarkerPosition = markerIntArray.get(currentNum);
				Marker lastMarker = mMarkers.get(lastMarkerPosition);
				if (lastMarker != null)
				{
					View layout = inflater.inflate(R.layout.item_marker, null);
					layout.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
					ImageView markerView = (ImageView) layout.findViewById(R.id.image_marker);
					TextView markerText = (TextView) layout.findViewById(R.id.text_marker_price);
					if (Datas.mEstates.get(lastMarkerPosition).estate_group == 1)
					{
						markerView.setImageResource(R.drawable.marker_sale);
					} else if (Datas.mEstates.get(lastMarkerPosition).estate_group == 3)
					{
						markerView.setImageResource(R.drawable.marker_rent);
					} else if (Datas.mEstates.get(lastMarkerPosition).estate_group == 2)
					{
						markerView.setImageResource(R.drawable.marker_presell);
					}
					markerText.setText(InfoParserApi.parsePerSquareFeetMoney_maker(Datas.mEstates
							.get(lastMarkerPosition).buy_per_square_feet));
					Bitmap bm = loadBitmapFromView(layout);
					// Changing marker icon
					lastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bm));
				}

				if (currentNum == 0)
				{
					currentNum = positionArraySize - 1;
				} else
				{
					currentNum = currentNum - 1;
				}

				textPosition.setText(Integer.toString(currentNum));
				textNumTotalNum.setText(Integer.toString(currentNum + 1) + "/"
						+ Integer.toString(positionArraySize));

				textAddress.setText(Datas.mEstates.get(markerIntArray.get(currentNum)).estate_address);

				textHouseAge.setText(InfoParserApi.parseHouseAge(Datas.mEstates.get(markerIntArray
						.get(currentNum)).date_built));
				textBuyDate.setText(InfoParserApi.parseBuyDate(Datas.mEstates.get(markerIntArray
						.get(currentNum)).date_buy));

				textGroundArea.setText(InfoParserApi.parseBuildingExchangeArea(Datas.mEstates
						.get(markerIntArray.get(currentNum)).building_exchange_area));
				textEstateType.setText(InfoParserApi.parseEstateType(Datas.mEstates
						.get(markerIntArray.get(currentNum)).estate_type));

				textTotalPrice.setText(InfoParserApi.parseTotalBuyMoney(Datas.mEstates
						.get(markerIntArray.get(currentNum)).buy_total_price));
				textBuildingType.setText(InfoParserApi.parseEstateType(Datas.mEstates
						.get(markerIntArray.get(currentNum)).building_type));

				textBuyPerSquareFeet.setText(InfoParserApi.parsePerSquareFeetMoney(Datas.mEstates
						.get(markerIntArray.get(currentNum)).buy_per_square_feet));
				textBuyLayer.setText(Datas.mEstates.get(markerIntArray.get(currentNum)).buy_layer
						+ "/"
						+ Integer.toString(Datas.mEstates.get(markerIntArray.get(currentNum)).building_total_layer));

				textRooms.setText(Integer.toString(Datas.mEstates.get(markerIntArray
						.get(currentNum)).building_room)
						+ "房"
						+ Integer.toString(Datas.mEstates.get(markerIntArray.get(currentNum)).building_sitting_room)
						+ "廳"
						+ Integer.toString(Datas.mEstates.get(markerIntArray.get(currentNum)).building_rest_room)
						+ "衛浴");
				textIsGuarding.setText(Datas.mEstates.get(markerIntArray.get(currentNum)).is_guarding);

				// change current marker image
				int currentMarkerPosition = markerIntArray.get(currentNum);
				currentMarker = mMarkers.get(currentMarkerPosition);
				if (lastMarker != null)
				{
					View layout = inflater.inflate(R.layout.item_marker, null);
					layout.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
					ImageView markerView = (ImageView) layout.findViewById(R.id.image_marker);
					TextView markerText = (TextView) layout.findViewById(R.id.text_marker_price);
					markerView.setImageResource(R.drawable.icon_selected);
					markerText.setText(InfoParserApi.parsePerSquareFeetMoney_maker(Datas.mEstates
							.get(currentMarkerPosition).buy_per_square_feet));
					Bitmap bm = loadBitmapFromView(layout);
					// Changing marker icon
					currentMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bm));
				}

			}
		});

		buttonForward.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// Toast.makeText(MainActivity.this, "forward",
				// Toast.LENGTH_SHORT).show();
				int currentNum = Integer.parseInt(textPosition.getText().toString());

				int lastMarkerPosition = markerIntArray.get(currentNum);
				Marker lastMarker = mMarkers.get(lastMarkerPosition);
				if (lastMarker != null)
				{
					View layout = inflater.inflate(R.layout.item_marker, null);
					layout.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
					ImageView markerView = (ImageView) layout.findViewById(R.id.image_marker);
					TextView markerText = (TextView) layout.findViewById(R.id.text_marker_price);
					if (Datas.mEstates.get(lastMarkerPosition).estate_group == 1)
					{
						markerView.setImageResource(R.drawable.marker_sale);
					} else if (Datas.mEstates.get(lastMarkerPosition).estate_group == 3)
					{
						markerView.setImageResource(R.drawable.marker_rent);
					} else if (Datas.mEstates.get(lastMarkerPosition).estate_group == 2)
					{
						markerView.setImageResource(R.drawable.marker_presell);
					}
					markerText.setText(InfoParserApi.parsePerSquareFeetMoney_maker(Datas.mEstates
							.get(lastMarkerPosition).buy_per_square_feet));
					Bitmap bm = loadBitmapFromView(layout);
					// Changing marker icon
					lastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bm));
				}

				if (currentNum == positionArraySize - 1)
				{
					currentNum = 0;
				} else
				{
					currentNum = currentNum + 1;
				}
				textPosition.setText(Integer.toString(currentNum));
				textNumTotalNum.setText(Integer.toString(currentNum + 1) + "/"
						+ Integer.toString(positionArraySize));

				textAddress.setText(Datas.mEstates.get(markerIntArray.get(currentNum)).estate_address);

				textHouseAge.setText(InfoParserApi.parseHouseAge(Datas.mEstates.get(markerIntArray
						.get(currentNum)).date_built));
				textBuyDate.setText(InfoParserApi.parseBuyDate(Datas.mEstates.get(markerIntArray
						.get(currentNum)).date_buy));

				textGroundArea.setText(InfoParserApi.parseBuildingExchangeArea(Datas.mEstates
						.get(markerIntArray.get(currentNum)).building_exchange_area));
				textEstateType.setText(InfoParserApi.parseEstateType(Datas.mEstates
						.get(markerIntArray.get(currentNum)).estate_type));

				textTotalPrice.setText(InfoParserApi.parseTotalBuyMoney(Datas.mEstates
						.get(markerIntArray.get(currentNum)).buy_total_price));
				textBuildingType.setText(InfoParserApi.parseEstateType(Datas.mEstates
						.get(markerIntArray.get(currentNum)).building_type));

				textBuyPerSquareFeet.setText(InfoParserApi.parsePerSquareFeetMoney(Datas.mEstates
						.get(markerIntArray.get(currentNum)).buy_per_square_feet));
				textBuyLayer.setText(Datas.mEstates.get(markerIntArray.get(currentNum)).buy_layer
						+ "/"
						+ Integer.toString(Datas.mEstates.get(markerIntArray.get(currentNum)).building_total_layer));

				textRooms.setText(Integer.toString(Datas.mEstates.get(markerIntArray
						.get(currentNum)).building_room)
						+ "房"
						+ Integer.toString(Datas.mEstates.get(markerIntArray.get(currentNum)).building_sitting_room)
						+ "廳"
						+ Integer.toString(Datas.mEstates.get(markerIntArray.get(currentNum)).building_rest_room)
						+ "衛浴");
				textIsGuarding.setText(Datas.mEstates.get(markerIntArray.get(currentNum)).is_guarding);

				int currentMarkerPosition = markerIntArray.get(currentNum);
				currentMarker = mMarkers.get(currentMarkerPosition);
				if (lastMarker != null)
				{
					View layout = inflater.inflate(R.layout.item_marker, null);
					layout.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
					ImageView markerView = (ImageView) layout.findViewById(R.id.image_marker);
					TextView markerText = (TextView) layout.findViewById(R.id.text_marker_price);
					markerView.setImageResource(R.drawable.icon_selected);
					markerText.setText(InfoParserApi.parsePerSquareFeetMoney_maker(Datas.mEstates
							.get(currentMarkerPosition).buy_per_square_feet));
					Bitmap bm = loadBitmapFromView(layout);
					// Changing marker icon
					currentMarker.setIcon(BitmapDescriptorFactory.fromBitmap(bm));
				}
			}
		});

		PopupWindow popupWindow = new PopupWindow(MainActivity.this);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(v);
		popupWindow.setWindowLayoutMode(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		popupWindow.setOnDismissListener(new OnDismissListener()
		{
			@Override
			public void onDismiss()
			{
				// TODO Auto-generated method stub
				// Toast.makeText(MainActivity.this, "PopView Dismissed",
				// Toast.LENGTH_SHORT).show();
				for (Marker item : mMarkers)
				{
					if (item.getTitle().equals(currentMarker.getTitle()))
					{
						// currentMarker = null;
						int markerPosition = Integer.parseInt(item.getTitle());
						View layout = inflater.inflate(R.layout.item_marker, null);
						layout.setLayoutParams(new LinearLayout.LayoutParams(
								LinearLayout.LayoutParams.WRAP_CONTENT,
								LinearLayout.LayoutParams.WRAP_CONTENT));
						ImageView markerView = (ImageView) layout.findViewById(R.id.image_marker);
						TextView markerText = (TextView) layout
								.findViewById(R.id.text_marker_price);
						if (Datas.mEstates.get(markerPosition).estate_group == 1)
						{
							markerView.setImageResource(R.drawable.marker_sale);
						} else if (Datas.mEstates.get(markerPosition).estate_group == 3)
						{
							markerView.setImageResource(R.drawable.marker_rent);
						} else if (Datas.mEstates.get(markerPosition).estate_group == 2)
						{
							markerView.setImageResource(R.drawable.marker_presell);
						}
						markerText.setText(InfoParserApi
								.parsePerSquareFeetMoney_maker(Datas.mEstates.get(markerPosition).buy_per_square_feet));
						Bitmap bm = loadBitmapFromView(layout);
						// Changing marker icon
						item.setIcon(BitmapDescriptorFactory.fromBitmap(bm));
						break;
					}
				}
			}
		});

		// popupWindow.showAsDropDown(findViewById(R.id.tv_title), x, 10);
		int offset_y = getPixelsFromDp(MainActivity.this, 150) / 2;
		int offset_x = getPixelsFromDp(MainActivity.this, 280) / 2;

		if (y > screenHeight / 2)
		{
			y = y - offset_y * 2;
		} else
		{
			y = y + offset_y;
		}

		if (x > screenWidth / 2)
		{
			// do nothing
		} else
		{
			x = x - offset_x;
		}

		popupWindow
				.showAtLocation(findViewById(R.id.map_wrapper), Gravity.LEFT | Gravity.TOP, x, y);
	}

	protected void showFilterDialog()
	{
		View vDialog = inflater.inflate(R.layout.filterdialog, null);
		CheckBox saleCheckBox = (CheckBox) vDialog.findViewById(R.id.checkbox_sale);
		CheckBox rentCheckBox = (CheckBox) vDialog.findViewById(R.id.checkbox_rent);
		CheckBox preSaleBox = (CheckBox) vDialog.findViewById(R.id.checkbox_presale);
		final EditText editSalePerSquareMin = (EditText) vDialog
				.findViewById(R.id.sale_per_square_min);
		final EditText editSalePerSquareMax = (EditText) vDialog
				.findViewById(R.id.sale_per_square_max);
		final EditText editSaleTotalMin = (EditText) vDialog.findViewById(R.id.sale_total_min);
		final EditText editSaleTotalMax = (EditText) vDialog.findViewById(R.id.sale_total_max);
		final EditText editSaleAreaMin = (EditText) vDialog.findViewById(R.id.sale_area_min);
		final EditText editSaleAreaMax = (EditText) vDialog.findViewById(R.id.sale_area_max);

		if (Constants.salePerSquareMin != 0)
		{
			editSalePerSquareMin.setText(Integer.toString(Constants.salePerSquareMin));
		}
		if (Constants.salePerSquareMax != 0)
		{
			editSalePerSquareMax.setText(Integer.toString(Constants.salePerSquareMax));
		}
		if (Constants.saleTotalMin != 0)
		{
			editSaleTotalMin.setText(Integer.toString(Constants.saleTotalMin));
		}
		if (Constants.saleTotalMax != 0)
		{
			editSaleTotalMax.setText(Integer.toString(Constants.saleTotalMax));
		}
		if (Constants.saleAreaMin != 0.0)
		{
			editSaleAreaMin.setText(Double.toString(Constants.saleAreaMin));
		}
		if (Constants.saleAreaMax != 0.0)
		{
			editSaleAreaMax.setText(Double.toString(Constants.saleAreaMax));
		}

		if (Constants.isSaledMarkerShow)
		{
			saleCheckBox.setChecked(true);
		} else
		{
			saleCheckBox.setChecked(false);
		}

		if (Constants.isRentMarkerShow)
		{
			rentCheckBox.setChecked(true);
		} else
		{
			rentCheckBox.setChecked(false);
		}

		if (Constants.isPreSaleMarkerShow)
		{
			preSaleBox.setChecked(true);
		} else
		{
			preSaleBox.setChecked(false);
		}

		saleCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					Constants.isSaledMarkerShow = true;
				} else
				{
					Constants.isSaledMarkerShow = false;
				}
			}
		});

		rentCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					Constants.isRentMarkerShow = true;
				} else
				{
					Constants.isRentMarkerShow = false;
				}

			}
		});

		preSaleBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
				{
					Constants.isPreSaleMarkerShow = true;
				} else
				{
					Constants.isPreSaleMarkerShow = false;
				}
			}
		});

		final LinearLayout layoutMore = (LinearLayout) vDialog
				.findViewById(R.id.layout_more_filter);
		LinearLayout layoutMoreBtn = (LinearLayout) vDialog.findViewById(R.id.layout_more_btn);
		layoutMoreBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (layoutMore.getVisibility() == View.GONE)
				{
					layoutMore.setVisibility(View.VISIBLE);
				} else
				{
					layoutMore.setVisibility(View.GONE);
				}

			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setTitle("條件篩選");
		builder.setView(vDialog);
		builder.setPositiveButton("確定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{
				try
				{
					Constants.salePerSquareMin = Integer.parseInt(editSalePerSquareMin.getText()
							.toString());
				} catch (Exception e)
				{
					Constants.salePerSquareMin = 0;
				}
				try
				{
					Constants.salePerSquareMax = Integer.parseInt(editSalePerSquareMax.getText()
							.toString());
				} catch (Exception e)
				{
					Constants.salePerSquareMax = 0;
				}
				try
				{
					Constants.saleTotalMin = Integer
							.parseInt(editSaleTotalMin.getText().toString());
				} catch (Exception e)
				{
					Constants.saleTotalMin = 0;
				}
				try
				{
					Constants.saleTotalMax = Integer
							.parseInt(editSaleTotalMax.getText().toString());
				} catch (Exception e)
				{
					Constants.saleTotalMax = 0;
				}
				try
				{
					Constants.saleAreaMin = Double
							.parseDouble(editSaleAreaMin.getText().toString());
				} catch (Exception e)
				{
					Constants.saleAreaMin = 0;
				}
				try
				{
					Constants.saleAreaMax = Double
							.parseDouble(editSaleAreaMax.getText().toString());
				} catch (Exception e)
				{
					Constants.saleAreaMax = 0;
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{

			}
		});
		builder.show();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		// If the nav drawer is open, hide action items related to the content
		// view
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerListView);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	private void setDrawerLayout()
	{

		items.add(new SectionItem("實價登錄搜尋"));
		items.add(new EntryItem("位置附近", R.drawable.icon_access_location));
		items.add(new EntryItem("我的最愛", R.drawable.icon_favorite));
		items.add(new EntryItem("條件篩選", R.drawable.icon_filter));
		items.add(new SectionItem("房貸計算"));
		items.add(new EntryItem("房貸計算機", R.drawable.icon_calculator));
		items.add(new SectionItem("其他"));
		items.add(new EntryItem("設定", R.drawable.icon_setting));
		items.add(new EntryItem("關於我們", R.drawable.icon_about));

		mDrawerAdapter = new EntryAdapter(MainActivity.this, items);
		mDrawerListView.setAdapter(mDrawerAdapter);
		mDrawerListView.setOnItemClickListener((new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				if (!items.get(position).isSection())
				{
					// EntryItem item = (EntryItem) items.get(position);

					switch (position)
					{
					case 1:
						// move to position
						CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(Constants.currentLatLng).zoom(14).build();
						googleMap.animateCamera(CameraUpdateFactory
								.newCameraPosition(cameraPosition));
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 2:
						// favorite activity
						Intent intent0 = new Intent(MainActivity.this, FavoriteActivity.class);
						startActivity(intent0);
						break;
					case 3:
						// filter dialog
						showFilterDialog();
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 5:
						Intent intent = new Intent(MainActivity.this, CalculatorActivity.class);
						startActivity(intent);
						break;
					case 7:
						// setting activity
						Intent intent1 = new Intent(MainActivity.this, SettingActivity.class);
						startActivity(intent1);
						break;
					case 8:
						// about us
						Intent intent2 = new Intent(MainActivity.this, AboutUsActivity.class);
						startActivity(intent2);
						break;
					default:
						break;
					}
				}
			}
		}));

	}

	private void CallAds()
	{

		adBannerLayout = (RelativeLayout) findViewById(R.id.adLayout);
		final AdRequest adReq = new AdRequest.Builder().build();

		// 12-18 17:01:12.438: I/Ads(8252): Use
		// AdRequest.Builder.addTestDevice("A25819A64B56C65500038B8A9E7C19DD")
		// to get test ads on this device.

		adMobAdView = new AdView(MainActivity.this);
		adMobAdView.setAdSize(AdSize.SMART_BANNER);
		adMobAdView.setAdUnitId(Constants.MEDIATION_KEY);

		adMobAdView.loadAd(adReq);
		adBannerLayout.addView(adMobAdView);
	}

}