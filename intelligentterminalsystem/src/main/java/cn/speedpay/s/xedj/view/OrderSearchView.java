package cn.speedpay.s.xedj.view;

import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/8/21.
 */
public interface OrderSearchView {
    /**
     * 获取查询条件
     * @return
     */
    String getOrderNumSearchString();
    /**
     * 获取查询条件
     * @return
     */
    String getOrderAccountSearchString();

    /**
     * 设置适配器
     * @param adapter  适配器你
     */
    void setListView(BaseAdapter adapter);

    /**
     * 获取ListView对象
     * @return
     */
    ListView getListView();

    /**
     * 跳转界面
     * @param orderid 订单号
     * @param ordertype 订单类型
     */
    void startDeatilActivity(String orderid, String ordertype);

    void showAlertMsg(String string);
}
