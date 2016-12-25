package ru.rkarasev.miptrain;

import java.util.ArrayList;

import ru.rkarasev.miptrain.downloadservice.FullestDataDownload;
import ru.rkarasev.miptrain.downloadservice.TrainsDBManager;
import ru.rkarasev.miptrain.gui.DataChanger;
import ru.rkarasev.miptrain.gui.DataListFragment;
import ru.rkarasev.miptrain.gui.PrefsActivity;
import ru.rkarasev.miptrain.utils.BriefContentSorting;
import ru.rkarasev.miptrain.utils.BriefTrain;
import ru.rkarasev.miptrain.utils.ContentSorting;
import ru.rkarasev.miptrain.utils.FinalData;
import ru.rkarasev.miptrain.utils.Train;
import ru.rkarasev.miptrain.utils.Utils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

	
public class MainActivity extends Activity {
	private boolean isRefreshing = false;
	public final static int TO = 0;
    public final static int FROM = 1;
	public static Context appContext;
	ActionBar actionbar;
	DataListFragment ToFragment;
    DataListFragment FromFragment;
    ActionBar.Tab FromTab;
    ActionBar.Tab ToTab;
    ArrayList<Train> fullTrainArrayList;
    ArrayList<BriefTrain> briefTrainArrayList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_main);
		FromFragment = new DataListFragment();
	    ToFragment = new DataListFragment();
		actionbar = getActionBar();
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        FromTab = actionbar.newTab().setText("Из Москвы");
        ToTab = actionbar.newTab().setText("На Москву");
        ToTab.setTabListener(new MyTabsListener(ToFragment, this, ToTab, this, TO, getActionBar()));
        FromTab.setTabListener(new MyTabsListener(FromFragment, this, FromTab, this, FROM, getActionBar()));
        
        actionbar.addTab(ToTab);
        actionbar.addTab(FromTab);
        SharedPreferences startPref = getSharedPreferences("MyPref", MODE_PRIVATE);
	    if (startPref.getString("ELNUMBER", "").equals("")) {
	    	startPref.edit().putString("ELNUMBER", "0").commit();
	    }
	    if (startPref.getString("FREQUENCY", "").equals("")) {
	    	startPref.edit().putString("FREQUENCY", "0").commit();
	    }
	    
	    long basicperiod = 1400 * 60 * 1000;
	    FullestDataDownload fdd = new FullestDataDownload();
	    fdd.setDownload(this, Integer.valueOf(startPref.getString("FREQUENCY", "")) * basicperiod);
	}
	@Override
	public void onResume() {
	    super.onResume();	    
	    if (actionbar.getSelectedTab() == ToTab) {
    		TrainsDBManager dbManager = getDBManager();
			FragmentManager fm = getFragmentManager();
			ArrayList<FinalData> newContent = (new ContentSorting(this, TO)).sortContent(dbManager.getData());
			ToFragment = DataChanger.briefDataChange(fm.beginTransaction(), newContent, ToTab, this, actionbar, this, TO);
			(fm.beginTransaction()).replace(R.id.fragment_container, ToFragment);
    	} else {
    		TrainsDBManager dbManager = getDBManager();
			FragmentManager fm = getFragmentManager();
			ArrayList<FinalData> newContent = (new ContentSorting(this, FROM)).sortContent(dbManager.getData());
			FromFragment = DataChanger.briefDataChange(fm.beginTransaction(), newContent, FromTab, this, actionbar, this, FROM);
			(fm.beginTransaction()).replace(R.id.fragment_container, FromFragment);
    	}	    
	    
	    SharedPreferences startPref = getSharedPreferences("MyPref", MODE_PRIVATE);
	    if (startPref.getString("REFRESHED", "").equals("")) {
	    	startPref.edit().putString("REFRESHED", "NULL").commit();
	    }
	    if (startPref.getString("REFRESHED", "").equals("NULL")) {
	    	Toast.makeText(this, "База данных ни разу не обновлялась", Toast.LENGTH_SHORT).show();
	    } else {
	    	if (Utils.updatedOutput(startPref.getString("REFRESHED", "")) != null) {
	    		Toast.makeText(this, Utils.updatedOutput(startPref.getString("REFRESHED", "")), Toast.LENGTH_SHORT).show();
	    	}	    	
	    }
	    
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	} 
	  @Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.mainmenu, menu);
	    return true;
	  }
	  @Override
	  public boolean onOptionsItemSelected(MenuItem item) {
	        if(item.isChecked())
	            item.setChecked(false);
	        else
	            item.setChecked(true);
	    
	    switch (item.getItemId()) {
	    case R.id.action_settings:
	    	Intent intent = new Intent(this, PrefsActivity.class);
	        startActivity(intent);
	      break;
	    case R.id.menu_refresh:	  
	    	SharedPreferences startPref = getSharedPreferences("MyPref", MODE_PRIVATE);
            String fromName;
            String toName;
            
            int direction;
	    	if (actionbar.getSelectedTab() == ToTab) {
	    		if (startPref.getString("STARTTO", "").equals("NOVO")) {
	    			fromName = "Новодачная";
	    		} else {
	    			fromName = "Долгопрудная";
	    		}
	    		if (startPref.getString("STARTTO", "").equals("SAVY")) {
	    			toName = "Москва (Савёловский вокзал)";
	    		} else {
	    			toName = "Тимирязевская";
	    		}
                direction = TO;
	    	} else {
	    		if (startPref.getString("STARTTO", "").equals("NOVO")) {
	    			toName = "Новодачная";
	    		} else {
	    			toName = "Долгопрудная";
	    		}
	    		if (startPref.getString("STARTTO", "").equals("SAVY")) {
	    			fromName = "Москва (Савёловский вокзал)";
	    		} else {
	    			fromName = "Тимирязевская";
	    		}

                direction = FROM;
	    	}
	    	if(mayRefresh()){
	    		Thread briefThread = new Thread(new BriefInformationDownloadThread(Utils.getCurrentDate(), fromName, toName, direction, this));
	    		briefThread.start();
	    	}
	    	
	      break;
	    default:
	      break;
	    }

	    return true;
	  }
	  private synchronized boolean mayRefresh(){
		  if(isRefreshing){
			  return false;
		  } else {
			  isRefreshing = true;
			  return true;
		  }
	  }
	  private synchronized void refreshed(){
		  isRefreshing = false;
	  }
	  public void changeFrom (MenuItem item) {
		  SharedPreferences startPref = getSharedPreferences("MyPref", MODE_PRIVATE);
	      Editor startEditor = startPref.edit();
		  switch (item.getItemId()) {
		    case R.id.menuFromTimityazevskaya:		    	
		    	startEditor.putString("STARTFROM", "TIMI");
		    	startEditor.commit();
		    	if (actionbar.getSelectedTab() == ToTab) {
		    		TrainsDBManager dbManager = getDBManager();
					FragmentManager fm = getFragmentManager();
					ArrayList<FinalData> newContent = (new ContentSorting(this, TO)).sortContent(dbManager.getData());
					ToFragment = DataChanger.briefDataChange(fm.beginTransaction(), newContent, ToTab, this, actionbar, this, TO);
					(fm.beginTransaction()).replace(R.id.fragment_container, ToFragment);
		    	} else {
		    		TrainsDBManager dbManager = getDBManager();
					FragmentManager fm = getFragmentManager();
					ArrayList<FinalData> newContent = (new ContentSorting(this, FROM)).sortContent(dbManager.getData());
					FromFragment = DataChanger.briefDataChange(fm.beginTransaction(), newContent, FromTab, this, actionbar, this, FROM);
					(fm.beginTransaction()).replace(R.id.fragment_container, FromFragment);
		    	}
		    	
		    	break;
		    case R.id.menuFromSavyolovskiyVokzal:
		    	startEditor.putString("STARTFROM", "SAVY");
		    	startEditor.commit();
		    	
		    	if (actionbar.getSelectedTab() == ToTab) {
		    		TrainsDBManager dbManager = getDBManager();
					FragmentManager fm = getFragmentManager();
					ArrayList<FinalData> newContent = (new ContentSorting(this, TO)).sortContent(dbManager.getData());
					ToFragment = DataChanger.briefDataChange(fm.beginTransaction(), newContent, ToTab, this, actionbar, this, TO);
					(fm.beginTransaction()).replace(R.id.fragment_container, ToFragment);
		    	} else {
		    		TrainsDBManager dbManager = getDBManager();
					FragmentManager fm = getFragmentManager();
					ArrayList<FinalData> newContent = (new ContentSorting(this, FROM)).sortContent(dbManager.getData());
					FromFragment = DataChanger.briefDataChange(fm.beginTransaction(), newContent, FromTab, this, actionbar, this, FROM);
					(fm.beginTransaction()).replace(R.id.fragment_container, FromFragment);
		    	}
		    	
		    	break;
			      
		    case R.id.menuFromNovodachnaya:
		    	startEditor.putString("STARTTO", "NOVO");
		    	startEditor.commit();
		    	
		    	if (actionbar.getSelectedTab() == ToTab) {
		    		TrainsDBManager dbManager = getDBManager();
					FragmentManager fm = getFragmentManager();
					ArrayList<FinalData> newContent = (new ContentSorting(this, TO)).sortContent(dbManager.getData());
					ToFragment = DataChanger.briefDataChange(fm.beginTransaction(), newContent, ToTab, this, actionbar, this, TO);
					(fm.beginTransaction()).replace(R.id.fragment_container, ToFragment);
		    	} else {
		    		TrainsDBManager dbManager = getDBManager();
					FragmentManager fm = getFragmentManager();
					ArrayList<FinalData> newContent = (new ContentSorting(this, FROM)).sortContent(dbManager.getData());
					FromFragment = DataChanger.briefDataChange(fm.beginTransaction(), newContent, FromTab, this, actionbar, this, FROM);
					(fm.beginTransaction()).replace(R.id.fragment_container, FromFragment);
		    	}
		    	
    		    break;
			      
		    case R.id.menuFromDolgoprudnaya:
		    	startEditor.putString("STARTTO", "DOLG");
		    	startEditor.commit();
		    	
		    	if (actionbar.getSelectedTab() == ToTab) {
		    		TrainsDBManager dbManager = getDBManager();
					FragmentManager fm = getFragmentManager();
					ArrayList<FinalData> newContent = (new ContentSorting(this, TO)).sortContent(dbManager.getData());
					ToFragment = DataChanger.briefDataChange(fm.beginTransaction(), newContent, ToTab, this, actionbar, this, TO);
					(fm.beginTransaction()).replace(R.id.fragment_container, ToFragment);
		    	} else {
		    		TrainsDBManager dbManager = getDBManager();
					FragmentManager fm = getFragmentManager();
					ArrayList<FinalData> newContent = (new ContentSorting(this, FROM)).sortContent(dbManager.getData());
					FromFragment = DataChanger.briefDataChange(fm.beginTransaction(), newContent, FromTab, this, actionbar, this, FROM);
					(fm.beginTransaction()).replace(R.id.fragment_container, FromFragment);
		    	}
		    	
			    break;
			      
		    default:
		      break;
		    }		  
	  }
	  public void FullRefreshThreadListener(ArrayList<Train> receivedArrayList, int direction) {
		  Looper.prepare();
		  if (receivedArrayList == null) {
			  Toast.makeText(this, "Невозможно скачать расписание", Toast.LENGTH_SHORT).show();
		  } else {
			  	fullTrainArrayList = receivedArrayList;
		  		ContentSorting contentSorter = new ContentSorting(this, direction);
		  		ArrayList<FinalData> newContent = contentSorter.sortContent(receivedArrayList);
		  		if (direction == TO) {	
			  		ToFragment = DataChanger.briefDataChange(getFragmentManager().beginTransaction(), newContent, ToTab, this, getActionBar(), this, TO);
		  		} else {
			  		FromFragment = DataChanger.briefDataChange(getFragmentManager().beginTransaction(), newContent, FromTab, this, getActionBar(), this, FROM);
		  		}
		  }
		  refreshed();
	  }

	  public void BriefRefreshThreadListener(ArrayList<BriefTrain> receivedArrayList, int direction) {
		  Looper.prepare();
		  if (receivedArrayList == null) {
			  Toast.makeText(this, "Невозможно скачать расписание", Toast.LENGTH_SHORT).show();
		  } else {
			  	briefTrainArrayList = receivedArrayList;
		  		ArrayList<FinalData> newContent;
		  		BriefContentSorting contentSorter = new BriefContentSorting(this, direction);
		  		newContent = contentSorter.sortContent(receivedArrayList);
		  		if (direction == TO) {			  			  
			  		ToFragment = DataChanger.briefDataChange(getFragmentManager().beginTransaction(), newContent, ToTab, this, getActionBar(), this, TO);
		  		} else {
			  		FromFragment = DataChanger.briefDataChange(getFragmentManager().beginTransaction(), newContent, FromTab, this, getActionBar(), this, FROM);
		  		}
		  		ArrayList<String> links = new ArrayList<String>();
		  		for(FinalData briefTrain : newContent) {
			  		links.add(briefTrain.getLink());
			  		System.out.println(briefTrain.getLink());      //PRINT
		  		}
		  		
	      		Thread thread = new Thread(new FullInformationDownloadThread(links, this, direction));
	      		thread.start();
		  }
	  }	  
	  public TrainsDBManager getDBManager() {
		  return (new TrainsDBManager(this));
	  }
      
      
}
