package cn.speedpay.s.xedj.biz;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import cn.speedpay.s.xedj.frame.http.RequestParams;
import cn.speedpay.s.xedj.frame.http.callback.BaseHttpCallBack;
import cn.speedpay.s.xedj.listener.OnRejectOrderDetailListener;
import cn.speedpay.s.xedj.utils.Constant;
import cn.speedpay.s.xedj.utils.HttpUtils;
import cn.speedpay.s.xedj.utils.RequestUtils;

/**
 * Created by taomf on 2016/8/20.
 */
public class RejectOrderDetailBizIml implements RejectOrderDetailBiz {
    private OnRejectOrderDetailListener listener;
    private JSONObject jsObject;
    public RejectOrderDetailBizIml(OnRejectOrderDetailListener listener) {
        this.listener = listener;
    }

    /**
     * 订单详情查询
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
                    jsObject = jsonObject;
                    if(jsonObject.getString("resultcode").equals("0")){
                        listener.orderDeatilData(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                listener.rejectErrInfo(msg);

                Log.i("taomf+++++", errorCode + "====" + msg);
            }
        });
    }

    /**
     * 拒收订单
     */
    @Override
    public void rejectReceiverOrder() {
        Log.i("jsonObjectReject", jsObject.toString());

        if(jsObject != null){
            try {
                Map<String, String> rejectMap = new TreeMap<>();
                rejectMap.put("orderid", jsObject.getString("orderid"));
                rejectMap.put("amount", jsObject.getString("payprice"));
                rejectMap.put("approvalid", "1");

                rejectMap.put("reason", jsObject.getString("rejectreason")+"00");

                rejectMap.put("optuser", jsObject.getString("orderphone"));

                rejectMap.put("eoporderid", jsObject.getString("eoporderid"));// 支付流水号

                RequestParams rejectParmas = RequestUtils.getRequestParmas(rejectMap, Constant.InterfaceCode.EOPRETURNPAYORDER, Constant.UserPower.MANAGE);
                rejectParmas.put("eoporderid", jsObject.getString("eoporderid"));

                String jsonParmas = rejectParmas.toString();
                rejectParmas.setApplicationJson(jsonParmas);
                HttpUtils.post(RequestUtils.getUrl(Constant.InterfaceCode.EOPRETURNPAYORDER, jsObject.getString("orderid")), rejectParmas, new BaseHttpCallBack() {
                    @Override
                    public void onSuccess(Object result) {
                        Log.i("jsonObjectReject", result.toString());
                        try {
                            JSONObject object = new JSONObject(result.toString());

                            if (object.getString("resultcode").equals("0")) {
                                listener.rejectInfo(object.getString("resultdesc"));
                            } else {
                                listener.rejectErrInfo(object.getString("resultdesc"));
                            }
                        } catch (JSONException e) {
                            listener.rejectErrInfo(e.toString());
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int errorCode, String msg) {
                        listener.rejectErrInfo(msg);

                        Log.i("jsonObjectReject", errorCode + "====" + msg);
                    }
                });

            } catch (JSONException e) {
                listener.rejectErrInfo(e.toString());
                Log.i("jsonObjectReject", e.toString());
                e.printStackTrace();
            }
        }
    }
}
