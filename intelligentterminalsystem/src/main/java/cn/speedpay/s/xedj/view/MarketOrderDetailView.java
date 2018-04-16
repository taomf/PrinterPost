package cn.speedpay.s.xedj.view;

import android.widget.BaseAdapter;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/8/20.
 */
public interface MarketOrderDetailView {
    void setText(JSONObject object);
    void setListViewText(BaseAdapter adapter);

    void printTicket(JSONObject jsonObject);

    void showMsg(String string);

}
