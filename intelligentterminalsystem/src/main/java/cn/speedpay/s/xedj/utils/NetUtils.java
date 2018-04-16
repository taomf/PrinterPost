package cn.speedpay.s.xedj.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import cn.speedpay.s.xedj.FastFrame;

/**
 * 说明：网络工具类
 */
public final class NetUtils {

    /******************************* 网络类型 ****************************************/

    public static class NetWorkType{
        // 网络类型
        public static final int NETTYPE_NONET = 0;
        public static final int NETTYPE_WIFI = 1;
        public static final int NETTYPE_2G = 2;
        public static final int NETTYPE_3G = 3;
        public static final int NETTYPE_4G = 4;
    }

    private NetUtils(){}

    /**
     * 说明：获取手机网络状态是否可用
     *
     * @return 返回网络状态【true:网络联通】【false:网络断开】
     */
    public static boolean isNetConnected() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) FastFrame.getApplication()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo network = connectivityManager.getActiveNetworkInfo();
            if (connectivityManager != null) {
                if (network != null && network.isConnected()) {
                    if (network.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 说明：获取当前网络类型
     *      字段常量[NetWorkType.NETTYPE_WIFI]
     * @return 0：没有网络 1：WIFI网络 2：2G网络 3：3G网络 4:4G网络
     *         int NETTYPE_NONET = 0;
     *         int NETTYPE_WIFI = 1;
     *         int NETTYPE_2G = 2;
     *         int NETTYPE_3G = 3;
     *         int NETTYPE_4G = 4;
     */
    public static int getNetWorkType() {
        int strNetworkType = 0;
        NetworkInfo networkInfo = ((ConnectivityManager) FastFrame.getApplication().getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = NetWorkType.NETTYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        strNetworkType = NetWorkType.NETTYPE_2G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        strNetworkType = NetWorkType.NETTYPE_3G;
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        strNetworkType = NetWorkType.NETTYPE_4G;
                        break;
                    default:
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA")
                                || _strSubTypeName.equalsIgnoreCase("WCDMA")
                                || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = NetWorkType.NETTYPE_3G;
                        } else {
                            strNetworkType = NetWorkType.NETTYPE_NONET;
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }

    /**
     * 说明：判断当前网络是否是wifi
     * @return
     */
    public static boolean isWifi(){
        return getNetWorkType() == NetWorkType.NETTYPE_WIFI;
    }

    /**
     * 说明：判断当前网络是否是2G
     * @return
     */
    public static boolean is2G(){
        return getNetWorkType() == NetWorkType.NETTYPE_2G;
    }

    /**
     * 说明：判断当前网络是否是3G
     * @return
     */
    public static boolean is3G(){
        return getNetWorkType() == NetWorkType.NETTYPE_3G;
    }

    /**
     * 说明：判断当前网络是否是4G
     * @return
     */
    public static boolean is4G(){
        return getNetWorkType() == NetWorkType.NETTYPE_4G;
    }

    /**
     * 说明：判断当前网络是否是3G或是4G
     * @return
     */
    public static boolean is3Gor4G(){
        return is3G() || is4G();
    }
}
