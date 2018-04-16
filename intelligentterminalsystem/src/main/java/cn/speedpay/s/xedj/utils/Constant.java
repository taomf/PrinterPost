package cn.speedpay.s.xedj.utils;

import java.io.File;

import cn.speedpay.s.xedj.FastFrame;
import cn.speedpay.s.xedj.R;

/**
 * 说明：常用变量
 */
public final class Constant {

    /******************************* 文件路径 ****************************************/

    public static class FilePath{
        //本地文件夹名称
        public final static String APP = "xedj_its";

        public static final String tor = File.separator;
        // SD卡根目录
        public static final String ROOT = SDCardUtils.getExternalStorage() + tor;
        // 默认下载路径
        public static final String FILE_DOWNLOAD = ROOT + "Download" + tor;
    }

    /******************************* 网络连接 ****************************************/
    public static class Http{
        // 网络请求超时时间 10s
        public static final int TIMEOUT = 10000;
        // 网络请求默认key
        public static final String DEFAULT_KEY = "defalut_key";
        // 网络请求方式：post
        public static final String POST = "POST";
        // 网络请求方式：get
        public static final String GET = "GET";
        // 网络请求方式：put
        public static final String PUT = "PUT";
        // 网络请求方式：delete
        public static final String DELETE = "DELETE";
        // 网络请求方式：head
        public static final String HEAD = "HEAD";
        // 网络请求方式：patch
        public static final String PATCH = "PATCH";
        // 最大下载数量
        public static final int MAX_DOWNLOAD_COUNT = 10;
    }

    /******************************* 程序崩溃纪录 ****************************************/
    public static class Crash{
        //崩溃日志保存路径
        public final static String CRASHPATH = FilePath.ROOT + FilePath.APP + FilePath.tor + "crash" + FilePath.tor;
        // 网络请求超时时间 10s
        public final static int TIMEOUT = 5000;
    }

    /**
     * 网络相关常量
     */

    public static interface NetWork{
        public static final String  THREADID = "";

        public static final int CODE_LOGIN_ACTIVITY = 100;

        public static final String TERMINALTYPE = "android";
        public static final String APPID = "19e";

        public static final String urls = FastFrame.getApplication().getResources().getString(R.string.url);

    }

    /**
     * 请求接口
     */
    public static interface InterfaceCode{
        public static final String USERLOGIN = "userlogin";
        public static final String USERLOGOUT = "userlogout";
        public static final String INITPAGE = "initpage";
        public static final String PRINTTICKET = "printticket";
        public static final String ORDERQUERY = "orderquery";
        public static final String ORDERDETAIL = "orderdetail";
        public static final String EOPRETURNPAYORDER = "eopreturnpayorder";
        public static final String SGREFUNDORDER = "sgrefundorder";
        public static final String VERSIONUPGRADE = "versionupgrade";
    }

    /**
     * 用户权限
     */
    public static interface UserPower{
        public static final String MANAGE = "19emanage";
        public static final String MENHU = "19emenhu";
    }

    public static interface MIntent{
        public static final int TOCAPTUREACTIVITY = 900; //跳转到扫描界面的flag
        public static final int CAPTUREACTIVITYREQUESTCODE = 800;  //请求code
        public static final int CAPTUREACTIVITYRESULTCODE = 700;  //结果code


        public static final int MAINACTIVITYFLG = 600;  //mainactivity flag

    }

}

