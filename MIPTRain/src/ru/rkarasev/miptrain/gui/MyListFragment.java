package ru.rkarasev.miptrain.gui;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class MyListFragment extends ListFragment {   
	  private ArrayAdapter<String> adapter;
	  private String[] data;
	  public void setData(String[] data) {
		  this.data = data;
	  }
	  @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		    super.onActivityCreated(savedInstanceState);
		    if (data != null) {
		    	adapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_list_item_1, data);
		    	setListAdapter(adapter);
		    }
		  }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {       
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
