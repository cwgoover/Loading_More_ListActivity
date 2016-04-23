package com.cwgoover.loadingmorelist.view;

import java.util.Arrays;
import java.util.List;

import com.cwgoover.loadingmorelist.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class SortByDialogFragment extends DialogFragment {

    public static final String DATA = "items";
    public static final String SELECTED = "selected";

    public interface OnHandleSortListener {
        public void onSortItemSelected(int which);
//        public void onSortCancelClicked();
    }

    public static SortByDialogFragment newInstance(String[] items, int selected) {
        SortByDialogFragment frag = new SortByDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray(DATA, items);
        args.putInt(SELECTED, selected);
        frag.setArguments(args);
        return frag;
    }

    /**
     * Use onCreateDialog when you just need to construct and configure a standard
     * Dialog class (such as AlertDialog) to display.
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        List<String> list = Arrays.asList(args.getStringArray(DATA));
        int position = args.getInt(SELECTED);
        CharSequence[] cs = list.toArray(new CharSequence[list.size()]);
        String title = getActivity().getResources().getString(R.string.settings_sort);

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setIcon(R.drawable.ic_menu_sort_size_desc)
                .setSingleChoiceItems(cs, position, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OnHandleSortListener activity = (OnHandleSortListener) getActivity();
                        activity.onSortItemSelected(which);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        OnHandleSortListener activity = (OnHandleSortListener) getActivity();
//                        activity.onSortCancelClicked();
                        dialog.dismiss();
                    }
                }).create();
    }
}
