package cn.speedpay.s.xedj.listener;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/20.
 */
public interface OnOrderDetailListener {
    void orderDeatilData(JSONObject jsonObject);


    void printTicket(JSONObject jsonObject);

    void errMsg(String msg);
}
