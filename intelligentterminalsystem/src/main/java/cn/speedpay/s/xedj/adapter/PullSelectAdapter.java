package cn.speedpay.s.xedj.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.speedpay.s.xedj.FastFrame;
import cn.speedpay.s.xedj.R;

/**
 * Created by taomf on 2016-8-17.
 * Description:
 */
public class PullSelectAdapter extends BaseAdapter{
    private List<String> list;
    public PullSelectAdapter(List<String> list){
        this.list = list;
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
        if (convertView == null){
            convertView =  View.inflate(FastFrame.getApplication(), R.layout.pull_down_textview,null);
            TextView textView = (TextView) convertView.findViewById(R.id.tv_select);
            textView.setText(list.get(position));
        }
        return convertView;
    }

}
