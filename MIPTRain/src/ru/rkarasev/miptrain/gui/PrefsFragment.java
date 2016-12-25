package ru.rkarasev.miptrain.gui;


import ru.rkarasev.miptrain.R;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
public class PrefsFragment extends PreferenceFragment {	
	 private  Context context;
	 public void setContext(Context context) {
		 this.context = context;
	 }
	 public interface TrainStationListener {
         public void onTrainStationListenerClick(CheckBoxPreference dolg, CheckBoxPreference novo, CheckBoxPreference mark, CheckBoxPreference lian, CheckBoxPreference besk, CheckBoxPreference degu, CheckBoxPreference okry, CheckBoxPreference timi, CheckBoxPreference savy);
     }  
	 
	 
	 TrainStationListener mListener;
	 CheckBoxPreference novoStation;
	 CheckBoxPreference dolgStation;
	 CheckBoxPreference markStation;
	 CheckBoxPreference lianStation;
	 CheckBoxPreference beskStation;
	 CheckBoxPreference deguStation;
	 CheckBoxPreference okryStation;
	 CheckBoxPreference timiStation;
	 CheckBoxPreference savyStation;
	 
	 @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        try {
	            mListener = (TrainStationListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString()
	                    + " must implement NoticeDialogListener");
	        }
	    }
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	addPreferencesFromResource(R.xml.preferences_scenario1);
    	
  	  SharedPreferences stationPref = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
  	  String[] values = {
  			  "dolg",
  			  "novo",
  			  "mark",
  			  "lian",
  			  "degu",
  			  "besk",
  			  "okry",
  			  "timi",
  			  "savy"
  	  };
  	  
  	  for (int i = 0; i < 9; i++) {
  		  if (stationPref.getString(values[i], "").equals("")) {
  	  		  stationPref.edit().putString(values[i], "N");
  	  	  	  stationPref.edit().commit();
  	  	  }
  	  }
    	  for (int i = 0; i < 9; i++) {
    		CheckBoxPreference station = (CheckBoxPreference) findPreference(values[i]);
    		if (stationPref.getString(values[i], "").equals("Y")) {
    			station.setChecked(true);
    		}
    		if (stationPref.getString(values[i], "").equals("N")) {
    			station.setChecked(false);
    		}
    	  }
    	
    	novoStation = (CheckBoxPreference) findPreference("novo");
    	dolgStation = (CheckBoxPreference) findPreference("dolg");
    	markStation = (CheckBoxPreference) findPreference("mark");
    	lianStation = (CheckBoxPreference) findPreference("lian");
    	deguStation = (CheckBoxPreference) findPreference("degu");
    	beskStation = (CheckBoxPreference) findPreference("besk");
    	okryStation = (CheckBoxPreference) findPreference("okry");
    	timiStation = (CheckBoxPreference) findPreference("timi");
    	savyStation = (CheckBoxPreference) findPreference("savy");
    	
    	novoStation.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
            	mListener.onTrainStationListenerClick(dolgStation, novoStation, markStation, lianStation, deguStation, beskStation, okryStation, timiStation, savyStation);
              return false;
            }
          }); 
    	
    	dolgStation.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
            	mListener.onTrainStationListenerClick(dolgStation, novoStation, markStation, lianStation, deguStation, beskStation, okryStation, timiStation, savyStation);
              return false;
            }
          }); 
    	
    	markStation.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
            	mListener.onTrainStationListenerClick(dolgStation, novoStation, markStation, lianStation, deguStation, beskStation, okryStation, timiStation, savyStation);
              return false;
            }
          }); 
    	
    	lianStation.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
            	mListener.onTrainStationListenerClick(dolgStation, novoStation, markStation, lianStation, deguStation, beskStation, okryStation, timiStation, savyStation);
              return false;
            }
          }); 
    	
    	deguStation.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
            	mListener.onTrainStationListenerClick(dolgStation, novoStation, markStation, lianStation, deguStation, beskStation, okryStation, timiStation, savyStation);
              return false;
            }
          }); 
    	
    	beskStation.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
            	mListener.onTrainStationListenerClick(dolgStation, novoStation, markStation, lianStation, deguStation, beskStation, okryStation, timiStation, savyStation);
              return false;
            }
          }); 
    	
    	okryStation.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
            	mListener.onTrainStationListenerClick(dolgStation, novoStation, markStation, lianStation, deguStation, beskStation, okryStation, timiStation, savyStation);
              return false;
            }
          }); 
    	
    	timiStation.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
            	mListener.onTrainStationListenerClick(dolgStation, novoStation, markStation, lianStation, deguStation, beskStation, okryStation, timiStation, savyStation);
              return false;
            }
          }); 
    	
    	savyStation.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
            	mListener.onTrainStationListenerClick(dolgStation, novoStation, markStation, lianStation, deguStation, beskStation, okryStation, timiStation, savyStation);
              return false;
            }
          }); 
    	    	    	
    	Preference trainNumberDialog = (Preference) findPreference("trainnumberdialog");
        trainNumberDialog.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                DialogFragment newFragment = new TrainDialogFragment();
                newFragment.show(getFragmentManager(), "");
              return false;
            }
          }); 
        
        Preference synchTimeDialog = (Preference) findPreference("synchtimedialog");
        synchTimeDialog.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                DialogFragment newFragment = new SynchTimeDialogFragment();
                newFragment.show(getFragmentManager(), "");
              return false;
            }
          });
             
    }    
}

