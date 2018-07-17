package com.whmaster.tl.whmaster.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2018/1/29.
 */

public class AtyContainerUtils {
    private AtyContainerUtils() {
    }

    private static AtyContainerUtils instance = new AtyContainerUtils();
    private static List<Activity> activityStack = new ArrayList<Activity>();
    private static List<Activity> LibraryStack = new ArrayList<Activity>();

    public static AtyContainerUtils getInstance() {
        return instance;
    }
    public void addLibraryActivity(Activity aty) {
        LibraryStack.add(aty);
    }
    public void addActivity(Activity aty) {
        activityStack.add(aty);
    }

    public void removeActivity(Activity aty) {
        activityStack.remove(aty);
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }
    public void finishLibraryActivity() {
        for (int i = 0, size = LibraryStack.size(); i < size; i++) {
            if (null != LibraryStack.get(i)) {
                LibraryStack.get(i).finish();
            }
        }
        LibraryStack.clear();
    }
}
