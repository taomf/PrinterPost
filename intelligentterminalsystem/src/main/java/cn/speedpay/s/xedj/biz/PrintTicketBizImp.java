package cn.speedpay.s.xedj.biz;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import cn.speedpay.s.xedj.frame.http.RequestParams;
import cn.speedpay.s.xedj.frame.http.callback.BaseHttpCallBack;
import cn.speedpay.s.xedj.listener.OrderSearchListener;
import cn.speedpay.s.xedj.utils.Constant;
import cn.speedpay.s.xedj.utils.HttpUtils;
import cn.speedpay.s.xedj.utils.RequestUtils;

/**
 * Created by Administrator on 2016/8/22.
 */
public class PrintTicketBizImp implements PrintTicketBiz {
//    PrintTicketListener listener;

//    public PrintTicketBizImp(PrintTicketListener listen) {
//        this.listener = listen;
//    }

    @Override
    public void orderSearch(String string) {
        Map<String,String> printMap = new TreeMap<>();
        printMap.put("orderid",string);

        RequestParams printParmas = RequestUtils.getRequestParmas(printMap, Constant.InterfaceCode.PRINTTICKET, Constant.UserPower.MANAGE);
        HttpUtils.post(RequestUtils.getUrl(Constant.InterfaceCode.PRINTTICKET, string), printParmas, new BaseHttpCallBack() {
            @Override
            public void onSuccess(Object result) {
                try {
                    JSONObject object = new JSONObject(result.toString());
                    if (object.getString("resultcode").equals("0") && object.getJSONArray("goodsinfo").length() != 0) {
                        Log.i("PrintTicketBizImp11", "???????????");
                        Log.i("PrintTicketBizImp22", object.toString());
                        Log.i("PrintTicketBizImp33", "........");

//                        listener.queryResult(object);
                        Log.i("PrintTicketBizImp444", "........");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("PrintTicketBizImp123", result.toString());
            }

            @Override
            public void onFailure(int errorCode, String msg) {
//                listener.showMesg(msg);
                Log.i("PrintTicketBizImp555", errorCode + "====" + msg);
            }
        });

    }
}
