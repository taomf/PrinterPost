package cn.speedpay.s.xedj.biz;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import cn.speedpay.s.xedj.frame.http.RequestParams;
import cn.speedpay.s.xedj.frame.http.callback.BaseHttpCallBack;
import cn.speedpay.s.xedj.listener.OnFlashOrderDetailListener;
import cn.speedpay.s.xedj.utils.Constant;
import cn.speedpay.s.xedj.utils.HttpUtils;
import cn.speedpay.s.xedj.utils.RequestUtils;

/**
 * Created by Administrator on 2016/8/20.
 */
public class FlashOrderDetailBizIml implements FlashOrderDetailBiz {
    private OnFlashOrderDetailListener listener;
    private JSONObject jsObject;

    public FlashOrderDetailBizIml(OnFlashOrderDetailListener listener) {
        this.listener = listener;
    }

    /**
     * 闪购 订单详情网络请求
     *
     * @param orderid 订单号
     */
    @Override
    public void orderDetailRequest(String orderid) {
        Map<String, String> map = new TreeMap<>();
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
                listener.errMsg(msg);

                Log.i("taomf+++++", errorCode + "====" + msg);
            }
        });
    }

    /**
     * /**
     * 退款订单
     */
    @Override
    public void tuiKuanOrder(String goodsids,String status) {

        final Map<String, String> refundMap = new TreeMap<>();
        try {
            refundMap.put("orderid", jsObject.getString("orderid"));

            Log.i("taomf+++++", goodsids);
            RequestParams refundParmas = RequestUtils.getRequestParmas(refundMap, Constant.InterfaceCode.SGREFUNDORDER, Constant.UserPower.MANAGE);

            refundParmas.put("status",status);
            refundParmas.put("bhuserid", jsObject.getString("bhuserid"));
            refundParmas.put("bhusername", jsObject.getString("bhusername"));
            refundParmas.put("bhuserphone", jsObject.getString("bhuserphone"));
            refundParmas.put("remark", "sgremark");
            refundParmas.put("goodsidlist", goodsids);

            String jsonParmas = refundParmas.toString();
            refundParmas.setApplicationJson(jsonParmas);


        HttpUtils.post(RequestUtils.getUrl(Constant.InterfaceCode.SGREFUNDORDER, jsObject.getString("orderid")), refundParmas, new BaseHttpCallBack() {
            @Override
            public void onSuccess(Object result) {
                Log.i("taomf+++++", result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    if (jsonObject.getString("resultcode").equals("0")) {  //处理成功了
                        listener.flashTuiKuan(jsonObject);
                    }else{
                        listener.errMsg(jsonObject.getString("resultdesc"));
                    }
                } catch (JSONException e) {
                    listener.errMsg(e.toString());
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int errorCode, String msg) {

                Log.i("taomf++++++", errorCode + "====" + msg);
            }
        });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印小票请求
     * @param id
     */
    @Override
    public void printTicket(String id) {
        Map<String, String> map = new TreeMap<>();
        map.put("orderid", id);
        RequestParams params = RequestUtils.getRequestParmas(map, Constant.InterfaceCode.PRINTTICKET, Constant.UserPower.MANAGE);

        String jsonParmas = params.toString();
        params.setApplicationJson(jsonParmas);

        HttpUtils.post(RequestUtils.getUrl(Constant.InterfaceCode.ORDERDETAIL, id), params, new BaseHttpCallBack() {
            @Override
            public void onSuccess(Object result) {
                Log.i("taomf+++++", result.toString());
                try {
                    JSONObject jsonObject = new JSONObject(result.toString());
                    Log.i("jsonObject", jsonObject.toString());
                    if(TextUtils.equals(jsonObject.getString("resultcode"),"0")){
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
