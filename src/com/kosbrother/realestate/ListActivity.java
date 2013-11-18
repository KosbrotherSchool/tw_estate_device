package com.kosbrother.realestate;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.kosbrother.realestate.adapter.ListEstateAdapter;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.InputType;
import android.view.ActionProvider;
import android.view.KeyEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem.OnActionExpandListener;
import android.view.MenuItem.OnMenuItemClickListener;
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
public class ListActivity extends SherlockFragmentActivity {
	 
//	private ActionBarHelper mActionBar;
	private ActionBarDrawerToggle mDrawerToggle;
	
	private DrawerLayout mDrawerLayout;
	private ArrayList<Item> items = new ArrayList<Item>();
	private ListView mDrawerListView;
	private EntryAdapter mDrawerAdapter;
	
	private EditText            search;
    private MenuItem            itemSearch;
    private static final int    ID_SEARCH   = 5;
   
    private ListEstateAdapter mAdapter;
    private ListView mainListView;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.drawer_list_layout);
       
       
//        mActionBar = createActionBarHelper();
//		mActionBar.init();
       
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListView = (ListView) findViewById(R.id.left_list_view);
		
//       mDrawerLayout.setDrawerListener(new DemoDrawerListener());
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		
		// enable ActionBar app icon to behave as action to toggle nav drawer
	    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    getSupportActionBar().setHomeButtonEnabled(true);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close){
	         public void onDrawerClosed(View view) {
//	             getSupportActionBar().setTitle(mTitle);
	             supportInvalidateOptionsMenu(); // creates call to
	                                             // onPrepareOptionsMenu()
	          }

	          public void onDrawerOpened(View drawerView) {
//	             getSupportActionBar().setTitle(mDrawerTitle);
	             supportInvalidateOptionsMenu(); // creates call to
	                                             // onPrepareOptionsMenu()
	          }
	       };
	    mDrawerLayout.setDrawerListener(mDrawerToggle);
       
		setDrawerLayout();
		
		mainListView = (ListView) findViewById(R.id.list_estates);
		mAdapter = new ListEstateAdapter(ListActivity.this, Datas.mEstates);
		mainListView.setAdapter(mAdapter);
		
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
		
       getSupportMenuInflater().inflate(R.menu.menu_list, menu);
       
		return super.onCreateOptionsMenu(menu);
	}
   
   @Override
   public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
	   if (mDrawerToggle.onOptionsItemSelected(getMenuItem(item))) {
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
   
   private android.view.MenuItem getMenuItem(final MenuItem item) {
       return new android.view.MenuItem() {
          @Override
          public int getItemId() {
             return item.getItemId();
          }

          public boolean isEnabled() {
             return true;
          }

          @Override
          public boolean collapseActionView() {
             // TODO Auto-generated method stub
             return false;
          }

          @Override
          public boolean expandActionView() {
             // TODO Auto-generated method stub
             return false;
          }

          @Override
          public ActionProvider getActionProvider() {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public View getActionView() {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public char getAlphabeticShortcut() {
             // TODO Auto-generated method stub
             return 0;
          }

          @Override
          public int getGroupId() {
             // TODO Auto-generated method stub
             return 0;
          }

          @Override
          public Drawable getIcon() {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public Intent getIntent() {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public ContextMenuInfo getMenuInfo() {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public char getNumericShortcut() {
             // TODO Auto-generated method stub
             return 0;
          }

          @Override
          public int getOrder() {
             // TODO Auto-generated method stub
             return 0;
          }

          @Override
          public SubMenu getSubMenu() {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public CharSequence getTitle() {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public CharSequence getTitleCondensed() {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public boolean hasSubMenu() {
             // TODO Auto-generated method stub
             return false;
          }

          @Override
          public boolean isActionViewExpanded() {
             // TODO Auto-generated method stub
             return false;
          }

          @Override
          public boolean isCheckable() {
             // TODO Auto-generated method stub
             return false;
          }

          @Override
          public boolean isChecked() {
             // TODO Auto-generated method stub
             return false;
          }

          @Override
          public boolean isVisible() {
             // TODO Auto-generated method stub
             return false;
          }

          @Override
          public android.view.MenuItem setActionProvider(ActionProvider actionProvider) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setActionView(View view) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setActionView(int resId) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setAlphabeticShortcut(char alphaChar) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setCheckable(boolean checkable) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setChecked(boolean checked) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setEnabled(boolean enabled) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setIcon(Drawable icon) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setIcon(int iconRes) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setIntent(Intent intent) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setNumericShortcut(char numericChar) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setOnActionExpandListener(OnActionExpandListener listener) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setShortcut(char numericChar, char alphaChar) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public void setShowAsAction(int actionEnum) {
             // TODO Auto-generated method stub

          }

          @Override
          public android.view.MenuItem setShowAsActionFlags(int actionEnum) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setTitle(CharSequence title) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setTitle(int title) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setTitleCondensed(CharSequence title) {
             // TODO Auto-generated method stub
             return null;
          }

          @Override
          public android.view.MenuItem setVisible(boolean visible) {
             // TODO Auto-generated method stub
             return null;
          }
       };
    }
   
   
   
//   private ActionBarHelper createActionBarHelper() {
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//			return new ActionBarHelperICS();
//		} else {
//			return new ActionBarHelper();
//		}
//	}
//
//	private class ActionBarHelper {
//		public void init() {}
//		public void onDrawerClosed() {}
//		public void onDrawerOpened() {}
//		public void setTitle(CharSequence title) {}
//	}
//	
//	private class ActionBarHelperICS extends ActionBarHelper {
//		private final ActionBar mActionBar;
//		private CharSequence mDrawerTitle;
//		private CharSequence mTitle;
//
//		ActionBarHelperICS() {
//			mActionBar = getActionBar();
//		}
//
//		@Override
//		public void init() {
//			mActionBar.setDisplayHomeAsUpEnabled(true);
//			mActionBar.setHomeButtonEnabled(true);
//			mTitle = mDrawerTitle = getTitle();
//		}
//
//		@Override
//		public void onDrawerClosed() {
//			super.onDrawerClosed();
//			mActionBar.setTitle(mTitle);
//		}
//
//		@Override
//		public void onDrawerOpened() {
//			super.onDrawerOpened();
//			mActionBar.setTitle(mDrawerTitle);
//		}
//
//		@Override
//		public void setTitle(CharSequence title) {
//			mTitle = title;
//		}
//	}
	
//	private class DemoDrawerListener implements DrawerLayout.DrawerListener {
//		@Override
//		public void onDrawerOpened(View drawerView) {
//			mDrawerToggle.onDrawerOpened(drawerView);
//			mActionBar.onDrawerOpened();
//		}
//
//		@Override
//		public void onDrawerClosed(View drawerView) {
//			mDrawerToggle.onDrawerClosed(drawerView);
//			mActionBar.onDrawerClosed();
//		}
//
//		@Override
//		public void onDrawerSlide(View drawerView, float slideOffset) {
//			mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
//		}
//
//		@Override
//		public void onDrawerStateChanged(int newState) {
//			mDrawerToggle.onDrawerStateChanged(newState);
//		}
//	}
	
	@Override
   protected void onPostCreate(Bundle savedInstanceState) {
       super.onPostCreate(savedInstanceState);
       mDrawerToggle.syncState();
   }

}