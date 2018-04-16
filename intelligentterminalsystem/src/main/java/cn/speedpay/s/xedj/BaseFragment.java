package cn.speedpay.s.xedj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.speedpay.s.xedj.frame.http.HttpTaskKey;
import cn.speedpay.s.xedj.frame.ui.SupportFragment;
import cn.speedpay.s.xedj.frame.ui.ToastUtil;

/**
 * 说明：Fragment基类(V4)
 */
public abstract class BaseFragment extends SupportFragment implements HttpTaskKey {

    private FragmentActivity mActivity;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mActivity = getActivity();
        return inflater.inflate(getRootViewResID(),null);
    }

    @Override
    public String getHttpTaskKey() {
        return "key"+hashCode();
    }

    /***************************************************************************************/

    public void shortToast(int res){
        ToastUtil.get().shortToast(res);
    }
    public void shortToast(String res){
        ToastUtil.get().shortToast(res);
    }
    public void longToast(String res){
        ToastUtil.get().longToast(res);
    }
    public void longToast(int res){
        ToastUtil.get().longToast(res);
    }
    public void cancelToast(){
        ToastUtil.get().cancelToast();
    }

    /***************************************************************************************/

    /***************************************************************************************/

    public void skipActivity(Class<?> cls) {
        skipActivity(cls);
        mActivity.finish();
    }

    public void skipActivity(Intent intent) {
        skipActivity(intent);
        mActivity.finish();
    }

    public void skipActivity(Class<?> cls, Bundle bundle) {
        skipActivity(cls, bundle);
        mActivity.finish();
    }

    public void showActivity(Class<?> cls) {
        Intent intent = new Intent(mActivity,cls);
        mActivity.startActivity(intent);
    }

    public void showActivity(Intent intent) {
        mActivity.startActivity(intent);
    }

    public void showActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(mActivity,cls);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    /***************************************************************************************/

    /***************************************************************************************/

    public FragmentManager getSupportFragmentManager() {
        if (mActivity != null){
            return mActivity.getSupportFragmentManager();
        }else {
            return null;
        }
    }

    /***************************************************************************************/
}
