package cn.speedpay.s.xedj.biz;

import java.util.Map;

/**
 * Created by Administrator on 2016/8/18.
 */
public interface OrderSearchBiz {
    void orderSearch(String string);
    void orderAccSearch(String ss);
    void orderDetailSearch(Map<String, String> map);
}
