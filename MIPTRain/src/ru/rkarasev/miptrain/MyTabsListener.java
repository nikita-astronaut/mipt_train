package ru.rkarasev.miptrain;

import java.util.ArrayList;

import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import ru.rkarasev.miptrain.downloadservice.TrainsDBManager;
import ru.rkarasev.miptrain.gui.DataChanger;
import ru.rkarasev.miptrain.utils.ContentSorting;
import ru.rkarasev.miptrain.utils.FinalData;

public class MyTabsListener implements ActionBar.TabListener {
	public final static int TO = 0;
    public final static int FROM = 1;
	private Fragment fragment;
    private int direction;
    private Context context;
    private MainActivity activity;
    private Tab tab;
    private ActionBar actionBar;
    
	public MyTabsListener(Fragment fragment, Context context, Tab tab, MainActivity activity, int direction, ActionBar actionBar) {
		this.fragment = fragment;
		this.direction = direction;
		this.context = context;
		this.activity = activity;
		this.tab = tab;
		this.actionBar = actionBar;
	}
 
@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	
	}
 
@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	ContentSorting contentSorter = new ContentSorting(context, direction);
		try {
			 TrainsDBManager dbManager = activity.getDBManager();
			 FragmentManager fm = activity.getFragmentManager();
			 ArrayList<FinalData> newContent = contentSorter.sortContent(dbManager.getData());
			 fragment = DataChanger.briefDataChange(fm.beginTransaction(), newContent, tab, context, actionBar, activity, direction);
			 ft.replace(R.id.fragment_container, fragment);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		ft.remove(fragment);
	}
}
