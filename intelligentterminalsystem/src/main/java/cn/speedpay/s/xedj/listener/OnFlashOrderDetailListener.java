package cn.speedpay.s.xedj.listener;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/20.
 */
public interface OnFlashOrderDetailListener {
    void orderDeatilData(JSONObject jsonObject);

    void flashTuiKuan(JSONObject jsonObject);

    void errMsg(String string);

    void printTicket(JSONObject jsonObject);
}
