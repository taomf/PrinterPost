package cn.speedpay.s.xedj.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.speedpay.s.xedj.FastFrame;
import cn.speedpay.s.xedj.R;
import cn.speedpay.s.xedj.adapter.CommonAdapter;
import cn.speedpay.s.xedj.biz.OrderSearchBiz;
import cn.speedpay.s.xedj.biz.OrderSearchBizImp;
import cn.speedpay.s.xedj.listener.OrderSearchListener;
import cn.speedpay.s.xedj.view.OrderSearchView;

/**
 * Created by Administrator on 2016/8/18.
 */
public class OrderSearchPresenterImp implements OrderSearchPresenter, OrderSearchListener, CommonAdapter.AdapterListener, AdapterView.OnItemClickListener {
    private OrderSearchView mActivtiy;
    private OrderSearchBiz odelBiz;
    private CommonAdapter adapter;
    private List<Object> jsonList = new ArrayList<>();
    private JSONArray jsonArray = null;


    public OrderSearchPresenterImp(OrderSearchView view) {
        this.mActivtiy =  view;
        this.odelBiz = new OrderSearchBizImp(this);

    }

    @Override
    public void orderNumSearch(String id) {
        if(!TextUtils.isEmpty(id)){
            odelBiz.orderSearch(id);
        }
    }

    /**
     * 订单编号查询
     * @param
     */
    @Override
    public void orderNumSearch() {
        String numSearchString = mActivtiy.getOrderNumSearchString();
        if(!TextUtils.isEmpty(numSearchString)){
            odelBiz.orderSearch(numSearchString);
        }else {
            mActivtiy.showAlertMsg("搜索内容不能为空");
        }
    }

    /**
     * 订单账号查询
     */
    @Override
    public void orderAccount() {
        String accountSearchString = mActivtiy.getOrderAccountSearchString();

        if(!TextUtils.isEmpty(accountSearchString)){
            odelBiz.orderAccSearch(accountSearchString);
        }else {
            mActivtiy.showAlertMsg("搜索内容不能为空");
        }
    }

    /**
     * 详情搜索
     * @param map
     */
    @Override
    public void orderDetail(Map<String, String> map) {
        odelBiz.orderDetailSearch(map);
    }

    /**
     * 返回的订单查询结果
     * @param jsonObject
     */
    @Override
    public void queryResult(Object jsonObject) {
        JSONObject json = (JSONObject) jsonObject;
        try {
            jsonArray = json.getJSONArray("orderlist");
            jsonList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonList.add(jsonArray.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new CommonAdapter(jsonList, this);

        mActivtiy.setListView(adapter);
        mActivtiy.getListView().setOnItemClickListener(this);

    }

    /**
     * 返回错误信息
     * @param string  错误信息
     */
    @Override
    public void showMesg(String string) {
        mActivtiy.showAlertMsg(string);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            ViewHolder holder;
            JSONObject js = jsonArray.getJSONObject(position);
            if (convertView == null) {
                convertView = LayoutInflater.from(FastFrame.getApplication()).inflate(R.layout.textview, null);
                holder = new ViewHolder();
                holder.tv_order_type = (TextView) convertView.findViewById(R.id.tv_order_type);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
                holder.tv_order_account = (TextView) convertView.findViewById(R.id.tv_order_account);
                holder.tv_pay_method = (TextView) convertView.findViewById(R.id.tv_pay_method);
                holder.tv_pay_money = (TextView) convertView.findViewById(R.id.tv_pay_money);
                holder.tv_order_num = (TextView) convertView.findViewById(R.id.tv_order_num);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv_number.setText(position + 1 + "");
            holder.tv_order_type.setText(js.getString("ordertype"));

            Log.i("errosearch", "TMF" + position);

            holder.tv_name.setText(js.getString("name"));
            holder.tv_order_account.setText(js.getString("singleaccountno"));
            holder.tv_pay_method.setText(js.getString("orderpaychannel"));
            holder.tv_pay_money.setText(js.getString("orderpayprice"));
            holder.tv_order_num.setText(js.getString("orderid"));

        } catch (JSONException e) {
            Log.i("errosearch",e.toString()+"===="+position);
            e.printStackTrace();
        }
        return convertView;
    }

    /**
     * listView 条目点击事件
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            JSONObject js = jsonArray.getJSONObject(position);
            String orderid = js.getString("orderid");  //订单id
            String ordertype = js.getString("ordertype");  //订单类型
            mActivtiy.startDeatilActivity(orderid,ordertype);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    static class ViewHolder {
        TextView tv_number, tv_order_num, tv_order_account, tv_order_type, tv_pay_money, tv_name, tv_pay_method;
    }
}
