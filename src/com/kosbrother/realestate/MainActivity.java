package com.kosbrother.realestate;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import at.bartinger.list.item.EntryAdapter;
import at.bartinger.list.item.EntryItem;
import at.bartinger.list.item.Item;
import at.bartinger.list.item.SectionItem;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kosbrother.realestate.api.EstateApi;
import com.kosbrother.realestate.api.InfoParserApi;
import com.kosbrother.realestate.fragment.TransparentSupportMapFragment;

public class MainActivity extends SherlockFragmentActivity implements
		LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener
{

	// private ActionBarHelper mActionBar;
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ListView mDrawerListView;
	private EntryAdapter mDrawerAdapter;

	private EditText search;
	private MenuItem itemSearch;
	private static final int ID_SEARCH = 5;

	// private LocationManager mLocationManager;
	// Stores the current instantiation of the location client in this object
	private LocationClient mLocationClient;
	// private LocationRequest mLocationRequest;

	// Google Map
	private GoogleMap googleMap;
	static final LatLng NKUT = new LatLng(23.979548, 120.696745);

	private LatLng currentLatLng;

	private LayoutInflater inflater;
	private ImageButton btnFocusButton;
	private ImageButton btnLayerButton;
	private ImageButton btnFilterButton;
	private int currentMapTypePosition = 0;
	private LinearLayout leftDrawer;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_layout);

		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListView = (ListView) findViewById(R.id.left_list_view);
		btnFocusButton = (ImageButton) findViewById(R.id.image_btn_focus);
		btnLayerButton = (ImageButton) findViewById(R.id.image_btn_layers);
		btnFilterButton = (ImageButton) findViewById(R.id.image_btn_filter);
		leftDrawer = (LinearLayout) findViewById(R.id.left_drawer);

		btnFocusButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				// Toast.makeText(MainActivity.this, "focus",
				// Toast.LENGTH_SHORT)
				// .show();
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(currentLatLng).zoom(14).build();
				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
			}
		});

		btnLayerButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				// Set the dialog title
				builder.setTitle("順序排列").setSingleChoiceItems(R.array.map_type,
						currentMapTypePosition,
						new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog,
									int position)
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
									googleMap
											.setMapType(GoogleMap.MAP_TYPE_NORMAL);
									break;
								case 1:
									googleMap
											.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
									break;
								case 2:
									googleMap
											.setMapType(GoogleMap.MAP_TYPE_HYBRID);
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
				// TODO Auto-generated method stub
				// Toast.makeText(MainActivity.this, "filter",
				// Toast.LENGTH_SHORT)
				// .show();
				showFilterDialog();

			}
		});

		// mDrawerLayout.setDrawerListener(new DemoDrawerListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close)
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

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	protected void showFilterDialog()
	{
		View vDialog = inflater.inflate(R.layout.filterdialog, null);
		final LinearLayout layoutMore = (LinearLayout) vDialog
				.findViewById(R.id.layout_more_filter);
		LinearLayout layoutMoreBtn = (LinearLayout) vDialog
				.findViewById(R.id.layout_more_btn);
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
		builder.setTitle("塞選條件");
		builder.setView(vDialog);
		builder.setPositiveButton("確定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{

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
		// TODO Auto-generated method stub
		items.add(new SectionItem("實價登陸搜尋"));
		items.add(new EntryItem("位置附近", R.drawable.icon_access_location));
		items.add(new EntryItem("我的最愛", R.drawable.icon_favorite));
		items.add(new EntryItem("條件塞選", R.drawable.icon_filter));
		items.add(new SectionItem("房貸計算"));
		items.add(new EntryItem("房貸計算機", R.drawable.icon_calculator));
		items.add(new SectionItem("其他"));
		items.add(new EntryItem("設定", R.drawable.icon_setting));
		items.add(new EntryItem("關於我們", R.drawable.icon_about));

		mDrawerAdapter = new EntryAdapter(MainActivity.this, items);
		mDrawerListView.setAdapter(mDrawerAdapter);
		mDrawerListView.setOnItemClickListener((new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id)
			{
				if (!items.get(position).isSection())
				{
					// EntryItem item = (EntryItem) items.get(position);

					switch (position)
					{
					case 1:
						// move to position
						CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(currentLatLng).zoom(14).build();
						googleMap.animateCamera(CameraUpdateFactory
								.newCameraPosition(cameraPosition));
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 2:
						// favorite activity
						break;
					case 3:
						// filter dialog
						showFilterDialog();
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 5:
						Toast.makeText(MainActivity.this, "pos=" + position,
								Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(MainActivity.this,
								CalculatorActivity.class);
						startActivity(intent);
						break;
					case 7:
						// setting activity
						break;
					case 8:
						// about us
						break;
					default:
						break;
					}
				}
			}
		}));

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
			// Marker nkut = googleMap.addMarker(new
			// MarkerOptions().position(NKUT).title("南開科技大學").snippet("數位生活創意系"));
			// CameraPosition cameraPosition = new
			// CameraPosition.Builder().target(NKUT).zoom(12).build();

			// googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

			// mLocationManager = (LocationManager)
			// getSystemService(LOCATION_SERVICE);
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setMyLocationButtonEnabled(false);
			googleMap.getUiSettings().setZoomControlsEnabled(false);
			googleMap.getUiSettings().setCompassEnabled(false);

			googleMap.setInfoWindowAdapter(new InfoWindowAdapter()
			{

				@Override
				public View getInfoWindow(Marker marker)
				{
					View v = inflater.inflate(R.layout.item_window_info, null);
					int position = Integer.parseInt(marker.getTitle());

					TextView textAddress = (TextView) v
							.findViewById(R.id.text_info_address);

					TextView textHouseAge = (TextView) v
							.findViewById(R.id.text_info_house_age);
					TextView textBuyDate = (TextView) v
							.findViewById(R.id.text_info_buy_date);

					TextView textGroundArea = (TextView) v
							.findViewById(R.id.text_info_ground_area);
					TextView textEstateType = (TextView) v
							.findViewById(R.id.text_info_estate_type);

					TextView textTotalPrice = (TextView) v
							.findViewById(R.id.text_info_total_price);
					TextView textBuildingType = (TextView) v
							.findViewById(R.id.text_info_buiding_type);

					TextView textBuyPerSquareFeet = (TextView) v
							.findViewById(R.id.text_info_buy_persquare_feet);
					TextView textBuyLayer = (TextView) v
							.findViewById(R.id.text_info_buy_layer);

					TextView textRooms = (TextView) v
							.findViewById(R.id.text_info_rooms);
					TextView textIsGuarding = (TextView) v
							.findViewById(R.id.text_info_is_guarding);

					textAddress.setText(Datas.mEstates.get(position).estate_address);

					textHouseAge.setText(InfoParserApi
							.parseHouseAge(Datas.mEstates.get(position).date_built));
					textBuyDate.setText(InfoParserApi
							.parseBuyDate(Datas.mEstates.get(position).date_buy));

					textGroundArea.setText(InfoParserApi
							.parseBuildingExchangeArea(Datas.mEstates
									.get(position).building_exchange_area));
					textEstateType.setText(InfoParserApi
							.parseEstateType(Datas.mEstates.get(position).estate_type));

					textTotalPrice.setText(InfoParserApi
							.parseTotalBuyMoney(Datas.mEstates.get(position).buy_total_price));
					textBuildingType.setText(InfoParserApi
							.parseEstateType(Datas.mEstates.get(position).building_type));

					textBuyPerSquareFeet.setText(InfoParserApi
							.parsePerSquareFeetMoney(Datas.mEstates
									.get(position).buy_per_square_feet));
					textBuyLayer.setText(Datas.mEstates.get(position).buy_layer
							+ "/"
							+ Integer.toString(Datas.mEstates.get(position).building_total_layer));

					textRooms.setText(Integer.toString(Datas.mEstates
							.get(position).building_room)
							+ "房"
							+ Integer.toString(Datas.mEstates.get(position).building_sitting_room)
							+ "廳"
							+ Integer.toString(Datas.mEstates.get(position).building_rest_room)
							+ "衛浴");
					textIsGuarding.setText(Datas.mEstates.get(position).is_guarding);

					return v;
				}

				@Override
				public View getInfoContents(Marker arg0)
				{
					return null;

				}
			});

			// Location currentLoc = googleMap.getMyLocation();
			// LatLng currentLatLng = new LatLng (currentLoc.getLatitude(),
			// currentLoc.getLongitude());
			// CameraPosition cameraPosition = new
			// CameraPosition.Builder().target(currentLatLng).zoom(12).build();
			// googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

			// check if map is created successfully or not
			if (googleMap == null)
			{
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
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

		itemSearch = menu
				.add(0, ID_SEARCH, 0,
						getResources().getString(R.string.menu_search))
				.setIcon(R.drawable.icon_search)
				.setOnActionExpandListener(
						new MenuItem.OnActionExpandListener()
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
									public boolean onEditorAction(TextView v,
											int actionId, KeyEvent event)
									{
										if (actionId == EditorInfo.IME_ACTION_SEARCH
												|| event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
										{
											// Bundle bundle = new Bundle();
											// bundle.putString("SearchKeyword",
											// v.getText().toString());
											// Intent intent = new Intent();
											// intent.setClass(MainActivity.this,
											// SearchActivity.class);
											// intent.putExtras(bundle);
											// startActivity(intent);
											// itemSearch.collapseActionView();
											return true;
										}
										return false;
									}
								});
								InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
								imm.showSoftInput(null,
										InputMethodManager.SHOW_IMPLICIT);
								return true;
							}

							@Override
							public boolean onMenuItemActionCollapse(
									MenuItem item)
							{
								// TODO Auto-generated method stub
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
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item)
	{
		if (mDrawerToggle.onOptionsItemSelected(getMenuItem(item)))
		{
			return true;
		} else
		{
			switch (item.getItemId())
			{
			case ID_SEARCH:
				Toast.makeText(MainActivity.this, "search", Toast.LENGTH_SHORT)
						.show();
				break;
			case R.id.menu_list:
				Toast.makeText(MainActivity.this, "list", Toast.LENGTH_SHORT)
						.show();
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
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean expandActionView()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public ActionProvider getActionProvider()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public View getActionView()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public char getAlphabeticShortcut()
			{
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getGroupId()
			{
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Drawable getIcon()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Intent getIntent()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ContextMenuInfo getMenuInfo()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public char getNumericShortcut()
			{
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getOrder()
			{
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public SubMenu getSubMenu()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CharSequence getTitle()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public CharSequence getTitleCondensed()
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean hasSubMenu()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isActionViewExpanded()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isCheckable()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isChecked()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isVisible()
			{
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public android.view.MenuItem setActionProvider(
					ActionProvider actionProvider)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(View view)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setActionView(int resId)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setAlphabeticShortcut(char alphaChar)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setCheckable(boolean checkable)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setChecked(boolean checked)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setEnabled(boolean enabled)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(Drawable icon)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setIcon(int iconRes)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setIntent(Intent intent)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setNumericShortcut(char numericChar)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setOnActionExpandListener(
					OnActionExpandListener listener)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setOnMenuItemClickListener(
					OnMenuItemClickListener menuItemClickListener)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setShortcut(char numericChar,
					char alphaChar)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void setShowAsAction(int actionEnum)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public android.view.MenuItem setShowAsActionFlags(int actionEnum)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(CharSequence title)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setTitle(int title)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setTitleCondensed(CharSequence title)
			{
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public android.view.MenuItem setVisible(boolean visible)
			{
				// TODO Auto-generated method stub
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
				currentLatLng = new LatLng(currentLocation.getLatitude(),
						currentLocation.getLongitude());
			} else
			{
				currentLatLng = new LatLng(25.0478, 121.5172);
			}

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(currentLatLng).zoom(14).build();
			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

			new GetEstatesTask().execute();
			// Display the current location in the UI
			// mLatLng.setText(LocationUtils.getLatLng(this, currentLocation));
		}
	}

	/**
	 * Verify that Google Play services is available before making a request.
	 * 
	 * @return true if Google Play services is available, otherwise false
	 */
	private boolean servicesConnected()
	{

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode)
		{
			// In debug mode, log the status
			Log.d(LocationUtils.APPTAG,
					getString(R.string.play_services_available));

			// Continue
			return true;
			// Google Play services was not available for some reason
		} else
		{
			// Display an error dialog
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					this, 0);
			if (dialog != null)
			{
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(getSupportFragmentManager(),
						LocationUtils.APPTAG);
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
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
				this, LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

		// If Google Play services can provide an error dialog
		if (errorDialog != null)
		{

			// Create a new DialogFragment in which to show the error dialog
			ErrorDialogFragment errorFragment = new ErrorDialogFragment();

			// Set the dialog in the DialogFragment
			errorFragment.setDialog(errorDialog);

			// Show the error dialog in the DialogFragment
			errorFragment.show(getSupportFragmentManager(),
					LocationUtils.APPTAG);
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

		/*
		 * Connect the client. Don't re-start any requests here; instead, wait
		 * for onResume()
		 */
		mLocationClient.connect();

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

	protected class GetEstatesTask extends AsyncTask
	{

		@Override
		protected void onPreExecute()
		{
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... params)
		{
			// TODO Auto-generated method stub
			Datas.mEstates = EstateApi.getAroundAll(currentLatLng.latitude,
					currentLatLng.longitude);

			return null;
		}

		@Override
		protected void onPostExecute(Object result)
		{
			// super.onPostExecute(result);
			if (Datas.mEstates != null)
			{
				for (int i = 0; i < Datas.mEstates.size(); i++)
				{
					LatLng newLatLng = new LatLng(Datas.mEstates.get(i).x_lat,
							Datas.mEstates.get(i).y_long);
					View layout = inflater.inflate(R.layout.item_marker, null);
					layout.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
					ImageView markerView = (ImageView) layout
							.findViewById(R.id.image_marker);
					TextView markerText = (TextView) layout
							.findViewById(R.id.text_marker_price);

					// for later marker info window use
					MarkerOptions marker = new MarkerOptions().position(
							newLatLng).title(Integer.toString(i));
					markerText.setText(InfoParserApi
							.parsePerSquareFeetMoney_maker(Datas.mEstates
									.get(i).buy_per_square_feet));

					if (Datas.mEstates.get(i).estate_group == 1)
					{
						markerView.setImageResource(R.drawable.marker_red);
						marker.snippet("estate");
					} else if (Datas.mEstates.get(i).estate_group == 2)
					{
						markerView.setImageResource(R.drawable.marker_green);
						marker.snippet("rent");
						// googleMap.addMarker(new
						// MarkerOptions().position(newLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_green)));
					} else if (Datas.mEstates.get(i).estate_group == 3)
					{
						markerView.setImageResource(R.drawable.marker_presale);
						marker.snippet("pre_sale");
						// googleMap.addMarker(new
						// MarkerOptions().position(newLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_presale)));
					}

					Bitmap bm = loadBitmapFromView(layout);

					// Changing marker icon
					marker.icon(BitmapDescriptorFactory.fromBitmap(bm));

					// adding marker
					googleMap.addMarker(marker);
				}
			} else
			{
				Toast.makeText(MainActivity.this, "No Data!!",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public static Bitmap loadBitmapFromView(View v)
	{
		if (v.getMeasuredHeight() <= 0)
		{
			v.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(),
					v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(b);
			v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
			v.draw(c);
			return b;
		}

		Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width,
				v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
		v.draw(c);
		return b;
	}
}