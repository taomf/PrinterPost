package cn.speedpay.s.xedj.biz;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import cn.speedpay.s.xedj.bean.UserBean;
import cn.speedpay.s.xedj.frame.http.RequestParams;
import cn.speedpay.s.xedj.frame.http.callback.BaseHttpCallBack;
import cn.speedpay.s.xedj.listener.OnLoginListener;
import cn.speedpay.s.xedj.utils.AndroidInfoUtils;
import cn.speedpay.s.xedj.utils.Constant;
import cn.speedpay.s.xedj.utils.HttpUtils;
import cn.speedpay.s.xedj.utils.MD5;
import cn.speedpay.s.xedj.utils.RequestUtils;
import cn.speedpay.s.xedj.utils.SPUtils;

/**
 * Created by jesse on 15-6-24.
 */
public class LoginModelImpl implements LoginModelBiz {
    private OnLoginListener listener;

    public LoginModelImpl(OnLoginListener listener){
        this.listener = listener;
    }

    @Override
    public void login(UserBean login) {
        final String account,password;
        account = login.getUsername();
        password = login.getPassword();
        String md5Pass = MD5.getMD5(password);
//      RequestParams loginParmas = new RequestParams();

        Map<String,String> map = new TreeMap<>();
        map.put("userloginid",account);
        map.put("userpass",md5Pass);
        map.put("androidversion", AndroidInfoUtils.getVersionString());
        map.put("clientversion",AndroidInfoUtils.versionName());
        map.put("usertype","1");
        map.put("logintype", "0");
        RequestParams loginParmas = RequestUtils.getRequestParmas(map, Constant.InterfaceCode.USERLOGIN, Constant.UserPower.MENHU);

        HttpUtils.post(RequestUtils.getUrl("userlogin", Constant.NetWork.THREADID), loginParmas, new BaseHttpCallBack() {
            @Override
            public void onSuccess(Object result) {
                boolean ff = false;
                Log.i("taomf++++", result.toString());
                try {
                    JSONObject obj = new JSONObject(result.toString());
                    if (TextUtils.equals(obj.getString("resultcode"), "0")) { //登录成功
                        Log.i("taomf++++", obj.getString("resultcode"));
                        ff = true;
                        SPUtils.getInstance("login").write("loginSuccess", true);
                        SPUtils.getInstance("phonenum").write("phone", account); //保存手机号
                    }
                    String desc = obj.getString("resultdesc");
                    listener.loginStatus(ff, desc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                listener.loginStatus(false, msg);
                Log.i("taomf++++", errorCode + "====" + msg);

            }
        });
    }
}
