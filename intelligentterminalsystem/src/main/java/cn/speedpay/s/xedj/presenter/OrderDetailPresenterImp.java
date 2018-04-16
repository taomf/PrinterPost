package cn.speedpay.s.xedj.presenter;

import android.content.Context;
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
import cn.speedpay.s.xedj.MBaseActivity;
import cn.speedpay.s.xedj.R;
import cn.speedpay.s.xedj.adapter.OrderDetailAdapter;
import cn.speedpay.s.xedj.biz.OrderDetailBiz;
import cn.speedpay.s.xedj.biz.OrderDetailBizIml;
import cn.speedpay.s.xedj.listener.OnOrderDetailListener;
import cn.speedpay.s.xedj.view.OrderDetailView;

/**
 * Created by Administrator on 2016/8/20.
 */
public class OrderDetailPresenterImp implements OrderDetailPresenter, OnOrderDetailListener, OrderDetailAdapter.OrderDetailAdapterListener {
    private JSONArray jsArray;
    private OrderDetailView mDetailView;
    private OrderDetailBiz biz;

    private List<Object> jsonList = new ArrayList<>();

    public OrderDetailPresenterImp(OrderDetailView orderDetailView) {
        this.mDetailView = orderDetailView;
        biz = new OrderDetailBizIml(this);
    }

    @Override
    public void orderDetail(String orderid) {
        biz.orderDetailRequest(orderid);
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
    public void printTicket(JSONObject jsonObject) {

    }

    @Override
    public void errMsg(String msg) {

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
            convertView = View.inflate(FastFrame.getApplication(), R.layout.order_detail_listview_textview, null);
        }
        TextView goodsName = (TextView) convertView.findViewById(R.id.tv_goods_name);
        TextView goodsCount = (TextView) convertView.findViewById(R.id.tv_goods_count);
        CheckBox gooodsCheckStatus = (CheckBox) convertView.findViewById(R.id.cb_select);

        try {
            JSONObject jsonObject = jsArray.getJSONObject(position);
            goodsName.setText(jsonObject.getString("goodsname"));
            goodsCount.setText(jsonObject.getString("goodscnt"));
        } catch (JSONException e) {
            Log.i("errodetail", e.toString());
            e.printStackTrace();
        }
        return convertView;
    }
}
