package com.cwgoover.loadingmorelist.view;

import com.cwgoover.loadingmorelist.R;
import com.cwgoover.loadingmorelist.SingleFileDataSet;
import com.cwgoover.loadingmorelist.RegexParser.LaunchCourse;
import com.cwgoover.loadingmorelist.adapter.DetailGraphListAdapter;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AppLaunchDetailDialog extends DialogFragment {

    private static final String APP_ICON = "app_icon";

    private ListView mListView;
    private LaunchCourse mCourse;

    public static AppLaunchDetailDialog newInstance(byte[] icon, LaunchCourse course) {
        AppLaunchDetailDialog frag = new AppLaunchDetailDialog();
        Bundle args = new Bundle();
        args.putByteArray(APP_ICON, icon);
        frag.setArguments(args);
        frag.setAppLaunchCourse(course);
        return frag;
    }

    public void setAppLaunchCourse(LaunchCourse course) {
        mCourse = course;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        DetailGraphListAdapter adapter = new DetailGraphListAdapter(getActivity(), SingleFileDataSet.createListData());
        mListView.setAdapter(adapter);
        // remove the separator between items in the same listView
        mListView.setDivider(null);
        mListView.setDividerHeight(0);
    }

    /**
     * Use onCreateView when the entire view of the dialog is going to be
     * defined via custom XML
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialog_list_detail_item, container);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        TextView clsName = (TextView) view.findViewById(R.id.app_class);
        TextView pkgName = (TextView) view.findViewById(R.id.app_package);
        TextView timePoint = (TextView) view.findViewById(R.id.time_point);
        TextView totalTime = (TextView) view.findViewById(R.id.total_time);
        TextView partialTime = (TextView) view.findViewById(R.id.partial_time);
        TimeUsageGraph graph = (TimeUsageGraph) view.findViewById(R.id.time_graph);
        mListView = (ListView) view.findViewById(R.id.detail_list);

        byte[] b = getArguments().getByteArray(APP_ICON);
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        icon.setImageBitmap(bmp);

        clsName.setText(mCourse.getClassName());
        pkgName.setText(mCourse.getPackageName());
        timePoint.setText(mCourse.getStartTime());
        totalTime.setText(getResources().getString(R.string.launch_activity_detail_total_time,
                mCourse.getKeyValue("totalTime")));
        partialTime.setText(getResources().getString(R.string.launch_activity_detail_partial_time,
                mCourse.getKeyValue("responseTime"), mCourse.getKeyValue("animationTime")));
        graph.setLevels(mCourse.getKeyValue("responseTime"), mCourse.getKeyValue("animationTime"), false);

        return view;
    }


    /**
     *  If you have both onCreateView() and onCreateDialog() implemented, you run
     * the risk of getting the "requestFeature() must be called before adding content"
     * crash.
     *  This is because BOTH onCreateDialog() then onCreateView() are called when
     * you show() that fragment as a dialog (why, I don't know). The inflate() in
     * onCreateView() after a dialog was created in onCreateDialog()
     * is what causes the crash.
     *
     * Reference:
     *  http://stackoverflow.com/questions/13257038/custom-layout-for-dialogfragment-oncreateview-vs-oncreatedialog
     */
}
