package cn.speedpay.s.xedj.biz;

/**
 * Created by Administrator on 2016/8/20.
 */
public interface MarketOrderDetailBiz {
    void orderDetailRequest(String orderid);

    void printTicket(String orderid);
}
