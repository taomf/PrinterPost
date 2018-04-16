package cn.speedpay.s.xedj.application;

import android.app.Application;

import cn.speedpay.s.xedj.FastFrame;
import cn.speedpay.s.xedj.frame.http.HttpConfig;
import cn.speedpay.s.xedj.tools.SystemCrashHandler;
import cn.speedpay.s.xedj.utils.Constant;


/**
 * 说明：BaseApplication
 */
public class BaseApplication extends Application{

    //gradlew install  gradlew bintrayUpload
    private static BaseApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化FastFrame框架
        FastFrame.init(this);
        //崩溃日志处理
        SystemCrashHandler.getInstance().init();
        //网络请求配置
        HttpConfig.Builder builder = new HttpConfig.Builder();
        builder.setTrustAll(true);
        builder.setTimeout(Constant.Crash.TIMEOUT);
        builder.build().init();

        mApplication = this;
    }

    public static BaseApplication getApplication(){
        return mApplication;
    }
}
