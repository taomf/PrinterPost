package cn.speedpay.s.xedj.biz;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import cn.speedpay.s.xedj.frame.http.RequestParams;
import cn.speedpay.s.xedj.frame.http.callback.BaseHttpCallBack;
import cn.speedpay.s.xedj.listener.OnOrderDetailListener;
import cn.speedpay.s.xedj.utils.Constant;
import cn.speedpay.s.xedj.utils.HttpUtils;
import cn.speedpay.s.xedj.utils.RequestUtils;

/**
 * Created by Administrator on 2016/8/20.
 */
public class MarketOrderDetailBizIml implements MarketOrderDetailBiz {
    private OnOrderDetailListener listener;
    public MarketOrderDetailBizIml(OnOrderDetailListener listener) {
        this.listener = listener;
    }

    /**
     * 订单详情网络请求
     * @param orderid 订单号
     */
    @Override
    public void orderDetailRequest(String orderid) {
        Map<String,String> map = new TreeMap<>();
        map.put("orderid", orderid);
        RequestParams params = RequestUtils.getRequestParmas(map, Constant.InterfaceCode.ORDERDETAIL, Constant.UserPower.MANAGE);

        String jsonParmas = params.toString();
        params.setApplicationJson(jsonParmas);

        HttpUtils.post(RequestUtils.getUrl(Constant.InterfaceCode.ORDERDETAIL, orderid), params, new BaseHttpCallBack() {
            @Override
            public void onSuccess(Object result) {
                Log.i("taomf+++++", result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    Log.i("jsonObject", jsonObject.toString());
                    if(jsonObject.getString("resultcode").equals("0")){
                        listener.orderDeatilData(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                listener.errMsg(msg);

                Log.i("taomf+++++", errorCode + "====" + msg);
            }
        });
    }

    @Override
    public void printTicket(String orderid) {
        Map<String, String> map = new TreeMap<>();
        map.put("orderid", orderid);
        RequestParams params = RequestUtils.getRequestParmas(map, Constant.InterfaceCode.PRINTTICKET, Constant.UserPower.MANAGE);

        String jsonParmas = params.toString();
        params.setApplicationJson(jsonParmas);

        HttpUtils.post(RequestUtils.getUrl(Constant.InterfaceCode.ORDERDETAIL, orderid), params, new BaseHttpCallBack() {
            @Override
            public void onSuccess(Object result) {
                Log.i("taomf+++++", result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    Log.i("jsonObject", jsonObject.toString());
                    if(TextUtils.equals(jsonObject.getString("resultcode"), "0")){
                        listener.printTicket(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                listener.errMsg(msg);
                Log.i("taomf+++++", errorCode + "====" + msg);
            }
        });
    }
}
