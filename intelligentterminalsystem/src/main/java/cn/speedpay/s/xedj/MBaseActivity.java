package cn.speedpay.s.xedj;

import android.app.Activity;
import android.os.Bundle;

import com.umeng.update.UmengUpdateAgent;

import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;
import cn.speedpay.s.xedj.utils.AndroidInfoUtils;

/**
 * Created by taomf on 2016-8-16.
 * Description: Activity基类
 */
public abstract class MBaseActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);

        setContentView(getContentView());
        UpdateKey.API_TOKEN = "b2cf4fb1b03729c6df5e707753bd479b";
        UpdateKey.APP_ID = "57bf2554ca87a837ef002724";
        //下载方式:
        //UpdateKey.DialogOrNotification=UpdateKey.WITH_DIALOG;通过Dialog来进行下载
        //UpdateKey.DialogOrNotification=UpdateKey.WITH_NOTIFITION;通过通知栏来进行下载(默认)
        UpdateFunGO.init(this);

        initParmas();
    }

    /**
     * 布局文件
     * @return
     */
    public abstract int getContentView();
    /**
     * 初始化参数
     */
    protected abstract void initParmas();
}
