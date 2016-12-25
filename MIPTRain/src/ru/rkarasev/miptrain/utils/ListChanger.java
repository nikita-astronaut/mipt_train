package ru.rkarasev.miptrain.utils;

import ru.rkarasev.miptrain.R;
import ru.rkarasev.miptrain.gui.DataListFragment;
import android.app.FragmentTransaction;

public class ListChanger {
	public void changeFragment(FragmentTransaction ft, DataListFragment newListFragment) {
		ft.replace(R.id.fragment_container, newListFragment);
		ft.commit();
	}

}
