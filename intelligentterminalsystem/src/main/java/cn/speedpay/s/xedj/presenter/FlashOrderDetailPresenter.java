package cn.speedpay.s.xedj.presenter;

/**
 * Created by Administrator on 2016/8/20.
 */
public interface FlashOrderDetailPresenter {
    void orderDetail(String orderid);
    void tuiKuan(String checkCount,String status);

    void printTicket(String id);
}
