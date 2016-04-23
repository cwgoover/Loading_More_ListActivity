package com.cwgoover.loadingmorelist.adapter;

import java.util.List;

import com.cwgoover.loadingmorelist.LoadingMoreActivity.DetailActionInfo;
import com.cwgoover.loadingmorelist.R;
import com.cwgoover.loadingmorelist.view.TimeUsageGraph;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class DetailGraphListAdapter extends ArrayAdapter<DetailActionInfo> {

    private final static String LONG_ACTION_NAME = "bindAppTime";

    private final Context mContext;
    private final List<DetailActionInfo> mItems;

    String suffix;
    int shrinkSize;
    boolean shrinkText = true;

    public DetailGraphListAdapter(Activity activity, List<DetailActionInfo> items) {
        super(activity, 0, items);
        mContext = activity;
        mItems = items;
        suffix = activity.getResources().getString(R.string.millisecond_short);
        shrinkSize = mContext.getResources().getDimensionPixelSize(R.dimen.graph_detail_bar_shrink_text_width);

        for (DetailActionInfo item : items) {
            if (shrinkText && item.getAction().contains(LONG_ACTION_NAME)) {
                shrinkText = false;
            }
        }
    }

    @Override
    public boolean isEnabled(int position) {
        // disable click event
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_detail_graph, null);
        }
        TextView action = (TextView) convertView.findViewById(R.id.action);
        TextView actionTime = (TextView) convertView.findViewById(R.id.action_time);
        TimeUsageGraph graph = (TimeUsageGraph) convertView.findViewById(R.id.time_bar);

        long at = mItems.get(position).getActionTime();
        long ant = mItems.get(position).getAnimationTime();
        String as = String.format("%s:", mItems.get(position).getAction());
        String ats = String.format("%d%s", at, suffix);

        if (shrinkText) {
            // Get the TextView current LayoutParams
            LayoutParams lp = (LinearLayout.LayoutParams) action.getLayoutParams();
            lp.width = shrinkSize;
            // Apply the updated layout parameters to TextView
            action.setLayoutParams(lp);
        }
        action.setText(as);
        actionTime.setText(ats);
        graph.setLevels(at, ant, true);
        return convertView;
    }
}
