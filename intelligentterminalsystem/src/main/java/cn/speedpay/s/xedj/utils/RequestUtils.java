package cn.speedpay.s.xedj.utils;

import android.text.TextUtils;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import cn.speedpay.s.xedj.frame.http.RequestParams;

/**
 * Created by taomf on 2016-8-17.
 * Description:网络请求的utils
 */
public class RequestUtils {

    /**
     * 说明：对参数进行URLEncoder操作
     *
     * @author liuwei
     * @since 2014-11-13 下午01:52:17
     * @version 1.0
     *
     * @param encryptList 待参与验签的集合
     * @return
     */
    public static String encodeEncryptList(Map<String, String> encryptList, String method,String power) {
        try {
            if(null == encryptList) {
                encryptList = new TreeMap<String, String>();
            }
            //添加验签的全局参数
            encryptList.putAll(getGlobalParams(method,power));
            Iterator<String> iterator = encryptList.keySet().iterator();
            StringBuffer sb = new StringBuffer();
            while(iterator.hasNext()) {
                String key = iterator.next();
                sb.append(key);
                sb.append("=");
                sb.append(encryptList.get(key));
                sb.append("&");
            }
            if(sb.toString().endsWith("&")) {
                //进行数据加密 UTF-8
                return URLEncoder.encode(sb.toString().substring(0, sb.toString().length() - 1), "UTF-8");
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * 说明：获取全局验签参数
     *
     * @author liuwei
     * @since 2014-11-13 下午02:13:02
     * @version 1.0
     *
     * @param
     * @return
     */
    public static Map<String, String> getGlobalParams(String method,String power) {
        Map<String, String> encryptList = new HashMap<String, String>();
        try {
            // 添加全局验签参数
            encryptList.put("appid", Constant.NetWork.APPID);
            encryptList.put("systemid", getSystemid(power));
            encryptList.put("reqtime", DateUtils.getNowTimeSend());
            encryptList.put("returntype", "2");
            encryptList.put("version", AndroidInfoUtils.versionName());
            encryptList.put("terminaltype", Constant.NetWork.TERMINALTYPE);
            encryptList.put("interfacecode", method);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptList;
    }

    private static String getSystemid(String power) {
        if(TextUtils.equals(power,"19emenhu")){
            return "JY102";
        }else if(TextUtils.equals(power,"19emanage")){
            return "JY103";
        }
        return  "JY103";
    }


    /**
     * MD5加密
     *
     * @param strSrc
     *            原始串
     * @param signType
     *            加密类型MD5
     * @param key
     *            密钥
     * @param charset
     *            字符集
     * @return 加密串
     */
    public static String getKeyedDigest(String strSrc, String signType,
                                        String key, String charset) {
        try {
            MessageDigest md5 = MessageDigest.getInstance(signType);
            md5.update(strSrc.getBytes(charset));
            if (null == key) {
                key = "";
            }
            String result = "";
            byte[] temp;
            temp = md5.digest(key.getBytes(charset));
            for (int i = 0; i < temp.length; i++) {
                result += Integer.toHexString(
                        (0x000000ff & temp[i]) | 0xffffff00).substring(6);
            }

            return result;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取请求参数
     * @param map 请求参数集合
     * @param interfacecode 接口
     * @param power 权限
     * @return
     */
    public static RequestParams getRequestParmas(Map<String,String> map,String interfacecode,String power){
        RequestParams params = new RequestParams();

        //进行数字签名
        String encodeString = encodeEncryptList(map,interfacecode,power);
        String sign = getKeyedDigest(encodeString, "MD5", power, "UTF-8");

        map.putAll(getGlobalParams(interfacecode,power));

        params = mapToParams(map,params);
        params.put("sign", sign);

        String jsonParmas = params.toString();
        params.setApplicationJson(jsonParmas);

        return params;
    }

    public static RequestParams mapAdParams(Map<String,String> map,RequestParams params){
        Iterator<String> iterator = map.keySet().iterator();
        while(iterator.hasNext()) {
            String key = iterator.next();
            params.put(key,map.get(key));
        }
        return params;
    }

    public static RequestParams mapToParams(Map<String,String> map,RequestParams params){
        if(params == null){
            params = new RequestParams();
        }

        Iterator<String> iterator = map.keySet().iterator();
        while(iterator.hasNext()) {
            String key = iterator.next();
            params.put(key,map.get(key));
        }
        return params;
    }

    public static final String INTERFACECODE = "&interfacecode=";
    public static final String TRADEID = "&tradeId=";

    public static String getUrl(String interace,String threadID){
       return Constant.NetWork.urls+INTERFACECODE+interace+TRADEID+threadID;
    }

    public static String getSendStatus(String string){
        if(TextUtils.equals(string,"初始化")){
            return "";
        }else if(TextUtils.equals(string,"派单中")){
            return "25";
        }else if (TextUtils.equals(string,"派单失败")){
            return "26";
        }else if(TextUtils.equals(string,"派单成功")){
            return "27";
        }
        return  "";
    }

    public static String getGoodStatus(String string){
        if(TextUtils.equals(string,"初始化")){
            return "28";
        }else if(TextUtils.equals(string,"备货中")){
            return "29";
        }else if (TextUtils.equals(string,"备货失败")){
            return "31";
        }else if(TextUtils.equals(string,"备货异常")){
            return "32";
        }else if(TextUtils.equals(string,"备货成功")){
            return "30";
        }
        return  "";
    }

    public static String sendMethod(String string){
        if(TextUtils.equals(string,"初始化")){
            return "";
        }else if(TextUtils.equals(string,"自提")){
            return "1";
        }else if (TextUtils.equals(string,"小e配送")){
            return "2";
        }
        return  "";
    }

    public static String getRejectStatus(String string){
        if(TextUtils.equals(string,"初始化")){
            return "";
        }else if(TextUtils.equals(string,"拒收成功")){
            return "1";
        }else if (TextUtils.equals(string,"待拒收")){
            return "0";
        }else if(TextUtils.equals(string,"拒收失败")){
            return "2";
        }
        return  "";
    }
}
