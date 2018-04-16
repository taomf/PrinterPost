package cn.speedpay.s.xedj.frame.ui;

import android.app.Activity;

import java.util.Stack;

/**
 * 说明：应用程序Activity管理类：用于Activity管理和应用程序退出
 */

public class ActivityStack {

    private static Stack<I_Activity> activities;
    private static final ActivityStack instance = new ActivityStack();

    private ActivityStack(){}

    public static ActivityStack create(){
        return instance;
    }

    /**
     * 说明：获取当前Activity栈中数量
     * @return
     */
    public int getCount(){
        int count = 0;
        if (activities != null) {
            count = activities.size();
        }
        return count;
    }

    /**
     * 说明：将activity添加到栈中
     * @param activity
     */
    public void addActivity(I_Activity activity){
        if (activities == null) {
            activities = new Stack<I_Activity>();
        }
        activities.add(activity);
    }

    /**
     * 说明：获取栈顶的Activity
     * @return
     */
    public Activity topActivity(){
        if (activities == null || activities.isEmpty()) {
            throw new NullPointerException(
                    "Activity stack is Null,your Activity must extend FlyActivity");
        }
        return (Activity) activities.lastElement();
    }

    /**
     * 说明：查找activity,没有则返回null
     * @param cls
     * @return
     */
    public Activity findActivity(Class<?> cls){
        I_Activity activity = null;
        for (I_Activity aty : activities) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return (Activity) activity;
    }

    public void finishActivity(){
        if (activities != null && !activities.isEmpty()) {
            I_Activity activity = activities.lastElement();
            finishActivity((I_Activity)activity);
        }
    }

    /**
     * 说明：结束指定activity
     * @param activity
     */
    public void finishActivity(I_Activity activity){
        if (activity != null) {
            activities.remove(activity);
            activity = null;
        }
    }

    /**
     * 说明：结束指定activity
     * @param cls
     */
    public void finishActivity(Class<?> cls){
        if (activities != null && !activities.isEmpty()) {
            for (I_Activity aty : activities) {
                if (aty.getClass().equals(cls)) {
                    finishActivity((I_Activity)aty);
                }
            }
        }
    }

    /**
     * 说明：清除cls外其他所有activity
     * @param cls
     */
    public void finishOtherActivity(Class<?> cls){
        if (activities != null && !activities.isEmpty()) {
            for (I_Activity aty : activities) {
                if (!aty.getClass().equals(cls)) {
                    finishActivity((I_Activity)aty);
                }
            }
        }
    }

    /**
     * 说明：结束所有activity
     */
    public void finishAllActivity(){
        if (activities != null && !activities.isEmpty()) {
            for(int i = 0,size = activities.size();i<size;i++){
                if (null != activities.get(i)) {
                    ((Activity)activities.get(i)).finish();
                }
            }
            activities.clear();
        }
    }

    /**
     * 说明：退出应用
     */
    public void AppExit(){
        try {
            finishAllActivity();
            Runtime.getRuntime().exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            Runtime.getRuntime().exit(-1);
        }
    }
}

