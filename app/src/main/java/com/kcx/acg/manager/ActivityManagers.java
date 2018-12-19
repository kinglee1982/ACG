package com.kcx.acg.manager;

import android.app.Activity;

import java.util.Iterator;
import java.util.Stack;

public class ActivityManagers {
    private static Stack<Activity> mStackActivity;
    private static ActivityManagers mInstance;

    public interface ITaskCompleteListener {
        void onComplete();
    }

    private ActivityManagers() {
        mStackActivity = new Stack<Activity>();
    }

    public static ActivityManagers getInstance() {
        if (mInstance == null) {
            synchronized (ActivityManagers.class) {
                if (mInstance == null) {
                    mInstance = new ActivityManagers();
                }
            }
        }
        return mInstance;
    }

    // ----------------------------activity life method

    public void onCreate(Activity activity) {
        addActivity(activity);
    }

    public void onResume(Activity activity) {
        addActivity(activity);
    }

    /**
     * finish()和onDestroy()都要调用
     *
     * @param activity
     */
    public void onDestroy(Activity activity) {
        removeActivity(activity);
    }

    private void addActivity(Activity activity) {
        if (!mStackActivity.contains(activity)) {
            mStackActivity.add(activity);
        }
    }

    private void removeActivity(Activity activity) {
        if (activity != null) {
            mStackActivity.remove(activity);
        }
    }

    public Activity getLastActivity() {
        Activity activity = null;
        try {
            activity = mStackActivity.lastElement();
        } catch (Exception e) {
        }
        return activity;
    }

    public Activity getActivity(Class<?> className) {
        Iterator<Activity> it = mStackActivity.iterator();
        while (it.hasNext()) {
            Activity act = it.next();
            if (act.getClass() == className) {
                return act;
            }
        }
        return null;
    }



    public boolean isLastActivity(Activity activity) {
        if (activity != null) {
            return getLastActivity() == activity;
        } else {
            return false;
        }
    }

    public boolean isEmpty() {
        return mStackActivity.isEmpty();
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        Iterator<Activity> it = mStackActivity.iterator();
        while (it.hasNext()) {
            Activity act = it.next();
            if (act.getClass() == cls) {
                it.remove();

                act.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls
     * @param completeListener 结束之后的操作
     */
    public void finishActivity(Class<?> cls, ITaskCompleteListener completeListener) {

        Stack<Activity> temp = new Stack<>();
        Iterator<Activity> it = mStackActivity.iterator();
        while (it.hasNext()) {
            Activity act = it.next();
            if (act.getClass() == cls) {
                temp.add(act);
            }
        }
        removeAllTemp(temp);

        if (completeListener != null) {
            completeListener.onComplete();
        }
    }

    public void finishAllClassActivityExcept(Activity activity) {
        Stack<Activity> temp = new Stack<>();
        Iterator<Activity> it = mStackActivity.iterator();
        while (it.hasNext()) {
            Activity act = it.next();
            if (act.getClass() == activity.getClass() && act != activity) {
                //                it.remove();
                //                act.finish();
                temp.add(act);
            }
        }
        removeAllTemp(temp);
    }

    public void finishAllActivity() {
        Stack<Activity> temp = new Stack<>();
        Iterator<Activity> it = mStackActivity.iterator();
        while (it.hasNext()) {
            temp.add(it.next());
        }
        mStackActivity.removeAllElements();
        for (int i = 0; i < temp.size(); i++) {
            temp.get(i).finish();
        }
    }

    public void finishAllActivityExcept(Class<?>... clss) {
        Stack<Activity> temp = new Stack<>();
        Iterator<Activity> it = mStackActivity.iterator();
        for (int i = 0; i < clss.length; i++) {
            Class<?> cls = clss[i];
            while (it.hasNext()) {
                Activity act = it.next();
                if (act.getClass() != cls) {
                    //                it.remove();
                    //                act.finish();
                    temp.add(act);
                }
            }
        }
        removeAllTemp(temp);
    }

    public void finishAllActivityExcept(Activity activity) {
        Stack<Activity> temp = new Stack<>();
        Iterator<Activity> it = mStackActivity.iterator();
        while (it.hasNext()) {
            Activity act = it.next();
            if (act != activity) {
                it.remove();
                act.finish();
            }
        }
        removeAllTemp(temp);
    }

    private void removeAllTemp(Stack<Activity> temp) {
        mStackActivity.removeAll(temp);
        for (int i = 0; i < temp.size(); i++) {
            temp.get(i).finish();
        }
    }
}