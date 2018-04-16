package cn.speedpay.s.xedj.frame.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import cn.speedpay.s.xedj.utils.LogUtils;


/**
 * 说明：SupportFragment
 */

public abstract class SupportFragment extends Fragment implements OnClickListener{

    protected View fragmentRootView;
    private int resId = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setViewBefor(context);
    }

    protected abstract View inflaterView(LayoutInflater inflater,
                                         ViewGroup container, Bundle bundle);

    /**
     * 说明：设置布局文件
     * @param resId
     */
    protected final void setRootViewResID(int resId){
        this.resId = resId;
    }

    protected int getRootViewResID(){
        return resId;
    }

    /**
     * 说明：初始化数据
     */
    protected abstract void onInit(Bundle savedInstanceState,View view);

    /**
     * 说明：在绑定数据之前调用
     * @param context
     */
    protected void setViewBefor(Context context){}

    /**
     * 说明：点击事件
     */
    protected void clickView(View v,int id) {}

    @Override
    public void onClick(View v) {
        clickView(v,v.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!AnnotateViewUtils.init(this)){
            throw new RuntimeException("please use @ContentView() in your Fragment!");
        }
        fragmentRootView = inflaterView(inflater, container, savedInstanceState);
        return fragmentRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onInit(savedInstanceState,fragmentRootView);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T bind(int id) {
        return (T) fragmentRootView.findViewById(id);
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T bind(int id, boolean click) {
        T view = (T) fragmentRootView.findViewById(id);
        if (click) {
            view.setOnClickListener(this);
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LogUtils.v(this.getClass().getName(), "--->onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        LogUtils.v(this.getClass().getName(), "--->onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        LogUtils.v(this.getClass().getName(), "--->onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        LogUtils.v(this.getClass().getName(), "--->onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        LogUtils.v(this.getClass().getName(), "--->onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        LogUtils.v(this.getClass().getName(), "--->onDestroyView");
        super.onDestroyView();
    }
}

