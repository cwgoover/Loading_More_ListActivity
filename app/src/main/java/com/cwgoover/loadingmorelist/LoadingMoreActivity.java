package com.cwgoover.loadingmorelist;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cwgoover.loadingmorelist.RegexParser.LaunchCourse;
import com.cwgoover.loadingmorelist.adapter.AppLaunchListAdapter;
import com.cwgoover.loadingmorelist.view.AppLaunchDetailDialog;
import com.cwgoover.loadingmorelist.view.InfiniteScrollListener;
import com.cwgoover.loadingmorelist.view.SortByDialogFragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LoadingMoreActivity extends Activity implements OnItemClickListener,
        SearchView.OnQueryTextListener, SortByDialogFragment.OnHandleSortListener {

    public static final String TAG = "LoadingMore";

    private static final int BUFFER_ITEM_COUNT = 6;
    private static final int SORT_BY_DEFAULT = 0;
    private static final int SORT_BY_NAME = 1;
    private static final int SORT_BY_FIRST_BOOT = 2;
    private static final int SORT_BY_START_TIME = 3;

    private ListView mListView;
    private TextView mFooter;
    private TextView mTopHeader;
    private SearchView mSearchView;

    private MenuItem mSearchItem;
    private AppLaunchListAdapter mAdapter;
    private MyTask mTask;
    private Resources mResources;

    List<VirtualData> mArrayList = new ArrayList<VirtualData>();
    ArrayList<VirtualData> mBackupData= new ArrayList<VirtualData>();

    private int mSortChoiceToken = 0;
    // XXX: for MyTask
    int TotalCount = 1;
    private int number;     // it must be still equal mArrayList's size(mAdapter.getCount()).

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_more);

        // XXX: create test data
        for (int i = 1; i<= 10; i++){
            VirtualData data = new VirtualData("Initial: " + String.valueOf(i));
            mArrayList.add(data);
        }
        mBackupData.addAll(mArrayList);
        number = mArrayList.size() + 1;

        mResources = getResources();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFooter = (TextView) inflater.inflate(R.layout.footer, null);
        mTopHeader = (TextView) findViewById(R.id.header);
        mListView = (ListView) findViewById(R.id.list);
        mListView.addFooterView(mFooter);

        mAdapter = new AppLaunchListAdapter(this, mArrayList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(new InfiniteScrollListener(BUFFER_ITEM_COUNT) {
            @Override
            public void setTopHeader(int pos) {
                if (pos == 0 && mArrayList.size() == 0) {
                    mTopHeader.setVisibility(View.GONE);
                } else {
                    final String text = mArrayList.get(pos).getText()
                            .substring(0, 1);
                    mTopHeader.setVisibility(View.VISIBLE);
                    mTopHeader.setText(text);
                }
            }

            @Override
            public void loadMore(int page, int totalItemsCount) {
                // TODO Auto-generated method stub
                if (mTask == null || (mTask != null && (mTask.getStatus() == AsyncTask.Status.FINISHED))) {
                    Log.d(TAG, "loadMore: new Mytask && page=" + page +
                            ", totalItemsCount=" + totalItemsCount);
                    mTask = new MyTask();
                    mTask.execute();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        mListView.setAdapter(null);
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO: Auto-generated method stub
        VirtualData data = (VirtualData) parent.getItemAtPosition(position);
        LaunchCourse course = RegexParser.creatLaunchCourse();

        // transfer Bitmap to Adapter through Bundle
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.forum_thread_tag_btn_pressed);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();

        AppLaunchDetailDialog detailView = AppLaunchDetailDialog.newInstance(b, course);
        detailView.show(getFragmentManager(), mResources.getString(R.string.app_launch_item_tag));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);

        Log.d(TAG, "tcao: onCreateOptionsMenu: onCreateOptionsMenu");

        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchItem = menu.findItem(R.id.action_search);
        Log.d(TAG, "tcao: onCreateOptionsMenu: mSearchItem=" + mSearchItem.toString());
        mSearchView = (SearchView) mSearchItem.getActionView();
        mSearchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        mSearchView.setOnQueryTextListener(this);
        // hide SearchView until all the data is loaded.
        mSearchItem.setVisible(false);
        mSearchView.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()) {
            case R.id.menu_sort:
                String[] items = mResources.getStringArray(R.array.settings_sort_items);
                SortByDialogFragment dialog = SortByDialogFragment.newInstance(items, mSortChoiceToken);
                dialog.show(getFragmentManager(), mResources.getString(R.string.settings_sort_tag));
                break;
            case R.id.menu_print:
                toast("click print item");
                break;
            case R.id.menu_settings:
                toast("click settings item");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mArrayList.clear();
        newText = newText.toLowerCase(Locale.getDefault());
        if (newText.length() == 0) {
            mArrayList.addAll(mBackupData);
        } else {
            for (VirtualData vd : mBackupData) {
                if (vd.getText().toLowerCase(Locale.getDefault()).contains(newText)) {
                    mArrayList.add(vd);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public void onSortItemSelected(int which) {
        // TODO Auto-generated method stub
        mSortChoiceToken = which;
        switch (which) {
            case SORT_BY_DEFAULT:
                toast("click sort default item");
                break;
            case SORT_BY_NAME:
                toast("click sort name item");
                break;
            case SORT_BY_FIRST_BOOT:
                toast("click sort first boot item");
                break;
            case SORT_BY_START_TIME:
                toast("click sort start time item");
                break;
        }
    }

    //\-----------------------------------------------------------------------------
    private void toast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    class MyTask extends AsyncTask<Void, Void, Void> {
        private final int MAX_ITEMS_PER_PAGE = 10;
        private final int TOTAL_ITEMS = 60;

        @Override
        protected Void doInBackground(Void... arg0) {
            if (TOTAL_ITEMS > number) {
                SystemClock.sleep(1000);
                for (int i = 1; i <= MAX_ITEMS_PER_PAGE; i++) {
                    switch (TotalCount) {
                        case 1:
                            mArrayList.add(new VirtualData("English: " + number));
                            break;
                        case 2:
                            mArrayList.add(new VirtualData("Frensh: " + number));
                            break;
                        case 3:
                            mArrayList.add(new VirtualData("China: " + number));
                            break;
                        case 4:
                            mArrayList.add(new VirtualData("Belize: " + number));
                            break;
                        default:
                            mArrayList.add(new VirtualData("Greece: " + number));
                            break;
                    }
                    number++;
                }
                if (mArrayList.size() != mBackupData.size()) {
                    mBackupData.clear();
                    mBackupData.addAll(mArrayList);
                }
                TotalCount++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mAdapter.notifyDataSetChanged();

            if (mAdapter.getCount() == TOTAL_ITEMS) {
//                mSearchItem.setVisible(true);
//                mSearchView.setVisibility(View.VISIBLE);
//                mListView.setOnScrollListener(null);
                mListView.removeFooterView(mFooter);
            }
        }
    }

    public static class DetailActionInfo {
        private String action;
        private long actionTime;
        private long animationTime;

        public void setAction(String a) {
            action =a;
        }

        public String getAction() {
            return action;
        }

        public void setActionTime(long time) {
            actionTime = time;
        }

        public long getActionTime() {
            return actionTime;
        }

        public void setAnimationTime(long time) {
            animationTime = time;
        }

        public long getAnimationTime() {
            return animationTime;
        }
    }
}
