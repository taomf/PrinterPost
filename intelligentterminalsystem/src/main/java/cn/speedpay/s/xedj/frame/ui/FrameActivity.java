package cn.speedpay.s.xedj.frame.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 说明：FrameActivity为Activity基类
 */

public abstract class FrameActivity extends AppCompatActivity implements OnClickListener,
        I_Broadcast, I_Activity, I_SkipActivity,I_Service{

    protected SupportFragment currentSupportFragment;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setViewBefor();
        if (!AnnotateViewUtils.init(this)){
            throw new RuntimeException("please use @ContentView() in your Activity!");
        }
        if (getIntent() != null){
            getIntentData(getIntent());
        }
        onInitCreate(bundle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onInitStart();
        registerBroadcast();
        registerService();
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T bind(int id) {
        return (T) findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T bind(int id, boolean click) {
        T view = (T) findViewById(id);
        if (click) {
            view.setOnClickListener(this);
        }
        return view;
    }

    @Override
    protected void onDestroy() {
        unRegisterBroadcast();
        unRegisterService();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        clickView(v,v.getId());
    }

    @Override
    public abstract void clickView(View v,int id);

    @Override
    public void registerBroadcast() {}

    @Override
    public void unRegisterBroadcast() {}

    @Override
    public void registerService() {}

    @Override
    public void unRegisterService() {}

    /**
     * 说明:设置界面之前调用
     * @return
     */
    protected void setViewBefor(){}

    /**
     * 说明：用Fragment替换视图
     * @param srcView 被替换视图
     * @param targetFragment 用来替换的Fragment
     */
    public void changeFragment(int srcView, SupportFragment targetFragment) {
        if (targetFragment.equals(currentSupportFragment)) {
            return;
        }
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(srcView, targetFragment, targetFragment.getClass()
                    .getName());
        }
        if (targetFragment.isHidden()) {
            transaction.show(targetFragment);
        }
        if (currentSupportFragment != null
                && currentSupportFragment.isVisible()) {
            transaction.hide(currentSupportFragment);
        }
        currentSupportFragment = targetFragment;
        transaction.commit();
    }
}

