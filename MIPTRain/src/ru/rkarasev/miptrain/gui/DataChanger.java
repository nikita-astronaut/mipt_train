package ru.rkarasev.miptrain.gui;

import java.util.ArrayList;

import ru.rkarasev.miptrain.gui.CustomArrayAdapter;
import ru.rkarasev.miptrain.gui.DataListFragment;
import ru.rkarasev.miptrain.utils.FinalData;
import ru.rkarasev.miptrain.MainActivity;
import ru.rkarasev.miptrain.MyTabsListener;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;

public class DataChanger {
	public static DataListFragment briefDataChange(FragmentTransaction ft, ArrayList<FinalData> data, ActionBar.Tab tab, Context context, ActionBar actionBar, MainActivity activity, int direction) {
		ListChanger listChanger = new ListChanger();
		CustomArrayAdapter adapter = new CustomArrayAdapter(context);
		adapter.setData(data);
		DataListFragment newListFragment = new DataListFragment();
		newListFragment.setAdapter(adapter);
		tab.setTabListener(new MyTabsListener(newListFragment, context, tab, activity, direction, actionBar));
		if (actionBar.getSelectedTab() == tab) {
			listChanger.changeFragment(ft, newListFragment);
		}
		return newListFragment;
	}
}
