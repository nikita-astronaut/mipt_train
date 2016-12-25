package ru.rkarasev.miptrain.gui;


import ru.rkarasev.miptrain.downloadservice.FullestDataDownload;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity implements TrainDialogFragment.TrainDialogListener, SynchTimeDialogFragment.SynchTimeDialogListener, PrefsFragment.TrainStationListener {
	 public static Context appContext;
	 
	 
	 
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  PrefsFragment newPrefsFragment = new PrefsFragment();
	  newPrefsFragment.setContext(this);
	  getFragmentManager().beginTransaction().replace(android.R.id.content,
	                newPrefsFragment).commit();	         
	  }	 
	    public void onTrainDialogPositiveClick(DialogFragment dialog, int selectedItem) {
	        SharedPreferences elNumberPref = getSharedPreferences("MyPref", MODE_PRIVATE);
	    	Editor elNumberEditor = elNumberPref.edit();
	    	elNumberEditor.putString("ELNUMBER", Integer.toString(selectedItem));
	    	elNumberEditor.commit();
	    }
	    public void onSynchTimeDialogPositiveClick(DialogFragment dialog, int selectedItem) {
	    	SharedPreferences freqPref = getSharedPreferences("MyPref", MODE_PRIVATE);
	    	Editor freqEditor = freqPref.edit();
	    	switch (selectedItem) {
	    		case 0:
	    			freqEditor.putString("FREQUENCY", "1");
	    			break;
	    		case 1:
	    			freqEditor.putString("FREQUENCY", "2");
	    			break;
	    		case 2:
	    			freqEditor.putString("FREQUENCY", "3");
	    			break;
	    		case 3:
	    			freqEditor.putString("FREQUENCY", "7");
	    			break;
	    		case 4:
	    			freqEditor.putString("FREQUENCY", "14");
	    			break;
	    			
	    	}
	    	
	        freqEditor.commit();
	        long basicperiod = 1400 * 60 * 1000;
		    FullestDataDownload fdd = new FullestDataDownload();
		    fdd.setDownload(this, Integer.valueOf(freqPref.getString("FREQUENCY", "")) * basicperiod);
	    }
	    public void onTrainStationListenerClick(CheckBoxPreference dolg, CheckBoxPreference novo, CheckBoxPreference mark, CheckBoxPreference lian, CheckBoxPreference besk, CheckBoxPreference degu, CheckBoxPreference okry, CheckBoxPreference timi, CheckBoxPreference savy) {
	    	SharedPreferences stationPref = getSharedPreferences("MyPref", MODE_PRIVATE);
	    	Editor freqEditor = stationPref.edit();
	    	//REFRESH CONTENT HERE!!!
	    	if (dolg.isChecked()) {
	    		freqEditor.putString("dolg", "Y");
	    	} else {
	    		freqEditor.putString("dolg", "N");
	    	}
	    	
	    	if (novo.isChecked()) {
	    		freqEditor.putString("novo", "Y");
	    	} else {
	    		freqEditor.putString("novo", "N");
	    	}
	    	
	    	if (mark.isChecked()) {
	    		freqEditor.putString("mark", "Y");
	    	} else {
	    		freqEditor.putString("mark", "N");
	    	}
	    	
	    	if (lian.isChecked()) {
	    		freqEditor.putString("lian", "Y");
	    	} else {
	    		freqEditor.putString("lian", "N");
	    	}
	    	
	    	if (besk.isChecked()) {
	    		freqEditor.putString("besk", "Y");
	    	} else {
	    		freqEditor.putString("besk", "N");
	    	}
	    	
	    	if (degu.isChecked()) {
	    		freqEditor.putString("degu", "Y");
	    	} else {
	    		freqEditor.putString("degu", "N");
	    	}
	    	
	    	if (okry.isChecked()) {
	    		freqEditor.putString("okry", "Y");
	    	} else {
	    		freqEditor.putString("okry", "N");
	    	}
	    	
	    	if (timi.isChecked()) {
	    		freqEditor.putString("timi", "Y");
	    	} else {
	    		freqEditor.putString("timi", "N");
	    	}
	    	
	    	if (savy.isChecked()) {
	    		freqEditor.putString("savy", "Y");
	    	} else {
	    		freqEditor.putString("savy", "N");
	    	}
	    	
	        freqEditor.commit();        
	    }
	    
}
