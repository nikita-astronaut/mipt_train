package ru.rkarasev.miptrain.gui;

import ru.rkarasev.miptrain.R;
import android.app.FragmentTransaction;

public class ListChanger {
	public void changeFragment(FragmentTransaction ft, DataListFragment newListFragment) {
		ft.replace(R.id.fragment_container, newListFragment);
		ft.commit();
	}

}
