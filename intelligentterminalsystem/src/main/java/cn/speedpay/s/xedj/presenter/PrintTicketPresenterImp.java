package cn.speedpay.s.xedj.presenter;

import android.util.Log;

import cn.speedpay.s.xedj.biz.PrintTicketBiz;
import cn.speedpay.s.xedj.biz.PrintTicketBizImp;
import cn.speedpay.s.xedj.listener.OrderSearchListener;
import cn.speedpay.s.xedj.listener.PrintTicketListener;
import cn.speedpay.s.xedj.view.PrintTicketView;

/**
 * Created by Administrator on 2016/8/22.
 */
public class PrintTicketPresenterImp implements PrintTicketPresenter,PrintTicketListener {
    private PrintTicketView mView;
    private PrintTicketBiz odelBiz;

    public PrintTicketPresenterImp(PrintTicketView view) {
        mView = view;
        this.odelBiz = new PrintTicketBizImp(this);
    }

    @Override
    public void orderSearch(String string) {
        Log.i("PrintTicketPresenterImp", string);

        odelBiz.orderSearch(string);
    }

    @Override
    public void queryResult(Object jsonObject) {
        mView.printTicketDetail(jsonObject);
    }

    @Override
    public void showMesg(String string) {

    }
}
