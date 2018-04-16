package cn.speedpay.s.xedj.tools;


import java.io.File;

import cn.speedpay.s.xedj.R;
import cn.speedpay.s.xedj.frame.ui.ToastUtil;
import cn.speedpay.s.xedj.utils.AndroidInfoUtils;
import cn.speedpay.s.xedj.utils.Constant;
import cn.speedpay.s.xedj.utils.CrashHandler;
import cn.speedpay.s.xedj.utils.DateUtils;
import cn.speedpay.s.xedj.utils.UIUtils;

/**
 * 说明：崩溃日志处理
 */
public class SystemCrashHandler extends CrashHandler {

    private final static SystemCrashHandler crashHandler = new SystemCrashHandler();

    public static SystemCrashHandler getInstance(){
        return crashHandler;
    }

    @Override
    public void upCrashLog(File file, String error) {

    }

    @Override
    public String setFileName() {
        return AndroidInfoUtils.getAndroidId()+"_crash_" + DateUtils.getNowTime(DateUtils.FORMAT_YYYY_MM_DD_HH_MM_SS_4)+".txt";
    }

    @Override
    public void showCrashTip() {
        super.showCrashTip();
        ToastUtil.get().shortToast(UIUtils.getString(R.string.crash_tip));
    }

    @Override
    public String setCrashFilePath() {
        return Constant.Crash.CRASHPATH;
    }

    @Override
    public boolean isCleanHistory() {
        return true;
    }
}
