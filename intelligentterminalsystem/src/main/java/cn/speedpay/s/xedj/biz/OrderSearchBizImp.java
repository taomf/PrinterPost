package cn.speedpay.s.xedj.biz;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import cn.speedpay.s.xedj.frame.http.RequestParams;
import cn.speedpay.s.xedj.frame.http.callback.BaseHttpCallBack;
import cn.speedpay.s.xedj.listener.OrderSearchListener;
import cn.speedpay.s.xedj.utils.Constant;
import cn.speedpay.s.xedj.utils.HttpUtils;
import cn.speedpay.s.xedj.utils.RequestUtils;
import cn.speedpay.s.xedj.utils.SPUtils;

/**
 * Created by Administrator on 2016/8/18.
 */
public class OrderSearchBizImp implements OrderSearchBiz {
    private OrderSearchListener listener;

    public OrderSearchBizImp(OrderSearchListener listener) {
        this.listener = listener;
    }

    /**
     * 根据用户订单编号进行查询
     *
     * @param string
     */
    @Override
    public void orderSearch(String string) {
        Map<String, String> map = new TreeMap<>();
        map.put("userloginid", SPUtils.getInstance("phonenum").readString("phone"));

        RequestParams params = RequestUtils.getRequestParmas(map, Constant.InterfaceCode.ORDERQUERY, Constant.UserPower.MANAGE);
        params.put("orderid",string);

        String jsonParmas = params.toString();
        params.setApplicationJson(jsonParmas);

        HttpUtils.post(RequestUtils.getUrl(Constant.InterfaceCode.ORDERQUERY, "ZI141124162059XX0002"), params, new BaseHttpCallBack() {
            @Override
            public void onSuccess(Object result) {
                Log.i("taomf+++++", result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    Log.i("jsonObject", jsonObject.toString());

                    if (jsonObject.getString("resultcode").equals("0") && jsonObject.getJSONArray("orderlist").length() != 0) {
                        listener.queryResult(jsonObject);
                    }else if (jsonObject.getJSONArray("orderlist").length() == 0){
                        listener.queryResult(jsonObject);
                        listener.showMesg(jsonObject.getString("resultdesc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                listener.showMesg(msg);
                Log.i("taomf+++++", errorCode + "====" + msg);
            }
        });
    }

    /**
     * 用户账号查询
     *
     * @param ss 用户账号
     */
    @Override
    public void orderAccSearch(String ss) {
        Map<String, String> map = new TreeMap<>();
        map.put("userloginid", SPUtils.getInstance("phonenum").readString("phone"));

        RequestParams params = RequestUtils.getRequestParmas(map, Constant.InterfaceCode.ORDERQUERY, Constant.UserPower.MANAGE);
        params.put("orderaccount",ss);

        String jsonParmas = params.toString();
        params.setApplicationJson(jsonParmas);

        HttpUtils.post(RequestUtils.getUrl(Constant.InterfaceCode.ORDERQUERY, Constant.NetWork.THREADID), params, new BaseHttpCallBack() {
            @Override
            public void onSuccess(Object result) {
                Log.i("taomf+++++", result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    Log.i("jsonObject", jsonObject.toString());

                    if (jsonObject.getString("resultcode").equals("0") && jsonObject.getJSONArray("orderlist").length() != 0) {
                        listener.queryResult(jsonObject);
                    }else if(jsonObject.getJSONArray("orderlist").length() == 0){
                        listener.queryResult(jsonObject);
                        listener.showMesg(jsonObject.getString("resultdesc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                listener.showMesg(msg);
                Log.i("taomf+++++", errorCode + "====" + msg);
            }
        });
    }

    /**
     * 多条件查询
     *
     * @param map
     */
    @Override
    public void orderDetailSearch(Map<String, String> map) {
        Map<String,String> detailMap = new TreeMap<>();
        detailMap.put("userloginid",SPUtils.getInstance("phonenum").readString("phone"));

        RequestParams params = RequestUtils.getRequestParmas(detailMap, Constant.InterfaceCode.ORDERQUERY, Constant.UserPower.MANAGE);
        params.put("orderaccount", map.get("orderaccount"));
        params.put("readystatus", map.get("readystatus"));
        params.put("assignstatus", map.get("assignstatus"));
        params.put("sendtype", map.get("sendtype"));
        params.put("rejectstatus", map.get("rejectstatus"));

        String jsonParmas = params.toString();
        params.setApplicationJson(jsonParmas);

        HttpUtils.post(RequestUtils.getUrl(Constant.InterfaceCode.ORDERQUERY, Constant.NetWork.THREADID), params, new BaseHttpCallBack() {
            @Override
            public void onSuccess(Object result) {
                Log.i("taomf+++++", result.toString());
                try {

                    JSONObject jsonObject = new JSONObject(result.toString());
                    Log.i("jsonObject", jsonObject.toString());
                    if(TextUtils.equals("0",jsonObject.getString("resultcode")) && jsonObject.getJSONArray("orderlist").length() != 0){
                            listener.queryResult(jsonObject);
                    }else{
                        listener.showMesg(jsonObject.getString("resultdesc"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                listener.showMesg(msg);
                Log.i("taomf+++++", errorCode + "====" + msg);
            }
        });

    }
}
