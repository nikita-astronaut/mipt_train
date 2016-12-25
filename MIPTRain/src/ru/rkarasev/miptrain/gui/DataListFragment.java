package ru.rkarasev.miptrain.gui;


import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class DataListFragment extends ListFragment {   
    private CustomArrayAdapter mAdapter;
    public void setAdapter(CustomArrayAdapter adapter) {
    	this.mAdapter = adapter;
    	setListAdapter(mAdapter);
    }
    @Override 
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText("");
        mAdapter = new CustomArrayAdapter(getActivity());
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    }
}
