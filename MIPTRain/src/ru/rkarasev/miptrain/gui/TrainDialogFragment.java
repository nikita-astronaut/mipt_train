package ru.rkarasev.miptrain.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class TrainDialogFragment extends DialogFragment {
	public interface TrainDialogListener {
        public void onTrainDialogPositiveClick(DialogFragment dialog, int which);
    }
	public static int selectedItem;
	TrainDialogListener mListener;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (TrainDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final CharSequence[] choiceList = 
        {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Все"};
         
        int selected = -1;
        builder.setTitle("Электричек:").setSingleChoiceItems(choiceList, selected, new DialogInterface.OnClickListener() {            
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	if (which == 10) {
            		which = 100;
            	}
            	selectedItem = which;
            }
        }).setPositiveButton("Принять", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   mListener.onTrainDialogPositiveClick(TrainDialogFragment.this, selectedItem);
                   }
               })
               .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {

                   }
               });
        return builder.create();
    }
}
