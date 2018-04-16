package cn.speedpay.s.xedj.frame.http.callback;

import okhttp3.Headers;
import okhttp3.Response;

/**
 * 类名称：BaseHttpCallBack
 * 类描述：HttpCallBack基类
 * 创建人：Clark
 * 创建时间：2016/8/5 10:22
 * 修改人：Clark
 * 修改时间：2016/8/5 10:22
 * 修改备注：
 * @version v1.0
 */
public abstract class BaseHttpCallBack<T> {

    //响应为null
    public static final int ERROR_RESPONSE_NULL = 1001;
    //数据转换异常
    public static final int ERROR_RESPONSE_JSON_EXCEPTION = 1002;
    //未知错误
    public static final int ERROR_RESPONSE_UNKNOWN = 1003;
    //连接超时
    public static final int ERROR_RESPONSE_TIMEOUT = 1004;
    //无网络
    public static final int ERROR_RESPONSE_NO_NETWORK = 1005;

    protected Class<T> clazz;
    protected Headers headers;

    public BaseHttpCallBack(){
    }

    /**
     * 说明：访问网络前开始执行
     */
    public void onStart(){}

    /**
     * 说明：访问网络后开始执行
     */
    public void onFinish(){}

    /**
     * 说明：访问网络成功后执行
     * @param result
     */
    public abstract void onSuccess(T result);

    /**
     * 说明：访问网络成功后执行
     * @param result
     */
    public void onSuccess(Headers headers,T result){}

    /**
     * 说明：访问网络失败后执行
     * @param errorCode
     * @param msg
     */
    public abstract void onFailure(int errorCode, String msg);

    /**
     * 说明：上传文件进度
     * @param progress
     * @param networkSpeed 网速
     * @param done
     */
    public void onProgress(int progress, long networkSpeed, boolean done){}

    public Class<T> getClazz(){
        return clazz;
    }

    public Headers getHeaders(){
        return headers;
    }

    public void setResponseHeaders(Headers headers){
        this.headers = headers;
    }

    public void onResponse(Response httpResponse, String response, Headers headers) {
    }

    public void onResponse(String response, Headers headers) {
    }
}