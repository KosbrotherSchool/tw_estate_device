package com.kosbrother.realestate;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import at.bartinger.list.item.EntryAdapter;
import at.bartinger.list.item.EntryItem;
import at.bartinger.list.item.Item;
import at.bartinger.list.item.SectionItem;

@SuppressLint("NewApi")
public class ListActivity extends FragmentActivity {
	 
	private ActionBarHelper mActionBar;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private DrawerLayout mDrawerLayout;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ListView mDrawerListView;
	private EntryAdapter mDrawerAdapter;
	
	private EditText            search;
   private MenuItem            itemSearch;
   private static final int    ID_SEARCH   = 5;
   
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.drawer_list_layout);
       
       
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
   	
   	mDrawerAdapter = new EntryAdapter(ListActivity.this, items);
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

   @Override
   protected void onResume() {
       super.onResume();
       
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
//                                   Bundle bundle = new Bundle();
//                                   bundle.putString("SearchKeyword", v.getText().toString());
//                                   Intent intent = new Intent();
//                                   intent.setClass(MainActivity.this, SearchActivity.class);
//                                   intent.putExtras(bundle);
//                                   startActivity(intent);
//                                   itemSearch.collapseActionView();
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
		
       getMenuInflater().inflate(R.menu.menu_list, menu);
       
		return super.onCreateOptionsMenu(menu);
	}
   
   @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
           return true;
       }else{
			switch (item.getItemId()) {
			case ID_SEARCH:
				Toast.makeText(ListActivity.this, "search", Toast.LENGTH_SHORT).show();
				break;
			case R.id.menu_map:
				Toast.makeText(ListActivity.this, "Map", Toast.LENGTH_SHORT).show();
				break;
			case R.id.menu_sorting:
				Toast.makeText(ListActivity.this, "Sorting", Toast.LENGTH_SHORT).show();
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

}