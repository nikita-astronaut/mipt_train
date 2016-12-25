package ru.rkarasev.miptrain.gui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class SynchTimeDialogFragment extends DialogFragment {
	private int selectedItem = -1;
	public interface SynchTimeDialogListener {
        public void onSynchTimeDialogPositiveClick(DialogFragment dialog, int which);
    }
	SynchTimeDialogListener mListener;
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SynchTimeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }
	
    @Override    
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final CharSequence[] choiceList = 
        {"1 сутки", "2 суток", "3 суток", "1 неделя", "2 недели"};
         
        int selected = -1; // does not select anything
        builder.setTitle("Частота синхронизации:").setSingleChoiceItems(choiceList, selected, new DialogInterface.OnClickListener() {            
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	selectedItem = which;
            }
        }).setPositiveButton("Принять", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   mListener.onSynchTimeDialogPositiveClick(SynchTimeDialogFragment.this, selectedItem);
                   }
               })
               .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {

                   }
               });
        return builder.create();
    }
}
