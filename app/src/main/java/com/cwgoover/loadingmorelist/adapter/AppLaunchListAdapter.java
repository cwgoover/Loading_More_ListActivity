package com.cwgoover.loadingmorelist.adapter;

import java.util.List;

import com.cwgoover.loadingmorelist.R;
import com.cwgoover.loadingmorelist.VirtualData;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppLaunchListAdapter extends ArrayAdapter<VirtualData> {

    private static LayoutInflater mInflater = null;

    private final List<VirtualData> mData;
    private final String defaultBootFlag;
    private final String defaultBootTime;


    public AppLaunchListAdapter(Activity activity, List<VirtualData> data) {
        super(activity, 0, data);
        mData = data;
        mInflater = activity.getLayoutInflater();

        defaultBootFlag = activity.getResources().getString(R.string.boot_flag);
        defaultBootTime = activity.getResources().getString(R.string.boot_time);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_app_launch, null);

            holder = new ViewHolder();
            holder.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
            holder.header = (TextView) convertView.findViewById(R.id.header);
            holder.label = (TextView) convertView.findViewById(R.id.app_label);
            holder.bootFlag = (TextView) convertView.findViewById(R.id.app_boot_flag);
            holder.bootTime = (TextView) convertView.findViewById(R.id.app_boot_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String currentLabel = mData.get(position).getText();
        if (position == 0 || mData.get(position - 1).getText().charAt(0) != currentLabel.charAt(0)) {
            holder.header.setVisibility(View.VISIBLE);
            holder.header.setText(currentLabel.substring(0, 1));
        } else {
            holder.header.setVisibility(View.GONE);
        }
//        holder.appIcon.setImageResource(R.drawable.ic_launcher);
        holder.appIcon.setBackgroundResource(R.mipmap.ic_launcher);
        holder.label.setText(mData.get(position).getText());
        holder.bootFlag.setText(defaultBootFlag);
        holder.bootTime.setText(defaultBootTime);
        return convertView;
    }

    private class ViewHolder {
        TextView header;
        ImageView appIcon;
        TextView label;
        TextView bootFlag;
        TextView bootTime;
    }
}
