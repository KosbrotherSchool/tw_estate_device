package com.kosbrother.realestate;

import java.util.ArrayList;
import java.util.Collections;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.ActionProvider;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import at.bartinger.list.item.EntryAdapter;
import at.bartinger.list.item.EntryItem;
import at.bartinger.list.item.Item;
import at.bartinger.list.item.SectionItem;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.kosbrother.realestate.adapter.ListEstateAdapter;

public class ListActivity extends SherlockFragmentActivity
{

	// private ActionBarHelper mActionBar;
	private ActionBarDrawerToggle mDrawerToggle;

	private DrawerLayout mDrawerLayout;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ListView mDrawerListView;
	private EntryAdapter mDrawerAdapter;

	// private MenuItem itemSearch;
	// private static final int ID_SEARCH = 5;

	private ListEstateAdapter mAdapter;
	private ListView mainListView;

	private int currentSortPosition;
	private LinearLayout leftDrawer;
	private LayoutInflater inflater;

	private ActionBar mActionBar;

	private RelativeLayout adBannerLayout;
	private AdView adMobAdView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drawer_list_layout);
		mActionBar = getSupportActionBar();
		mActionBar.setTitle("Zillion 實價登錄: " + Integer.toString(Datas.mEstates.size()) + "筆");

		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListView = (ListView) findViewById(R.id.left_list_view);

		// mDrawerLayout.setDrawerListener(new DemoDrawerListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		leftDrawer = (LinearLayout) findViewById(R.id.left_drawer);

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

		mainListView = (ListView) findViewById(R.id.list_estates);
		mAdapter = new ListEstateAdapter(ListActivity.this, Datas.mEstates);
		mainListView.setAdapter(mAdapter);
		
		CallAds();
		
	}

	private void setDrawerLayout()
	{
		// TODO Auto-generated method stub
		items.add(new SectionItem("實價登錄搜尋"));
		items.add(new EntryItem("位置附近", R.drawable.icon_access_location));
		items.add(new EntryItem("我的最愛", R.drawable.icon_favorite));
		items.add(new EntryItem("條件篩選", R.drawable.icon_filter));
		items.add(new SectionItem("房貸計算"));
		items.add(new EntryItem("房貸計算機", R.drawable.icon_calculator));
		items.add(new SectionItem("其他"));
		items.add(new EntryItem("設定", R.drawable.icon_setting));
		items.add(new EntryItem("關於我們", R.drawable.icon_about));

		mDrawerAdapter = new EntryAdapter(ListActivity.this, items);
		mDrawerListView.setAdapter(mDrawerAdapter);
		leftDrawer = (LinearLayout) findViewById(R.id.left_drawer);

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
						finish();
						break;
					case 2:
						// favorite activity
						Intent intent0 = new Intent(ListActivity.this, FavoriteActivity.class);
						startActivity(intent0);
						break;
					case 3:
						// filter dialog
						showFilterDialog();
						mDrawerLayout.closeDrawer(leftDrawer);
						break;
					case 5:
						// Toast.makeText(MainActivity.this, "pos=" + position,
						// Toast.LENGTH_SHORT).show();

						Intent intent = new Intent(ListActivity.this, CalculatorActivity.class);
						startActivity(intent);
						break;
					case 7:
						// setting activity
						Intent intent1 = new Intent(ListActivity.this, SettingActivity.class);
						startActivity(intent1);
						break;
					case 8:
						// about us
						Intent intent2 = new Intent(ListActivity.this, AboutUsActivity.class);
						startActivity(intent2);
						break;
					default:
						break;
					}
				}
			}
		}));

	}

	@Override
	protected void onResume()
	{
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);

		getSupportMenuInflater().inflate(R.menu.menu_list, menu);

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
			case R.id.menu_map:
				// Toast.makeText(ListActivity.this, "Map", Toast.LENGTH_SHORT)
				// .show();
				finish();
				break;
			case R.id.menu_sorting:

				AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
				// Set the dialog title
				builder.setTitle("順序排列").setSingleChoiceItems(R.array.list_sort,
						currentSortPosition, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int position)
							{
								sortingByPosition(position);
								currentSortPosition = position;
								dialog.cancel();
							}
						});
				builder.show();

				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private void sortingByPosition(int position)
	{
		switch (position)
		{
		case 0:
			Collections.sort(Datas.mEstates, new Datas.BuyPerSquareComparator(0));
			break;
		case 1:
			Collections.sort(Datas.mEstates, new Datas.BuyPerSquareComparator(1));
			break;
		case 2:
			Collections.sort(Datas.mEstates, new Datas.BuyTotalPriceComparator(0));
			break;
		case 3:
			Collections.sort(Datas.mEstates, new Datas.BuyTotalPriceComparator(1));
			break;
		case 4:
			Collections.sort(Datas.mEstates, new Datas.BuildingExchangeAreaComparator(0));
			break;
		case 5:
			Collections.sort(Datas.mEstates, new Datas.BuildingExchangeAreaComparator(1));
			break;
		case 6:
			Collections.sort(Datas.mEstates, new Datas.BuiltDateComparator());
			break;
		}
		mAdapter.notifyDataSetChanged();
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
			public android.view.MenuItem setActionProvider(ActionProvider actionProvider)
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
			public android.view.MenuItem setOnActionExpandListener(OnActionExpandListener listener)
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
			public android.view.MenuItem setShortcut(char numericChar, char alphaChar)
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

		AlertDialog.Builder builder = new AlertDialog.Builder(ListActivity.this);
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
				Log.i("fffff", Constants.salePerSquareMin + " " + Constants.salePerSquareMax);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int id)
			{

			}
		});
		builder.show();
	}

	// private ActionBarHelper createActionBarHelper() {
	// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
	// return new ActionBarHelperICS();
	// } else {
	// return new ActionBarHelper();
	// }
	// }
	//
	// private class ActionBarHelper {
	// public void init() {}
	// public void onDrawerClosed() {}
	// public void onDrawerOpened() {}
	// public void setTitle(CharSequence title) {}
	// }
	//
	// private class ActionBarHelperICS extends ActionBarHelper {
	// private final ActionBar mActionBar;
	// private CharSequence mDrawerTitle;
	// private CharSequence mTitle;
	//
	// ActionBarHelperICS() {
	// mActionBar = getActionBar();
	// }
	//
	// @Override
	// public void init() {
	// mActionBar.setDisplayHomeAsUpEnabled(true);
	// mActionBar.setHomeButtonEnabled(true);
	// mTitle = mDrawerTitle = getTitle();
	// }
	//
	// @Override
	// public void onDrawerClosed() {
	// super.onDrawerClosed();
	// mActionBar.setTitle(mTitle);
	// }
	//
	// @Override
	// public void onDrawerOpened() {
	// super.onDrawerOpened();
	// mActionBar.setTitle(mDrawerTitle);
	// }
	//
	// @Override
	// public void setTitle(CharSequence title) {
	// mTitle = title;
	// }
	// }

	// private class DemoDrawerListener implements DrawerLayout.DrawerListener {
	// @Override
	// public void onDrawerOpened(View drawerView) {
	// mDrawerToggle.onDrawerOpened(drawerView);
	// mActionBar.onDrawerOpened();
	// }
	//
	// @Override
	// public void onDrawerClosed(View drawerView) {
	// mDrawerToggle.onDrawerClosed(drawerView);
	// mActionBar.onDrawerClosed();
	// }
	//
	// @Override
	// public void onDrawerSlide(View drawerView, float slideOffset) {
	// mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
	// }
	//
	// @Override
	// public void onDrawerStateChanged(int newState) {
	// mDrawerToggle.onDrawerStateChanged(newState);
	// }
	// }

	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onStart()
	{
		super.onStart();
		// The rest of your onStart() code.
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop()
	{
		super.onStop();
		// The rest of your onStop() code.
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}
	
	private void CallAds()
	{

		adBannerLayout = (RelativeLayout) findViewById(R.id.adLayout);
		final AdRequest adReq = new AdRequest.Builder().build();

		// 12-18 17:01:12.438: I/Ads(8252): Use
		// AdRequest.Builder.addTestDevice("A25819A64B56C65500038B8A9E7C19DD")
		// to get test ads on this device.

		adMobAdView = new AdView(ListActivity.this);
		adMobAdView.setAdSize(AdSize.SMART_BANNER);
		adMobAdView.setAdUnitId(Constants.MEDIATION_KEY);

		adMobAdView.loadAd(adReq);
		adMobAdView.setAdListener(new AdListener()
		{
			@Override
			public void onAdLoaded() {
				adBannerLayout.setVisibility(View.VISIBLE);
				if (adBannerLayout.getChildAt(0)!=null)
				{
					adBannerLayout.removeViewAt(0);
				}
				adBannerLayout.addView(adMobAdView);
			}
			
			public void onAdFailedToLoad(int errorCode) {
				adBannerLayout.setVisibility(View.GONE);
			}
			
		});	
	}

}