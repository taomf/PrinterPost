package cn.speedpay.s.xedj.presenter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.speedpay.s.xedj.FastFrame;
import cn.speedpay.s.xedj.R;
import cn.speedpay.s.xedj.adapter.OrderDetailAdapter;
import cn.speedpay.s.xedj.biz.RejectOrderDetailBiz;
import cn.speedpay.s.xedj.biz.RejectOrderDetailBizIml;
import cn.speedpay.s.xedj.listener.OnRejectOrderDetailListener;
import cn.speedpay.s.xedj.view.RejectOrderDetailView;

/**
 * Created by Administrator on 2016/8/20.
 */
public class RejectOrderDetailPresenterImp implements RejectOrderDetailPresenter, OnRejectOrderDetailListener, OrderDetailAdapter.OrderDetailAdapterListener {
    private JSONArray jsArray;
    private RejectOrderDetailView mDetailView;
    private RejectOrderDetailBiz biz;

    private List<Object> jsonList = new ArrayList<>();

    public RejectOrderDetailPresenterImp(RejectOrderDetailView orderDetailView) {
        this.mDetailView = orderDetailView;
        biz = new RejectOrderDetailBizIml(this);
    }

    @Override
    public void orderDetail(String orderid) {
        biz.orderDetailRequest(orderid);
    }

    /**
     * 拒收订单
     */
    @Override
    public void rejectReceiver() {
        biz.rejectReceiverOrder();
    }

    /**
     * 订单详情
     *
     * @param jsonObject 订单详情信息
     */
    @Override
    public void orderDeatilData(JSONObject jsonObject) {
        mDetailView.setText(jsonObject);
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("goodsinfo");
            mDetailView.setListViewText(getAdapter(jsonArray));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void rejectInfo(String str) {
        mDetailView.rejectReceiver(str);
    }

    @Override
    public void rejectErrInfo(String str) {
        mDetailView.rejectErrMsg(str);
    }

    public BaseAdapter getAdapter(JSONArray jsonArray) {
        jsArray = jsonArray;
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonList.add(jsonArray.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        BaseAdapter adapter = new OrderDetailAdapter(jsonList, this);
        return adapter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i("viewdetail", "" + position);

        if (convertView == null) {
            convertView = View.inflate(FastFrame.getApplication(), R.layout.order_textview, null);
        }
        TextView goodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
        TextView goodsCount = (TextView) convertView.findViewById(R.id.tv_goods_count);
        TextView goodsPrice = (TextView) convertView.findViewById(R.id.tv_goods_price);

        CheckBox gooodsCheckStatus = (CheckBox) convertView.findViewById(R.id.cb_select);
        gooodsCheckStatus.setVisibility(View.GONE);

        try {
            JSONObject jsonObject = jsArray.getJSONObject(position);
            goodsName.setText(jsonObject.getString("goodsname"));
            goodsCount.setText(jsonObject.getString("goodscnt"));
            goodsPrice.setText(jsonObject.getString("goodsprice"));
        } catch (JSONException e) {
            Log.i("errodetail", e.toString());
            e.printStackTrace();
        }
        return convertView;
    }
}
