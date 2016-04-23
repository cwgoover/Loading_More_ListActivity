package com.cwgoover.loadingmorelist;

import java.util.ArrayList;
import java.util.List;

import com.cwgoover.loadingmorelist.LoadingMoreActivity.DetailActionInfo;
import com.cwgoover.loadingmorelist.RegexParser.TimeCourse;

public class SingleFileDataSet {
    private String fileName;
    private long lastModifiedTime;
    private boolean fileDataInvalidFlag;
    private ArrayList<? extends TimeCourse> mDataSet;

    public static List<DetailActionInfo> createListData() {
        List<DetailActionInfo> list = new ArrayList<DetailActionInfo>();
        DetailActionInfo data1 = new DetailActionInfo();
        data1.setAction("bindAppTime");
        data1.setActionTime(142);
        data1.setAnimationTime(740);
        list.add(data1);
        DetailActionInfo data2 = new DetailActionInfo();
        data2.setAction("onCreate");
        data2.setActionTime(169);
        data2.setAnimationTime(740);
        list.add(data2);
        DetailActionInfo data3 = new DetailActionInfo();
        data3.setAction("onStart");
        data3.setActionTime(4);
        data3.setAnimationTime(740);
        list.add(data3);
        DetailActionInfo data4 = new DetailActionInfo();
        data4.setAction("onResume");
        data4.setActionTime(13);
        data4.setAnimationTime(740);
        list.add(data4);
        return list;
    }

    public void setFileName(String name) {
        fileName = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setLastModifiedTime(long time) {
        lastModifiedTime = time;
    }

    public long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setFileValidFlag(boolean flag) {
        fileDataInvalidFlag = flag;
    }

    public boolean getFileValidFlag() {
        return fileDataInvalidFlag;
    }

    public void setDataSet(ArrayList<? extends TimeCourse> set) {
        mDataSet = set;
    }

    public ArrayList<? extends TimeCourse> getDataSet() {
        return mDataSet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SingleFileDataSet:");
        sb.append("fileName=").append(fileName);
        sb.append(", lastModifiedTime=").append(lastModifiedTime);
        sb.append(", fileDataInvalidFlag=").append(fileDataInvalidFlag);
        sb.append("   ### mDataSet=").append(mDataSet.toString());
        return sb.toString();
    }
}
