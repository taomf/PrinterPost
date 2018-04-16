package cn.speedpay.s.xedj.listener;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/20.
 */
public interface OnRejectOrderDetailListener {
    void orderDeatilData(JSONObject jsonObject);
    void rejectInfo(String str);
    void rejectErrInfo(String str);
}
