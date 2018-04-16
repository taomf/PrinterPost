package cn.speedpay.s.xedj.biz;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import cn.speedpay.s.xedj.frame.http.RequestParams;
import cn.speedpay.s.xedj.frame.http.callback.BaseHttpCallBack;
import cn.speedpay.s.xedj.listener.OnLoginOutListener;
import cn.speedpay.s.xedj.utils.Constant;
import cn.speedpay.s.xedj.utils.HttpUtils;
import cn.speedpay.s.xedj.utils.RequestUtils;
import cn.speedpay.s.xedj.utils.SPUtils;

/**
 * Created by Administrator on 2016/8/18.
 */
public class MainModelBizImp implements MainModelBiz {
    private OnLoginOutListener listener;
    public MainModelBizImp(OnLoginOutListener listener){
        this.listener = listener;
    }
    @Override
    public void loginOut(String num) {
        Map<String,String> map = new TreeMap<>();
        map.put("userloginid",num);
        map.put("usertype","1");
        RequestParams params = RequestUtils.getRequestParmas(map, Constant.InterfaceCode.USERLOGOUT, Constant.UserPower.MENHU);
        HttpUtils.post(RequestUtils.getUrl(Constant.InterfaceCode.USERLOGOUT, Constant.NetWork.THREADID), params, new BaseHttpCallBack() {
            @Override
            public void onSuccess(Object result) {
                Log.i("taomf+++", result.toString());

                boolean ff = false;
                try {
                    JSONObject object = new JSONObject(result.toString());
                    String desc = object.getString("resultdesc");  //处理结果
                    if (TextUtils.equals("0", object.getString("resultcode"))) { //请求成功
                        //清除用户登录痕迹
                        SPUtils.getInstance("phonenum").clear();
                        SPUtils.getInstance("login").clear();
                        ff = true;
                    }
                    listener.userLoginOutStatus(desc, ff);
                } catch (JSONException e) {
                    Log.i("taomf+++", e.toString());

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                Log.i("taomf+++", msg);
                listener.userLoginOutStatus(msg, false);

            }
        });
    }
}
