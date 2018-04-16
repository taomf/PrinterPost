package cn.speedpay.s.xedj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by taomf on 2016-8-17.
 * Description:
 */
public class CommonAdapter extends BaseAdapter{
    private List<?> list;
    private AdapterListener listener;
    public CommonAdapter(List<?> list,AdapterListener listener){
        this.list = list;
        this.listener = listener;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return listener.getView(position,convertView,parent);
    }
    public interface AdapterListener{
        View getView(int position, View convertView, ViewGroup parent);
    }
}
