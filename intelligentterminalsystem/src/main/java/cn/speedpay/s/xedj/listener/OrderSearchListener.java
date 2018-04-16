package cn.speedpay.s.xedj.listener;

/**
 * Created by Administrator on 2016/8/18.
 */
public interface OrderSearchListener {
    void queryResult(Object jsonObject);

    void showMesg(String string);
}
