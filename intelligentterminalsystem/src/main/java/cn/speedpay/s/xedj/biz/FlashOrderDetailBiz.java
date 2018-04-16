package cn.speedpay.s.xedj.biz;

/**
 * Created by Administrator on 2016/8/20.
 */
public interface FlashOrderDetailBiz {
    void orderDetailRequest(String orderid);

    void tuiKuanOrder(String goodsinfo,String status);

    void printTicket(String id);
}
