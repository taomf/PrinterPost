package cn.speedpay.s.xedj.presenter;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/18.
 */
public interface OrderSearchPresenter {
    void orderNumSearch();
    void orderNumSearch(String id);
    void orderAccount();
    void orderDetail(Map<String, String> map);
}
