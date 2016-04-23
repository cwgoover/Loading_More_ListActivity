package com.cwgoover.loadingmorelist;

import java.util.HashMap;


public abstract class RegexParser {

    public static LaunchCourse creatLaunchCourse() {
        LaunchCourse course = new LaunchCourse();
        course.setClassName("Gallery");
        course.setPackageName("com.tct.gallery3d");
        course.setStartTime("18:55:34.455");

        course.setKeyValue("bindAppTime", 28);
        course.setKeyValue("onCreate", 516);
        course.setKeyValue("onStart", 5);
        course.setKeyValue("onResume", 38);
        course.setKeyValue("responseTime", 190);
        course.setKeyValue("animationTime", 1100);
        course.setKeyValue("totalTime", 1290);
        return course;
    }

    protected static class TimeCourse {
        private String mPkg;
        private String mCls;
        private String mStartTimeStamp;
        private final HashMap<String, Long> mMethods;

        public TimeCourse() {
            mMethods = new HashMap<String, Long>();
        }

        public long getKeyValue(String key) {
            if (!mMethods.containsKey(key)) {
                return -1;
            }
            return mMethods.get(key);
        }

        public void setKeyValue(String key, long value) {
            mMethods.put(key, value);
        }

        public String getPackageName () {
            return mPkg;
        }

        public void setPackageName (String packagename) {
            mPkg = packagename;
        }

        public String getClassName () {
            return mCls;
        }

        public void setClassName (String classname) {
            mCls = classname;
        }

        public String getStartTime () {
            return mStartTimeStamp;
        }

        public void setStartTime (String startTime) {
            mStartTimeStamp = startTime;
        }

        @Override
        public String toString() {
            return toStringBuilder().toString();
        }

        protected StringBuilder toStringBuilder() {
            final StringBuilder sb = new StringBuilder(getClass().getSimpleName()).append(":");
            sb.append("APP: ").append(mCls);
            sb.append("(").append(mPkg);
            sb.append(") Time course: [[");
            sb.append(mStartTimeStamp + "]] ");
            sb.append(mMethods.toString());
            sb.append("; ");
            return sb;
        }
    }

    public static class LaunchCourse extends TimeCourse {
        private boolean mFirstBoot;

        public LaunchCourse() {
            super();
        }

        public boolean getFirstBootFlag () {
            return mFirstBoot;
        }

        public void setFirstBootFlag (boolean  firstboot) {
            mFirstBoot = firstboot;
        }

        @Override
        public StringBuilder toStringBuilder() {
            StringBuilder sb = super.toStringBuilder();
            sb.insert(sb.length() - 1, "[first boot: " + mFirstBoot + "]");
            return sb;
        }
    }
}
