package com.cwgoover.loadingmorelist.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.cwgoover.loadingmorelist.R;

public class TimeUsageGraph extends View {

    private final Resources mResources;

    private final int mFirstColor;
    private final int mLastColor;
    private final int mSeparatorColor;
    private final int mSingleColor;
    private final int mSingleBGColor;
    private final int mTextColor;
    private final int mMarkerWidth;
    private final RectF mTmpRect = new RectF();
    private final Paint mTmpPaint = new Paint();

    private long mFirstLevel;
    private long mLastLevel;
    private long mMaxLevel;

    private String mFirstPercent;
    private String mLastPercent;

    boolean mShowSingle;

    public TimeUsageGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        mResources = context.getResources();
        mFirstColor = mResources.getColor(R.color.first_part_color);
        mLastColor = mResources.getColor(R.color.last_part_color);
        mSeparatorColor = mResources.getColor(R.color.graph_separator_color);
        mSingleColor = mResources.getColor(R.color.graph_detail_action_color);
        mSingleBGColor = mResources.getColor(R.color.graph_single_bar_bg);
        mTextColor = mResources.getColor(R.color.grap_text_color);
        mMarkerWidth = mResources.getDimensionPixelSize(R.dimen.graph_marker_width);
    }

    /**
     * If showSingle is true, the last parameter is the total level of the bar;
     * otherwise, the last parameter is the last part of the bar.
     */
    public void setLevels(long first, long last, boolean showSingle) {
        mFirstLevel = Math.max(0, first);
        mLastLevel = Math.max(0, last);
        mMaxLevel = Math.max(Math.max(mFirstLevel, mLastLevel), 1);
        mShowSingle = showSingle;

        if (!mShowSingle) {
            // 四舍五入:round-off
            long fp = Math.round((double)mFirstLevel / (mFirstLevel + mLastLevel) * 100);
            long lp = Math.round((double)mLastLevel / (mFirstLevel + mLastLevel) * 100);
            mFirstPercent = mResources.getString(R.string.graph_text_percent, String.valueOf(fp));
            mLastPercent = mResources.getString(R.string.graph_text_percent, String.valueOf(lp));
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final RectF r = mTmpRect;
        final Paint p = mTmpPaint;
        final int w = getWidth();
        final int h = getHeight();

        float firstRight = w * (mFirstLevel / (float) mMaxLevel);
        float textHeight = h * 3 / 4;

        if (mShowSingle) {
            // draw bar background
            r.set(0, 0, w, h);
            p.setColor(mSingleBGColor);
            canvas.drawRect(r, p);

            // draw value area
            r.set(0, 0, firstRight, h);
            p.setColor(mSingleColor);
            canvas.drawRect(r, p);
        } else {
            // draw first part
            float firstRightBound = firstRight - mMarkerWidth / 2;
            r.set(0, 0, firstRightBound, h);
            p.setColor(mFirstColor);
            canvas.drawRect(r, p);
            p.setColor(mTextColor);
            p.setTextSize(24);
            canvas.drawText(mFirstPercent, firstRightBound / 3, textHeight, p);

            // draw last part
            float lastLeftBound = firstRight + mMarkerWidth / 2;
            r.set(lastLeftBound, 0, w, h);
            p.setColor(mLastColor);
            canvas.drawRect(r, p);
            p.setColor(mTextColor);
            canvas.drawText(mLastPercent, (w + lastLeftBound) / 2, textHeight, p);

            // draw separator marker
            float separatorLeft = w * (mFirstLevel / (float) mMaxLevel) - mMarkerWidth / 2;
            separatorLeft = Math.min(Math.max(separatorLeft, 0), w - mMarkerWidth);
            r.set(separatorLeft, 0, separatorLeft + mMarkerWidth, h);
            p.setColor(mSeparatorColor);
            canvas.drawRect(r, p);
        }
    }
}
