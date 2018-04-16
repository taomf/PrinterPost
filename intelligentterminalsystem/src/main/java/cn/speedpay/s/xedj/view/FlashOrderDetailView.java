package cn.speedpay.s.xedj.view;

import android.widget.BaseAdapter;

import org.json.JSONObject;

/**
 * Created by taomf on 2016/8/20.
 */
public interface FlashOrderDetailView {
    /**
     * 设置文本数据
     * @param object
     */
    void setText(JSONObject object);

    /**
     * 设置list数据
     * @param adapter 适配器
     */
    void setListViewText(BaseAdapter adapter);

    /**
     * 退款信息
     * @param jsonObject
     */
    void tuiKuanMsg(JSONObject jsonObject);

    /**
     * 打印小票
     * @param jsonObject 返回小票信息
     */
    void printTicket(JSONObject jsonObject);

    /**
     * 显示信息
     * @param str
     */
    void showMess(String str);
}
