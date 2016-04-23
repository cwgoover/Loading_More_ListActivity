package com.cwgoover.loadingmorelist.view;

import android.widget.AbsListView;

import com.cwgoover.loadingmorelist.LoadingMoreActivity;

public abstract class InfiniteScrollListener implements AbsListView.OnScrollListener {
    private static final String TAG = LoadingMoreActivity.TAG + ".liter";

    /**
     * The minimum amount of items to have below your current scroll
     * position, before loading more.
     */
    private final int mBufferItemCount;

    /**
     * The current page of data you have loaded.
     */
    private int mCurrentPage = 0;

    /**
     * The total number of items in the dataset after the last load.
     */
    private int mItemCount = 0;

    /**
     * True if we are still waiting for the last set of data to load.
     */
    private boolean mIsLoading = true;

    private int mTopVisiblePosition = -1;

    public InfiniteScrollListener(int bufferItemCount) {
        mBufferItemCount = bufferItemCount;
    }

    /** Defines the process for actually loading more data based on page */
    public abstract void loadMore(int page, int totalItemsCount);
    public abstract void setTopHeader(int pos);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take any action on changed
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
        //Log.d(TAG, "onScroll: totalItemCount=" + totalItemCount);
        // update the most top item of list's header
        if (firstVisibleItem != mTopVisiblePosition) {
            mTopVisiblePosition = firstVisibleItem;
            setTopHeader(firstVisibleItem);
        }

        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state
        if (totalItemCount < mItemCount) {
            // Note: totalItemCount adds footer view as one item
            mItemCount = totalItemCount;

            if (totalItemCount == 0) {
                mIsLoading = true;
            }
        }

        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (mIsLoading && (totalItemCount > mItemCount)) {
            // Log.d(TAG, "onScroll: totalItemCount > mItemCount, so mCurrentPage=" + mCurrentPage
            // +", mItemCount=" + mItemCount + ", totalItemCount=" + totalItemCount);
            mIsLoading = false;
            mItemCount = totalItemCount;
            mCurrentPage++;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the mBufferItemCount and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if(!mIsLoading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + mBufferItemCount)) {
            loadMore(mCurrentPage + 1, totalItemCount);
            mIsLoading = true;
        }
    }
}
