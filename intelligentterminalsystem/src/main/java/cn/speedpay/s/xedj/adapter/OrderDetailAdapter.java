package cn.speedpay.s.xedj.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016/8/20.
 */
public class OrderDetailAdapter extends BaseAdapter{
    private List jsonList;
    private OrderDetailAdapterListener listener;
    public OrderDetailAdapter(List jsonArray,OrderDetailAdapterListener listener) {
        this.jsonList = jsonArray;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        Log.i("jsonsize",jsonList.size()+"");
        return jsonList.size();
    }

    @Override
    public Object getItem(int position) {
       return jsonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return listener.getView(position,convertView,parent);
    }

    public interface OrderDetailAdapterListener{
        View getView(int position, View convertView, ViewGroup parent);
    }
}
