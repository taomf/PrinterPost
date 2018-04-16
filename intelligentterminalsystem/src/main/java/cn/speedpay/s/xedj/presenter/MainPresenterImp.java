package cn.speedpay.s.xedj.presenter;

import android.content.Context;

import cn.speedpay.s.xedj.activitys.MainActivity;
import cn.speedpay.s.xedj.biz.MainModelBiz;
import cn.speedpay.s.xedj.biz.MainModelBizImp;
import cn.speedpay.s.xedj.listener.OnLoginOutListener;
import cn.speedpay.s.xedj.utils.SPUtils;

/**
 * Created by Administrator on 2016/8/18.
 */
public class MainPresenterImp implements MainPresenter,OnLoginOutListener {
    private MainActivity mMainActivity;
    private MainModelBiz modelBiz;
    public MainPresenterImp(Context context){
        this.mMainActivity = (MainActivity) context;
        modelBiz = new MainModelBizImp(this);
    }
    @Override
    public void loginOut() {
        String phoneNum = SPUtils.getInstance("phonenum").readString("phone",""); //用户手机号
        modelBiz.loginOut(phoneNum);
    }

    @Override
    public void userLoginOutStatus(String desc,boolean status) {
        mMainActivity.loginOut(desc,status);
    }
}
